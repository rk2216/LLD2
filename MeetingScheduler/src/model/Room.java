package model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    String name;
    List<Meeting> calendar;

    public Room(String name) {
        this.name = name;
        calendar = new ArrayList<>();
    }

    public void addMeeting(Meeting meeting) {
        for(Meeting meet : calendar) {
            if(meet.overLaps(meeting)) {
                throw new IllegalArgumentException("Meeting overlaps with other calendar in this room");
            }
        }
        calendar.add(meeting);
    }

    public String getName() {
        return name;
    }
}