import java.util.ArrayList;
import java.util.List;

public class Rider extends Person {
    private int completedRides;
    public Rider(String name) {
        super(name);
        completedRides = 0;
    }

    public void incrementCompletedRides() {
        completedRides++;
    }

    public int getCompletedRides() {
        return completedRides;
    }
}
