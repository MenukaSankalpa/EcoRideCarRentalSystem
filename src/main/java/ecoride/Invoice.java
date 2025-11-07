package ecoride;

import java.time.LocalDate;
import java.text.DecimalFormat;

/**
 * Simple invoice generator for a given Reservation.
 * It accepts precomputed values from ReservationManager.calculateInvoiceDetails()
 */
public class Invoice {
    private final String invoiceId;
    private final Reservation reservation;
    private final LocalDate issueDate;
    private double basePrice;
    private double extraKmCharge;
    private double discount;
    private double tax;
    private double deposit;
    private double payable;

    public Invoice(String invoiceId, Reservation reservation) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.issueDate = LocalDate.now();
    }

    public void setAmounts(double basePrice, double extraKmCharge, double discount, double tax, double deposit, double payable) {
        this.basePrice = basePrice;
        this.extraKmCharge = extraKmCharge;
        this.discount = discount;
        this.tax = tax;
        this.deposit = deposit;
        this.payable = payable;
    }

    public String formatForPrint() {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder sb = new StringBuilder();
        sb.append("========== EcoRide Invoice ==========\n");
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Issue Date: ").append(issueDate).append("\n\n");

        Car car = reservation.getCar();
        sb.append("Car: ").append(car.getCarId()).append(" - ").append(car.getModel()).append("\n");
        sb.append("Category: ").append(car.getCategory()).append("\n");
        sb.append("Daily Rate: LKR ").append(df.format(car.getDailyRentalPrice())).append("\n\n");

        sb.append("Customer: ").append(reservation.getCustomer().getName()).append("\n");
        sb.append("Rental Start: ").append(reservation.getRentalStartDate()).append("\n");
        sb.append("Number of Days: ").append(reservation.getNumDays()).append("\n");
        sb.append("Expected Km Used: ").append(reservation.getExpectedTotalKm()).append("\n\n");

        sb.append("Base Price: LKR ").append(df.format(basePrice)).append("\n");
        sb.append("Extra Km Charge: LKR ").append(df.format(extraKmCharge)).append("\n");
        sb.append("Discount: -LKR ").append(df.format(discount)).append("\n");
        sb.append("Tax: LKR ").append(df.format(tax)).append("\n");
        sb.append("Deposit (handled): -LKR ").append(df.format(deposit)).append("\n");
        sb.append("--------------------------------------\n");
        sb.append("Final Payable: LKR ").append(df.format(payable)).append("\n");
        sb.append("======================================\n");
        return sb.toString();
    }
}
