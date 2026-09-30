package model;

import strategy.PricingStrategy;

import java.util.Objects;
import java.util.Optional;

public class Ride {
    private final RideKey key;
    private long driverId;
    private RideDetails details;
    private RideStatus status = RideStatus.CREATED;
    private RideReceipt receipt;

    public Ride(RideKey key, long driverId, RideDetails details) {
        this.key = Objects.requireNonNull(key, "key");
        requireValidDriverId(driverId);
        this.driverId = driverId;
        this.details = Objects.requireNonNull(details, "details");
    }

    private static void requireValidDriverId(long driverId) {
        if(driverId <= 0) {
            throw new IllegalArgumentException("Driver ID must be positive");
        }
    }

    public RideKey getKey() {
        return key;
    }
    public long getDriverId() { return driverId; }
    public RideDetails getDetails() { return details; }
    public RideStatus getStatus() { return status; }

    public void requireCreated() {
        if(status != RideStatus.CREATED) {
            throw new IllegalStateException("Ride " + key + " is " + status);
        }
    }

    public void updateRide(RideDetails newDetails, long newDriverId) {
        requireCreated();
        Objects.requireNonNull(newDetails, "newDetails");
        requireValidDriverId(newDriverId);

        details = newDetails;
        driverId = newDriverId;
    }

    public void withdraw() {
        if(this.status == RideStatus.WITHDRAWN) {
            return; //Idempotent retry; still a terminal state.
        }
        requireCreated();
        this.status = RideStatus.WITHDRAWN;
    }

    public RideReceipt closeRide(PricingStrategy strategy, boolean preferred) {
        if(this.status == RideStatus.CLOSED) {
            return getReceipt();
        }
        requireCreated();
        Objects.requireNonNull(strategy, "strategy");

        long finalFare = strategy.calculateFare(details);
        RideReceipt result = new RideReceipt(key, driverId, details, finalFare, preferred);

        // Arithmetic, strategy or receipt-validation failure changes no state.

        this.receipt = result;
        this.status = RideStatus.CLOSED;

        return result;
    }

    public RideReceipt getReceipt() {
        if(receipt == null) {
            throw new IllegalStateException("Ride has not been closed");
        }
        return receipt;
    }

    public View view() {
        return new View(key, driverId, details, status, Optional.ofNullable(receipt));
    }

    //record
    public final class View {
        private final RideKey key;
        private final long driverId;
        private final RideDetails details;
        private final RideStatus status;
        private final Optional<RideReceipt> receipt;

        View(RideKey key, long driverId, RideDetails details, RideStatus status, Optional<RideReceipt> receipt) {
            this.key = key;
            this.driverId = driverId;
            this.details = details;
            this.status = status;
            this.receipt = receipt;
        }
        public RideKey key() {
            return key;
        }
        public Optional<RideReceipt> receipt() {
            return receipt;
        }
        public long driverId() {
            return driverId;
        }
        public RideDetails details() {
            return details;
        }
    }
}
