package model;

import java.time.LocalDate;
import java.util.Objects;

// Immutable record of a successful room assignment.
public final class Booking {
    private final LocalDate date;
    private final Meeting meeting;
    private final String roomName;

    public Booking(LocalDate date, Meeting meeting, String roomName) {
        this.date = Objects.requireNonNull(date, "date");
        this.meeting = Objects.requireNonNull(meeting, "meeting");
        this.roomName = Objects.requireNonNull(roomName, "roomName");
    }

    public LocalDate getDate() {
        return date;
    }
    public Meeting getMeeting() {
        return meeting;
    }
    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return date + " " + meeting + " -> " + roomName;
    }
}
