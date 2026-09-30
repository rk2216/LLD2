package app;

import model.Driver;
import model.Ride;
import model.RideReceipt;
import model.Rider;
import service.RideService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        RideService service = new RideService();
        long alice = service.addRider("Alice");
        long bob = service.addRider("Bob");
        long driver1 = service.addDriver("Driver 1");
        long driver2 = service.addDriver("Driver 2");

        service.offerRide(driver1, 50, 60, 4);
        service.offerRide(driver2, 50, 60, 4);

        Ride.View first = service.createRide(alice, 1, 50, 60, 1);
        RideService.UpsertResult second = service.createOrUpdateRide(bob, 1, 50, 60, 2);

        System.out.println("Same local ride Id, different keys: " + first.key() + " / " + second.ride().key());

        expectRejection(() -> service.createRide(alice, 1, 50, 60, 1));
        expectRejection(() -> service.createOrUpdateRide(alice, 2, 50, 60, 1));

        printClose(service, alice, 1, "Alice");
        printClose(service, bob, 1, "Bob");

        System.out.println("Close retry returns stored fare: " + service.closeRide(alice, 1));

        // Closed offers are consumed. Drivers must publish new offers;

        service.offerRide(driver1, 10, 20, 1);
        service.offerRide(driver2, 10, 20, 4);

        service.createRide(alice, 2, 10, 20, 1);
        Ride.View updated = service.updateRide(alice, 2, 10, 20, 3);
        System.out.println("Seat increase rematched ride 2 to driver " + updated.driverId());

        expectRejection(() -> service.updateRide(alice, 2, 30, 40, 1));
        System.out.println("Failed rematch preserved route: " + service.getRide(alice, 2).details());

        service.withdrawRide(alice, 2);
        service.withdrawRide(alice, 2); // Retry is harmless

        expectRejection(() -> service.createOrUpdateRide(alice, 2, 10, 20, 1));
        System.out.println("Withdrawal preserved driver 2's offer: " + service.getDriver(driver2).isAvailable());

        // Alice has ONE completion; the withdrawn ride does not count.
        for(int rideId = 3; rideId<=12; rideId++) {
            service.offerRide(driver1, 50, 60, 4);
            service.createRide(alice, rideId, 50, 60, 1);
            long countBefore = service.getRider(alice).completedRides();
            long fare = service.closeRide(alice, rideId);
            if(countBefore >= 9) {
                System.out.println("Completion " + (countBefore + 1) + " fare: " + fare);
            }
        }
        System.out.println("Alice preferred after 11 completions: " + service.isPreferredRider(alice));
        service.offerRide(driver1, 50, 60, 4);
        service.createRide(alice, 13, 50, 60, 1);
        printClose(service, alice, 13, "Alice's 12th completion");
    }

    private static void expectRejection(Runnable operation) {
        try {
            operation.run();
            throw new AssertionError("Expected rejection");
        } catch (IllegalArgumentException | IllegalStateException expected) {
            System.out.println("Rejected: " + expected.getMessage());
        }
    }

    private static void printClose(RideService service, long riderId, int rideId, String label) {
        long amount = service.closeRide(riderId, rideId);
        RideReceipt receipt = service.getRide(riderId, rideId).receipt().orElseThrow();
        System.out.println(label + ": fare=" + amount + ", preferred=" + receipt.preferredPricingApplied());
    }
}