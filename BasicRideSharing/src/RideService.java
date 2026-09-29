import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideService {
    private Map<Integer, Ride> rides = new HashMap<>();
    private List<Driver> availableDrivers = new ArrayList<>();

    public void addDriver(Driver driver) {
        availableDrivers.add(driver);
    }

    public void createRide(Rider rider, int id, int origin, int destination, int seats) {
        if(origin >= destination) {
            System.out.println("Invalid route: Destination must be greater than origin");
            return;
        }
        if (availableDrivers.isEmpty()) {
            System.out.println("Drivers are not available");
            return;
        }

        Driver assignedDriver = availableDrivers.remove(0);
        Ride ride = new Ride(id, origin, destination, seats, rider, assignedDriver);
        rides.put(id, ride);
    }

    public void updateRide(int id, int origin, int destination, int seats) {
        Ride ride = rides.get(id);
        if(ride == null) {
            System.out.println("Ride not found");
            return;
        }
        try {
            ride.updateRide(origin, destination, seats);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void withdrawRide(int id) {
        Ride ride = rides.get(id);
        if(ride == null) {
            System.out.println("Ride not found");
            return;
        }
        try {
            ride.withdraw();
            availableDrivers.add(ride.getDriver());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeRide(int id) {
        Ride ride = rides.get(id);
        if(ride == null) {
            System.out.println("Ride not found");
            return;
        }
        try {
            PricingStrategy strategy = PricingStrategyFactory.getPricingStrategy(ride.getRider());
            int fare = ride.closeRide(strategy);

            availableDrivers.add(ride.getDriver());

            System.out.println("Closed the ride: " + fare);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
