public class PreferredPricingStrategy implements PricingStrategy {
    private static final int AMOUNT_PER_KM = 20;

    @Override
    public int calculateFare(int origin, int destination, int seats) {
        int distance = destination - origin;
        if(seats >= 2) {
            return (int)(distance * seats * 0.5 * AMOUNT_PER_KM);
        }
        return (int)(distance * 0.75 * AMOUNT_PER_KM);
    }
}
