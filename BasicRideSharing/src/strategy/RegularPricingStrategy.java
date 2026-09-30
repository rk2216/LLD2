package strategy;

import model.RideDetails;

public class RegularPricingStrategy implements PricingStrategy {
    private static final int AMOUNT_PER_KM = 20;

    @Override
    public long calculateFare(RideDetails details) {
        long seats = details.seats();
        long distanceKm = details.distanceKm();
        if(seats >= 2) {
            return (long)(distanceKm * seats * 0.75 * AMOUNT_PER_KM);
        }
        return distanceKm * AMOUNT_PER_KM;
    }
}
