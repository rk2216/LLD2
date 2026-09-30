package service;

import model.*;
import strategy.PricingStrategy;
import strategy.PricingStrategyFactory;

import java.util.*;

/*
    In-memory, single-service-instance implementation.
    Every public operation shares this service's monitor, so multi-entity workflows
    cannot interleave. Managed mutable objects never escape; only views/IDs do.
    This is NOT a database transaction or a lock shared by multiple applicaton nodes.
 */

public class RideService {
    private final Map<Long, Rider> riders = new HashMap<>();
    private final Map<Long, Driver> drivers = new LinkedHashMap<>();
    private final Map<RideKey, Ride> rides = new HashMap<>();
    private final PricingStrategyFactory pricingStrategyFactory;
    private long nextRiderId = 1;
    private long nextDriverId = 1;

    public RideService() {
        this(new PricingStrategyFactory());
    }
    public RideService(PricingStrategyFactory pricingStrategyFactory) {
        this.pricingStrategyFactory = Objects.requireNonNull(pricingStrategyFactory, "pricingFactory");
    }

    public synchronized long addDriver(String name) {
        Driver driver = new Driver(nextDriverId, name);
        nextDriverId = Math.incrementExact(nextDriverId);
        drivers.put(driver.getId(), driver);
        return driver.getId();
    }

    public synchronized long addRider(String name) {
        Rider rider = new Rider(nextRiderId, name);
        nextRiderId = Math.incrementExact(nextRiderId);
        riders.put(rider.getId(), rider);
        return rider.getId();
    }

    public synchronized Driver.View offerRide(long driverId, int origin, int destination, int seats) {
        Driver driver = requireDriver(driverId);
        driver.offerRide(new RideDetails(origin, destination, seats));
        return driver.view();
    }

    private Driver requireDriver(long driverId) {
        requirePositiveId(driverId);
        Driver driver = drivers.get(driverId);
        if(driver == null) {
            throw new NoSuchElementException("Unknown driver: " + driverId);
        }
        return driver;
    }

    private void requirePositiveId(long id) {
        if(id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

    public synchronized Driver.View withdrawOffer(long driverId) {
        Driver driver = requireDriver(driverId);
        driver.withdrawOffer();
        return driver.view();
    }

    public synchronized Ride.View createRide(long riderId, int rideId, int origin, int destination, int seats) {
        requireRider(riderId);
        return create(new RideKey(riderId, rideId), new RideDetails(origin, destination, seats));
    }

    private Rider requireRider(long riderId) {
        requirePositiveId(riderId);
        Rider rider = riders.get(riderId);
        if(rider == null) {
            throw new NoSuchElementException("Unknown rider: " + riderId);
        }
        return rider;
    }

    private Ride.View create(RideKey key, RideDetails request) {
        if(rides.containsKey(key)) {
            throw new IllegalArgumentException("Ride ID already exists for rider: " + key);
        }
        Driver driver = findDriver(request);
        Ride ride = new Ride(key, driver.getId(), request);
        driver.reserve(key, request);
        rides.put(key, ride);
        return ride.view();
    }

    private Driver findDriver(RideDetails request) {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .filter(driver -> driver.canServe(request))
                .findFirst() // Stable driver-registration order
                .orElseThrow(() -> new IllegalStateException("No matching available driver"));
    }

    public synchronized Ride.View updateRide(long riderId, int rideId, int origin, int destination, int seats) {
        Ride ride = requireRide(riderId, rideId);
        return update(ride, new RideDetails(origin, destination, seats));
    }

    private Ride requireRide(long riderId, int rideId) {
        requireRider(riderId);
        RideKey key = new RideKey(riderId, rideId);
        Ride ride = rides.get(key);
        if(ride == null) {
            throw new NoSuchElementException("Unknown ride: " + key);
        }
        return ride;
    }

    private Ride.View update(Ride ride, RideDetails request) {
        ride.requireCreated();
        Driver previous = requireDriver(ride.getDriverId());
        previous.requireAssignment(ride.getKey());

        // Keep the current driver when its original offer can still serve the ride.
        Driver selected = previous.canServe(request) ? previous : findDriver(request);
        // A failed search leaves the old ride and assignment untouched.
        if(selected.getId() != previous.getId()) {
            selected.reserve(ride.getKey(), request);
            previous.release(ride.getKey(), false);
        }
        ride.updateRide(request, selected.getId());
        return ride.view();
    }

    public synchronized UpsertResult createOrUpdateRide(long riderId, int rideId, int origin, int destination, int seats) {
        requireRider(riderId);
        RideKey key = new RideKey(riderId, rideId);
        RideDetails request = new RideDetails(origin, destination, seats);
        Ride ride = rides.get(key);

        return ride == null
                ? new UpsertResult(Change.CREATED, create(key, request))
                : new UpsertResult(Change.UPDATED, update(ride, request));
    }

    public final class UpsertResult {
        private final Change change;
        private final Ride.View ride;
        UpsertResult(Change change, Ride.View ride) {
            this.change = change;
            this.ride = ride;
        }
        public Change change() {
            return change;
        }
        public Ride.View ride() {
            return ride;
        }
    }
    public enum Change {CREATED, UPDATED }

    public synchronized Ride.View withdrawRide(long riderId, int rideId) {
        Ride ride = requireRide(riderId, rideId);
       if(ride.getStatus() == RideStatus.WITHDRAWN) {
           return ride.view(); // Do not relese a driver twice.
       }
       ride.requireCreated();
       Driver driver = requireDriver(ride.getDriverId());
       driver.requireAssignment(ride.getKey());
       ride.withdraw();
       driver.release(ride.getKey(), false);
       return ride.view();
    }

    public synchronized long closeRide(long riderId, int rideId) {
        Ride ride = requireRide(riderId, rideId);
        if(ride.getStatus() == RideStatus.CLOSED) {
            return ride.getReceipt().amount(); // Never reprice or release on a retry.
        }
        ride.requireCreated();
        Driver driver = requireDriver(ride.getDriverId());
        driver.requireAssignment(ride.getKey());
        Rider rider = requireRider(riderId);
        rider.ensureCanCompleteRide(); // Overflow must fail before closing the ride.

        long completedBefore = rider.getCompletedRides();
        PricingStrategy strategy = pricingStrategyFactory.getPricingStrategy(completedBefore);
        boolean preferred = PricingStrategyFactory.isPreferred(completedBefore);
        RideReceipt receipt = ride.closeRide(strategy, preferred);

        // All guards and potentially failing calculations have succeeded.
        // These operations cannot fail for valid managed state under this service's lock.
        rider.incrementCompletedRides();
        driver.release(ride.getKey(), true);
        return receipt.amount();
    }

    public synchronized Ride.View getRide(long riderId, int rideId) {
        return requireRide(riderId, rideId).view();
    }

    public synchronized Rider.View getRider(long riderId) {
        return requireRider(riderId).view();
    }

    public synchronized Driver.View getDriver(long driverId) {
        return requireDriver(driverId).view();
    }

    public synchronized boolean isPreferredRider(long riderId) {
        return PricingStrategyFactory.isPreferred(requireRider(riderId).getCompletedRides());
    }
}
