package model;

public class Meeting {
    int startTime;
    int endTime;

    public Meeting(int startTime, int endTime) {
        if(endTime <= startTime) {
            throw new IllegalArgumentException("Meeting endTime must be greater than startTime");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public boolean overLaps(Meeting meeting) {
        return !(meeting.getStartTime() >= endTime || startTime >= meeting.getEndTime());
    }
}
