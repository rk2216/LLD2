package strategy;

import model.Rider;

public class PricingStrategyFactory {
    public static PricingStrategy getPricingStrategy(Rider rider) {
        if (rider.getCompletedRides() >= 10) {
            return new PreferredPricingStrategy();
        }
        return new RegularPricingStrategy();
    }
}