package model;

// Managed instances stay inside RideService; callers receive immutable views
public class Rider extends Person {
    private long completedRides;
    public Rider(long id, String name) {
        super(id, name);
        completedRides = 0;
    }

    public void ensureCanCompleteRide() {
        Math.incrementExact(completedRides); // throws error if value overflown
    }

    public void incrementCompletedRides() {
        completedRides = Math.incrementExact(completedRides);
    }

    public long getCompletedRides() {
        return completedRides;
    }

    public View view() {
        return new View(getId(), getName(), completedRides);
    }

    //record
    public final class View {
        final long id;
        final String name;
        final long completedRides;

        View(long id, String name, long completedRides) {
            this.id = id;
            this.name = name;
            this.completedRides = completedRides;
        }

        public long completedRides() {
            return completedRides;
        }
    }
}

