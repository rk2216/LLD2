public class Ride {
    static final int AMT_PER_KM = 20;
    private int id;
    private int origin;
    private int destination;
    private int noOfSeats;
    private RideStatus status;
    private Rider rider;

    public Ride(Rider rider, int id, int origin, int destination, int noOfSeats) {
        this.rider = rider;
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.noOfSeats = noOfSeats;
        status = RideStatus.IDLE;
    }

    int calculatePrice() {
        // 1. Fetch the correct algorithm based on rider status
        PricingStrategy strategy = PricingStrategyFactory.getPricingStrategy(rider);

        // 2. Execute the calculation
        int finalFare = strategy.calculateFare(origin, destination, noOfSeats);

        return finalFare;
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
