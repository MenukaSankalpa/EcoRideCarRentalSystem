package ecoride;

import java.util.Map;

/**
 * Static business table data for pricing.
 * Values match the coursework table (LKR).
 */
public class PricingInfo {
    public static final Map<Category, Double> DAILY_RATES = Map.of(
            Category.COMPACT_PETROL, 5000.0,
            Category.HYBRID, 7500.0,
            Category.ELECTRIC, 10000.0,
            Category.LUXURY_SUV, 15000.0
    );

    public static final Map<Category, Integer> FREE_KM = Map.of(
            Category.COMPACT_PETROL, 100,
            Category.HYBRID, 150,
            Category.ELECTRIC, 200,
            Category.LUXURY_SUV, 250
    );

    public static final Map<Category, Double> EXTRA_KM_CHARGES = Map.of(
            Category.COMPACT_PETROL, 50.0,
            Category.HYBRID, 60.0,
            Category.ELECTRIC, 40.0,
            Category.LUXURY_SUV, 75.0
    );

    public static final Map<Category, Double> TAX_RATES = Map.of(
            Category.COMPACT_PETROL, 0.10,
            Category.HYBRID, 0.12,
            Category.ELECTRIC, 0.08,
            Category.LUXURY_SUV, 0.15
    );

    public static final double REFUNDABLE_DEPOSIT = 5000.0;
}
