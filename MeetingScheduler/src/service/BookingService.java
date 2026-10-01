package service;

import model.Meeting;
import model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingService {
    HashMap<String, Room> rooms;

    public BookingService() {
        rooms = new HashMap<>();
    }

    public void addRoom(String name) {
        Room room = rooms.get(name);
        if(room != null) {
            throw new IllegalArgumentException("Room with this name already exists");
        }

        rooms.put(name, new Room(name));
    }

    public String scheduleMeeting(int startTime, int endTime) {
        Meeting requestMeeting = new Meeting(startTime, endTime);
        for(Room room : rooms.values()) {
            try{
                room.addMeeting(requestMeeting);
                return room.getName();
            } catch (IllegalArgumentException ex) {
                System.out.println("Could not add meeting to room " + room.getName());
                System.out.println(ex.getMessage());
            }
        }
        throw new IllegalStateException("No rooms available");
    }
}
