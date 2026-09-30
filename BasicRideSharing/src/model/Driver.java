package model;

// One offer and at most one assigned ride per driver.

import java.util.Objects;
import java.util.Optional;

public final class Driver extends Person {
    private RideDetails offer;
    private RideKey assignedRide;

    public Driver(long id, String name) {
        super(id, name);
    }

    public void offerRide(RideDetails details) {
        requireUnassigned();
        offer = Objects.requireNonNull(details, "details");
    }
    private void requireUnassigned() {
        if(assignedRide != null) {
            throw new IllegalStateException("Driver already has an active ride");
        }
    }

    public void withdrawOffer() {
        requireUnassigned();
        offer = null;
    }

    public boolean isAvailable() {
        return offer != null && assignedRide == null;
    }

    public boolean canServe(RideDetails request) {
        return offer != null && offer.canServe(request);
    }

    public void reserve(RideKey key, RideDetails request) {
        Objects.requireNonNull(key);
        if(!isAvailable() || !canServe(request)) {
            throw new IllegalStateException("Driver cannot serve this requeest");
        }
        assignedRide = key;
    }

    public void requireAssignment(RideKey key) {
        if(!Objects.requireNonNull(key, "key").equals(assignedRide)) {
            throw new IllegalStateException("Driver is not assigned to ride " + key);
        }
    }

    public void release(RideKey key, boolean completed) {
        requireAssignment(key);
        assignedRide = null;
        if(completed) {
            offer = null; // A completed trip consumes its offer.
        }
        //Withdrawal or reassignment preservers the original offer.
    }

    public View view() {
        return new View(getId(), getName(), Optional.ofNullable(offer), Optional.ofNullable(assignedRide));
    }

    //record
    public final class View {
        final long id;
        final String name;
        final Optional<RideDetails> offer;
        final Optional<RideKey> assignedRide;

        View(long id, String name, Optional<RideDetails> offer, Optional<RideKey> assignedRide) {
            this.id = id;
            this.name = name;
            this.offer = offer;
            this.assignedRide = assignedRide;
        }

        public boolean isAvailable() {
            return offer.isPresent() && assignedRide.isEmpty();
        }
    }

}
