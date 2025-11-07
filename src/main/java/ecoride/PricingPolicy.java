package ecoride;

/**
 * Wrapper around PricingInfo for clearer access.
 */
public class PricingPolicy {

    public double getDailyRate(Category category) {
        return PricingInfo.DAILY_RATES.get(category);
    }

    public int getFreeKm(Category category) {
        return PricingInfo.FREE_KM.get(category);
    }

    public double getExtraKmCharge(Category category) {
        return PricingInfo.EXTRA_KM_CHARGES.get(category);
    }

    public double getTaxRate(Category category) {
        return PricingInfo.TAX_RATES.get(category);
    }

    public double getDeposit() {
        return PricingInfo.REFUNDABLE_DEPOSIT;
    }
}
