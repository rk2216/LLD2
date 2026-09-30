package model;

import java.util.Objects;

public final class RideReceipt {
    private final RideKey key;
    private final long driverId;
    private final RideDetails details;
    private final long amount;
    private final boolean preferredPricingApplied;

    public RideReceipt(RideKey key, long driverId, RideDetails details, long amount, boolean preferredPricingApplied) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(details, "details");
        if(driverId <= 0 || amount <= 0) {
            throw new IllegalArgumentException("Driver ID and fare must be positive");
        }
        this.key = key;
        this.driverId = driverId;
        this.details = details;
        this.amount = amount;
        this.preferredPricingApplied = preferredPricingApplied;
    }

    public long amount() {
        return amount;
    }
    public boolean preferredPricingApplied() {
        return preferredPricingApplied;
    }
}
