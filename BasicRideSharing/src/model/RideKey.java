package model;

// Use record if possible - Below is class representation of a record

import java.util.Objects;

public final class RideKey {
    private final long riderId;
    private final long rideId;

    public RideKey(long riderId, long rideId) {
        if(rideId <=0 || rideId <=0) {
            throw new IllegalArgumentException("RiderID and ride ID must be positive");
        }
        this.riderId = riderId;
        this.rideId = rideId;
    }
    public final long riderId() {
        return riderId;
    }
    public final long rideId() {
        return rideId;
    }

    @Override
    public String toString() {
        return "riderId = " + riderId + " ; rideId = " + rideId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof RideKey)) {
            return false;
        }
        RideKey other = (RideKey) o;
        return riderId == other.riderId && rideId == other.rideId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(riderId, rideId);
    }
}
