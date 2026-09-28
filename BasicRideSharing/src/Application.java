import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    private List<Driver> drivers;
    private int occupiedDrivers;
    private List<Rider> riders;

    public Application() {
        drivers = new ArrayList<>();
        occupiedDrivers = 0;
        riders = new ArrayList<>();
    }

    void addDriver(String name) {
        Driver driver = new Driver(name);
        drivers.add(driver);
    }
    void addRider(String name) {
        Rider rider = new Rider(name);
        riders.add(rider);
    }

    void createRide(Rider rider, int id, int source, int destination, int noOfSeats) {
        if(drivers.size() < 2 || occupiedDrivers == drivers.size()) {
            System.out.println("Drivers are not available");
            return;
        }
        Ride newRide = rider.createRide(id, source, destination, noOfSeats);
        if(newRide == null) {
            System.out.println("Application could not create the ride");
            return;
        }
        occupiedDrivers++;
    }

    void updateRide(Rider rider, int id, int source, int destination, int noOfSeats) {
        boolean occupyDriver = rider.updateRide(id, source, destination, noOfSeats);
        if(occupyDriver) {
            occupiedDrivers++;
        }
    }

    void withdrawRide(Rider rider, int id) {
        boolean withdrawn = rider.withDrawRide(id);
        if(withdrawn)
            occupiedDrivers--;
    }

    double closeRide(Rider rider, int id) {
        double closingAmount = rider.closeRide(id);
        if(closingAmount == 0.0) {
            System.out.println("Application could not close the ride");
            return 0;
        }
        occupiedDrivers--;
        return closingAmount;
    }
}
