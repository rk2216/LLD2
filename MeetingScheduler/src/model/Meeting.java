package model;

import java.time.LocalTime;
import java.util.Objects;

// A meeting on one calendar date. The end time is exclusive
public final class Meeting {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Meeting(LocalTime startTime, LocalTime endTime) {
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
        if(!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Meeting endTime must be greater than startTime");
        }
    }

    public LocalTime getStartTime(){
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overLaps(Meeting other) {
        Objects.requireNonNull(other, "other");
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }

    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }
}
