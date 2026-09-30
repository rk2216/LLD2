package strategy;

import model.Rider;

import java.util.Objects;

public class PricingStrategyFactory {
    private static final long PREFFERED_THRESHOLD = 10;
    private final PricingStrategy regular;
    private final PricingStrategy preferred;

    public PricingStrategyFactory() {
        this(new RegularPricingStrategy(), new PreferredPricingStrategy());
    }

    public PricingStrategyFactory(PricingStrategy regular, PricingStrategy preferred) {
        this.regular = Objects.requireNonNull(regular, "regular");
        this.preferred = Objects.requireNonNull(preferred, "preferred");
    }

    public PricingStrategy getPricingStrategy(long previouslyCompletedRides) {
        if (isPreferred(previouslyCompletedRides)) {
            return preferred;
        }
        return regular;
    }

    public static boolean isPreferred(long completedRides) {
        if(completedRides < 0) {
            throw new IllegalArgumentException("Completed rides must not be negative");
        }
        return completedRides > PREFFERED_THRESHOLD;
    }
}