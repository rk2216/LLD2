import java.util.ArrayList;
import java.util.List;

public class Rider extends Person {
    private List<Ride> rides;
    public Rider(String name) {
        this.name = name;
        rides = new ArrayList<>();
    }

    public Ride createRide(int id, int origin, int destination, int noOfSeats) {
        if(origin >= destination) {
            System.out.println("Destination must be greater than origin");
            return null;
        }
        if(noOfSeats < 1) {
            System.out.println("Add atleast 1 seat");
            return null;
        }
        Ride ride = new Ride(this, id, origin, destination, noOfSeats);
        ride.startRide();
        rides.add(ride);
        return ride;
    }

    public boolean updateRide(int id, int origin, int destination, int noOfSeats) {
        if(origin >= destination) {
            System.out.println("Destination must be greater than origin");
            return false;
        }
        if(noOfSeats < 1) {
            System.out.println("Add atleast 1 seat");
            return false;
        }
        Ride existingRide = rides.stream()
                .filter(ride -> ride.getId() == id)
                .findFirst()
                .orElse(null);
        if(existingRide == null) {
            Ride newRide = createRide(id, origin, destination, noOfSeats);
            if(newRide == null) {
                return false;
            }
            return true;
        }
        if(existingRide.getRideStatus() != RideStatus.IN_PROGRESS) {
            System.out.println("Ride was not in progress. Can't update ride");
            return false;
        }
        if(existingRide.getRideStatus() == RideStatus.WITHDRAWN || existingRide.getRideStatus() == RideStatus.COMPLETED) {
            System.out.println("Can't update a withdrawn or completed ride");
            return false;
        }
        existingRide.setOrigin(origin);
        existingRide.setDestination(destination);
        existingRide.setNoOfSeats(noOfSeats);
        return false;
    }

    public double closeRide(int id) {
        Ride existingRide = rides.stream()
                .filter(ride -> ride.getId() == id)
                .findFirst()
                .orElse(null);
        if(existingRide == null) {
            System.out.println("Cannot close non existing ride");
            return 0;
        }
        if(existingRide.getRideStatus() != RideStatus.IN_PROGRESS) {
            System.out.println("Ride was not in progress. Can't close the ride");
            return 0;
        }
        existingRide.closeRide();
        return existingRide.calculatePrice();
    }

    public boolean withDrawRide(int id) {
        Ride existingRide = rides.stream()
                .filter(ride -> ride.getId() == id)
                .findFirst()
                .orElse(null);
        if(existingRide == null) {
            System.out.println("Cannot withdraw non existing ride");
            return false;
        }
        if(existingRide.getRideStatus() == RideStatus.COMPLETED) {
            System.out.println("Can't withdraw a completed ride");
            return false;
        }
        existingRide.withdrawRide();
        rides.remove(existingRide);
        return true;
    }

    public int getCompletedRides() {
        return rides.size();
    }
}
