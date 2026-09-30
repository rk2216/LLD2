package strategy;

import model.RideDetails;

public interface PricingStrategy {
    long calculateFare(RideDetails details);
}
