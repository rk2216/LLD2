//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        RideService rideService = new RideService();

        Rider R1 = new Rider("Rider 1");
        Rider R2 = new Rider("Rider 2");

        // Attempting to create without drivers
        rideService.createRide(R1, 1, 10, 20, 1);
        System.out.println("********************************************");

        rideService.addDriver( new Driver("Driver 1"));
        rideService.addDriver( new Driver("Driver 2"));

        rideService.createRide(R1, 1, 10, 20, 1);
        rideService.createRide(R1, 2, 10, 20, 1);
        rideService.createRide(R2, 3, 10, 20, 1); // Fails gracefully: Drivers are not available
        System.out.println("********************************************");

        rideService.updateRide(2, 10, 20, 2);

        rideService.closeRide(1); // Output: 200
        rideService.closeRide(2); // Output: 300
        System.out.println("********************************************");

        // Simulating the 10 rides to hit Preferred Status
        for (int i = 3; i <= 12; i++) {
            rideService.createRide(R1, i, 10, 20, 1);
            rideService.closeRide(i);
        }

        System.out.println("********************************************");
        // This is R1's 11th ride (Preferred Pricing applied)
        rideService.createRide(R1, 13, 10, 20, 2);
        rideService.closeRide(13); // Expected: 10 * 2 * 0.5 * 20 = 200

        // R2's 1st ride (Regular Pricing applied)
        rideService.createRide(R2, 14, 10, 20, 2);
        rideService.closeRide(14); // Expected: 10 * 2 * 0.75 * 20 = 300
        /*
        Expected output:
            Drivers are not available
            ********************************************
            Drivers are not available
            ********************************************
            Closed the ride: 200.0
            Closed the ride: 300.0
            ********************************************
            Closed the ride: 200.0
            Closed the ride: 200.0
            ********************************************
            Closed the ride: 200.0
            Closed the ride: 300.0
         */
    }
}