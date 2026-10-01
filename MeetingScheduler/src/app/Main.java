package app;

import service.BookingService;

import java.util.concurrent.Callable;

public class Main {
    public static void main(String[] args) {
        BookingService bookingService = new BookingService();

        bookingService.addRoom("Room 1");
        printOutput("Success case", () -> bookingService.scheduleMeeting(1, 1, 3));
        printOutput("Success case", () -> bookingService.scheduleMeeting(1, 4, 8));
        printOutput("Failure case", () -> bookingService.scheduleMeeting(1, 2, 5)); // Error

        bookingService.addRoom("Room 2");
        printOutput("Success case", () -> bookingService.scheduleMeeting(1, 2, 5));

        printOutput("Success case", () -> bookingService.scheduleMeeting(2, 2, 5));
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
}
