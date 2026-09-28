//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Application rideApp = new Application();
        Rider R1 = new Rider("Rider 1");
        Rider R2 = new Rider("Rider 2");
        rideApp.createRide(R1, 1, 10, 20, 1);
        System.out.println("********************************************");
        rideApp.addDriver("Driver 1");
        rideApp.addDriver("Driver 2");
        rideApp.createRide(R1, 1, 10, 20, 1);
        rideApp.createRide(R1, 2, 10, 20, 1);
        rideApp.createRide(R2, 1, 10, 20, 1);
        System.out.println("********************************************");
        rideApp.updateRide(R1, 2, 10, 20, 2);
        System.out.println("Closed the ride: " + rideApp.closeRide(R1, 1));
        System.out.println("Closed the ride: " + rideApp.closeRide(R1, 2));
        System.out.println("********************************************");
        rideApp.createRide(R1, 3, 10, 20, 1);
        rideApp.createRide(R2, 1, 10, 20, 1);
        System.out.println("Closed the ride: " + rideApp.closeRide(R1, 3));
        System.out.println("Closed the ride: " + rideApp.closeRide(R2, 1));
        System.out.println("********************************************");
        rideApp.createRide(R1, 4, 10, 20, 1);
        rideApp.closeRide(R1, 4);
        rideApp.createRide(R1, 5, 10, 20, 1);
        rideApp.closeRide(R1, 5);
        rideApp.createRide(R1, 6, 10, 20, 1);
        rideApp.closeRide(R1, 6);
        rideApp.createRide(R1, 7, 10, 20, 1);
        rideApp.closeRide(R1, 7);
        rideApp.createRide(R1, 8, 10, 20, 1);
        rideApp.closeRide(R1, 8);
        rideApp.createRide(R1, 9, 10, 20, 1);
        rideApp.closeRide(R1, 9);
        rideApp.createRide(R1, 10, 10, 20, 2);
        rideApp.createRide(R2, 2, 10, 20, 2);
        System.out.println("Closed the ride: " + rideApp.closeRide(R1, 10));
        System.out.println("Closed the ride: " + rideApp.closeRide(R2, 2));
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