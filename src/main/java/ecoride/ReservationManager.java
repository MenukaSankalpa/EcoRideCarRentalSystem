package ecoride;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Manages reservations and applies business rules and pricing logic.
 */
public class ReservationManager {
    private final Map<String, Reservation> reservations = new HashMap<>();
    private final CarInventory inventory;
    private final PricingPolicy pricing;

    public ReservationManager(CarInventory inventory, PricingPolicy pricing) {
        this.inventory = inventory;
        this.pricing = pricing;
    }

    /**
     * Creates reservation after validating:
     * - booking must be at least 3 days before start date
     * - an available car in category must exist
     * Returns reservation id if successful.
     */
    public String createReservation(Customer customer, Category category, LocalDate startDate, int numDays, int expectedTotalKm) throws IllegalArgumentException {
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today.plusDays(3))) {
            throw new IllegalArgumentException("Booking must be made at least 3 days prior to rental start date.");
        }
        List<Car> candidates = inventory.listAvailableByCategory(category);
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("No available cars in chosen category for the requested period.");
        }
        Car assigned = candidates.get(0); // simple first-fit assignment
        Reservation r = new Reservation(customer, assigned, startDate, numDays, expectedTotalKm);
        reservations.put(r.getReservationId(), r);
        return r.getReservationId();
    }

    public Reservation findById(String reservationId) {
        return reservations.get(reservationId);
    }

    public List<Reservation> searchByCustomerName(String name) {
        List<Reservation> out = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getCustomer().getName().equalsIgnoreCase(name)) out.add(r);
        }
        return out;
    }

    public List<Reservation> reservationsByDate(LocalDate date) {
        List<Reservation> out = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getRentalStartDate().equals(date)) out.add(r);
        }
        return out;
    }

    /**
     * Cancellation allowed only within 2 days from booking date.
     * Returns true if cancelled successfully.
     */
    public boolean cancelReservation(String reservationId) {
        Reservation r = reservations.get(reservationId);
        if (r == null) return false;
        LocalDate bookingDate = r.getBookingDate();
        LocalDate now = LocalDate.now();
        long daysSinceBooking = ChronoUnit.DAYS.between(bookingDate, now);
        if (daysSinceBooking > 2) {
            return false; // cannot cancel after 2 days from reservation
        }
        r.cancel();
        return true;
    }

    /**
     * Update reservation (allowed only within 2 days of booking).
     * Returns true if update applied.
     */
    public boolean updateReservation(String reservationId, int newDays, int newExpectedKm) {
        Reservation r = reservations.get(reservationId);
        if (r == null) return false;
        long daysSinceBooking = ChronoUnit.DAYS.between(r.getBookingDate(), LocalDate.now());
        if (daysSinceBooking > 2) return false;
        r.setNumDays(newDays);
        r.setExpectedTotalKm(newExpectedKm);
        return true;
    }

    /**
     * Calculate full price breakdown and return as a small map.
     * keys: base, extraCharge, discount, tax, deposit, payable
     */
    public Map<String, Double> calculateInvoiceDetails(Reservation r) {
        Map<String, Double> out = new HashMap<>();
        PricingPolicy p = pricing;
        double daily = p.getDailyRate(r.getCar().getCategory());
        double basePrice = daily * r.getNumDays();

        int freeKmPerDay = p.getFreeKm(r.getCar().getCategory());
        int freeKmTotal = freeKmPerDay * r.getNumDays();
        int extraKm = Math.max(0, r.getExpectedTotalKm() - freeKmTotal);
        double extraCharge = extraKm * p.getExtraKmCharge(r.getCar().getCategory());

        double discount = (r.getNumDays() >= 7) ? 0.10 * basePrice : 0.0;
        double subTotal = basePrice + extraCharge - discount;
        double tax = p.getTaxRate(r.getCar().getCategory()) * subTotal;
        double finalBeforeDeposit = subTotal + tax;
        double deposit = p.getDeposit();
        double payable = finalBeforeDeposit - deposit;
        if (payable < 0) payable = 0.0;

        out.put("base", round(basePrice));
        out.put("extraCharge", round(extraCharge));
        out.put("discount", round(discount));
        out.put("tax", round(tax));
        out.put("deposit", round(deposit));
        out.put("payable", round(payable));
        return out;
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public Collection<Reservation> getAllReservations() {
        return reservations.values();
    }
}
