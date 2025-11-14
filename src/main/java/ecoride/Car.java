package ecoride;

public class Car {
    private final String carId;
    private String model;
    private Category category;
    private double dailyRentalPrice;
    private AvailabilityStatus availabilityStatus;

    public Car(String carId, String model, Category category) {
        this.carId = carId;
        this.model = model;
        this.category = category;
        this.dailyRentalPrice = PricingInfo.DAILY_RATES.get(category);
        this.availabilityStatus = AvailabilityStatus.AVAILABLE;
    }

    public String getCarId() { return carId; }
    public String getModel() { return model; }
    public Category getCategory() { return category; }
    public double getDailyRentalPrice() { return dailyRentalPrice; }
    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }

    public void setModel(String model) { this.model = model; }
    public void setCategory(Category category) {
        this.category = category;
        this.dailyRentalPrice = PricingInfo.DAILY_RATES.get(category);
    }

    public boolean isAvailable() {
        return availabilityStatus == AvailabilityStatus.AVAILABLE;
    }

    public void setAvailability(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | LKR %.2f | %s",
                carId, model, category, dailyRentalPrice, availabilityStatus);
    }
}
