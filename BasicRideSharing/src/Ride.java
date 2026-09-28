public class Ride {
    static final int AMT_PER_KM = 20;
    private int id;
    private int origin;
    private int destination;
    private int noOfSeats;
    private RideStatus status;

    public Ride(int id, int origin, int destination, int noOfSeats) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.noOfSeats = noOfSeats;
        status = RideStatus.IDLE;
    }

    double calculatePrice(boolean isPreferred) {
        double multiple = 1;
        if(isPreferred) {
            multiple = (noOfSeats < 2)? 0.75 : 0.5;
        } else {
            multiple = (noOfSeats < 2)? 1 : 0.75;
        }
        return (destination - origin) * noOfSeats * multiple * Ride.AMT_PER_KM;
    }

    void startRide() {
        status = RideStatus.IN_PROGRESS;
    }
    void withdrawRide() {
        status = RideStatus.WITHDRAWN;
    }
    void closeRide() {
        status = RideStatus.COMPLETED;
    }
    public RideStatus getRideStatus() {
        return status;
    }
    int getId() {
        return id;
    }
    void setOrigin(int origin) {
        this.origin = origin;
    }
    void setDestination(int destination) {
        this.destination = destination;
    }
    void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }
}
