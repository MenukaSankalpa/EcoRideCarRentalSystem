package ecoride;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain entity representing a Reservation.
 * BookingDate = date reservation was made (today)
 */
public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Car car;
    private final LocalDate bookingDate;
    private final LocalDate rentalStartDate;
    private int numDays;
    private int expectedTotalKm;
    private final double refundableDeposit;
    private ReservationStatus status;

    public Reservation(Customer customer, Car car, LocalDate rentalStartDate, int numDays, int expectedTotalKm) {
        this.reservationId = UUID.randomUUID().toString();
        this.customer = customer;
        this.car = car;
        this.bookingDate = LocalDate.now();
        this.rentalStartDate = rentalStartDate;
        this.numDays = numDays;
        this.expectedTotalKm = expectedTotalKm;
        this.refundableDeposit = PricingInfo.REFUNDABLE_DEPOSIT;
        this.status = ReservationStatus.ACTIVE;

        // Reserve the car when creating reservation
        this.car.setAvailability(AvailabilityStatus.RESERVED);
    }

    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalDate getRentalStartDate() { return rentalStartDate; }
    public int getNumDays() { return numDays; }
    public int getExpectedTotalKm() { return expectedTotalKm; }
    public double getRefundableDeposit() { return refundableDeposit; }
    public ReservationStatus getStatus() { return status; }

    public void setNumDays(int numDays) { this.numDays = numDays; }
    public void setExpectedTotalKm(int expectedTotalKm) { this.expectedTotalKm = expectedTotalKm; }

    /**
     * Cancel reservation. Business rule about cancellation window is enforced by ReservationManager.
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        this.car.setAvailability(AvailabilityStatus.AVAILABLE);
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
        this.car.setAvailability(AvailabilityStatus.AVAILABLE);
    }

    @Override
    public String toString() {
        return String.format("ResID:%s | Cust:%s | Car:%s | Start:%s | Days:%d | Km:%d | Status:%s",
                reservationId.substring(0,8), customer.getName(), car.getCarId(), rentalStartDate, numDays, expectedTotalKm, status);
    }
}
