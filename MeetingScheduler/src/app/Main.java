package app;

import model.Booking;
import service.BookingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Callable;

public class Main {
    public static void main(String[] args) {
        BookingService scheduler = new BookingService();
        LocalDate firstDay = LocalDate.of(2026, 10, 1);
        LocalDate nextDay = firstDay.plusDays(1);

        scheduler.addRoom("Room 1");

        assertRoom("Room 1", scheduler.scheduleMeeting(firstDay, at(9), at(11)));
        assertRoom("Room 1", scheduler.scheduleMeeting(firstDay, at(11), at(12)));

        expectUnavailable(() -> scheduler.scheduleMeeting(firstDay, at(10), at(12)));

        scheduler.addRoom("Room 2");

        assertRoom("Room 2", scheduler.scheduleMeeting(firstDay, at(10), at(12)));
        assertRoom("Room 1", scheduler.scheduleMeeting(nextDay, at(10), at(12)));

        List<Booking> history = scheduler.getHistory(firstDay);
        for(Booking booking : history) {
            System.out.println(booking);
        }

        history = scheduler.getHistory(nextDay);
        for(Booking booking : history) {
            System.out.println(booking);
        }
    }

    private static void printOutput(String title, Callable<String> operation) {
        System.out.println("=======================================");
        System.out.println("Running test: " + title);
        try {
            System.out.println("Booked room " + operation.call());
        } catch (Exception ex) {
            System.out.println("Error occurred " + ex.getMessage());
        }
    }

    private static void assertRoom (String expected, String actual) {
        if(!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    private static LocalTime at(int hour) {
        return LocalTime.of(hour, 0);
    }

    private static void expectUnavailable(Runnable booking) {
        try{
            booking.run();
            throw new AssertionError("Expected no available room");
        } catch (IllegalStateException exception) {
            // All currently configured rooms are occupied
        }
    }
}
