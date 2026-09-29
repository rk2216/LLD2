public class Ride {
    static final int AMT_PER_KM = 20;
    private int id;
    private int origin;
    private int destination;
    private int noOfSeats;
    private RideStatus status;
    private Rider rider;
    private Driver driver;

    public Ride(int id, int origin, int destination, int noOfSeats, Rider rider, Driver driver) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.noOfSeats = noOfSeats;
        this.rider = rider;
        this.driver = driver;
        status = RideStatus.CREATED;
    }

    public void updateRide(int origin, int destination, int seats) {
        if(this.status != RideStatus.CREATED) {
            throw new IllegalStateException("Cannot update a ride that is already " + this.status);
        }
        this.origin = origin;
        this.destination = destination;
        this.noOfSeats = seats;
    }

    public void withdraw() {
        if(this.status != RideStatus.CREATED) {
            throw new IllegalStateException("Cannot withdraw a ride that is already " + this.status);
        }
        this.status = RideStatus.WITHDRAWN;
    }

    public int closeRide(PricingStrategy strategy) {
        if(this.status != RideStatus.CREATED) {
            throw new IllegalStateException("Cannot close a ride that is already " + this.status);
        }
        int finalFare = strategy.calculateFare(origin, destination, noOfSeats);

        this.status = RideStatus.CLOSED;
        this.rider.incrementCompletedRides();

        return finalFare;
    }

    public RideStatus getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }
    public Driver getDriver() {
        return driver;
    }
    public Rider getRider() {
        return rider;
    }
}
