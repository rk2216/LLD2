package service;

import model.Booking;
import model.Meeting;
import model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public final class BookingService {
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final List<Booking> history = new ArrayList<>();

    public synchronized void addRoom(String name) {
        Room room = rooms.get(name);
        if(room != null) {
            throw new IllegalArgumentException("Room with this name already exists");
        }

        rooms.put(name, new Room(name));
    }

    public synchronized String scheduleMeeting(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Objects.requireNonNull(date, "date");
        Meeting meeting = new Meeting(startTime, endTime);
        if(rooms.isEmpty()) {
            throw new IllegalStateException("No rooms configured");
        }
        for(Room room : rooms.values()) {
            if(room.isAvailable(date, meeting)) {
                Booking booking = new Booking(date, meeting, room.getName());
                room.addBooking(booking);
                history.add(booking);
                return room.getName();
            }
        }
        throw new IllegalStateException("No rooms available for " + date + " " + meeting);
    }

    public synchronized List<Booking> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    public synchronized List<Booking> getHistory(LocalDate date) {
        Objects.requireNonNull(date, "date");
        List<Booking> result = new ArrayList<>();
        for(Booking booking: history) {
            if(booking.getDate().equals(date)) {
                result.add(booking);
            }
        }

        return Collections.unmodifiableList(result);
    }
}
