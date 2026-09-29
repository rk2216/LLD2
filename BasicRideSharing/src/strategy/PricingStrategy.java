package strategy;

public interface PricingStrategy {
    int calculateFare(int origin, int destination, int seats);
}
