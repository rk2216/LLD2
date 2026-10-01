package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    String name;
    Map<Integer, List<Meeting>> calendar;

    public Room(String name) {
        this.name = name;
        calendar = new HashMap<>();
    }

    public void addMeeting(int day, Meeting meeting) {
        List<Meeting> calendarForTheDay = calendar.getOrDefault(day, new ArrayList<>());
        for(Meeting meet : calendarForTheDay) {
            if(meet.overLaps(meeting)) {
                throw new IllegalArgumentException("Meeting overlaps with other calendar in this room");
            }
        }
        calendarForTheDay.add(meeting);
        calendar.putIfAbsent(day, calendarForTheDay);
    }

    public String getName() {
        return name;
    }
}