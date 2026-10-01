package model;

import java.time.LocalDate;
import java.util.*;

public final class Room {
    private final String name;
    private final Map<LocalDate, List<Booking>> calendar = new HashMap<>();;

    public Room(String name) {
        Objects.requireNonNull(name, "name");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name must not be blank");
        }
        this.name = name;
    }

    public boolean isAvailable(LocalDate date, Meeting meeting) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(meeting, "meeting");
        for(Booking booking : calendar.getOrDefault(date, Collections.emptyList())) {
            if(booking.getMeeting().overLaps(meeting)) {
                return false;
            }
        }
        return true;
    }

    // Called by BookingService while its booking lock is held.
    public void addBooking(Booking booking) {
        Objects.requireNonNull(booking, "booking");
        if(!name.equals(booking.getRoomName())) {
            throw new IllegalArgumentException("Booking belongs to another room");
        }
        if(!isAvailable(booking.getDate(), booking.getMeeting())) {
            throw new IllegalStateException("Room is occupied for this time");
        }
        calendar.computeIfAbsent(booking.getDate(), ignored -> new ArrayList<>()).add(booking);
    }

    public String getName() {
        return name;
    }
}