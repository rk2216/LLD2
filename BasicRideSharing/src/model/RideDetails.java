package model;

public final class RideDetails {
    private final int origin;
    private final int destination;
    private final int seats;

    public RideDetails(int origin, int destination, int seats) {
        if(destination <= origin) {
            throw new IllegalArgumentException("Destination must be greater than origin");
        }
        if(seats <= 0) {
            throw new IllegalArgumentException("Seats must be positive");
        }
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
    }
    public final int origin() {
        return origin;
    }
    public final int destination() {
        return destination;
    }
    public final int seats() {
        return seats;
    }

    public long distanceKm() {
        return (long) destination - origin;
    }

    public boolean canServe(RideDetails request) {
        return origin == request.origin() && destination == request.destination() && seats >= request.seats();
    }

}
