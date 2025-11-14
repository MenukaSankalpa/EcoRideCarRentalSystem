
public class CarInventory {
    // Uses HashMap for instant access to the fleet by CarID
    private final Map<String, Car> cars = new HashMap<>(); 

    // O(1) performance lookup method
    public Car getCar(String carId) {
        return cars.get(carId); 
    }
}

// 2. ReservationManager (O(1) lookup for Reservation by ID)
public class ReservationManager {
    // Uses HashMap for instant access to reservations by ReservationID
    private final Map<String, Reservation> reservations = new HashMap<>(); 

    // O(1) performance lookup method
    public Reservation findById(String reservationId) {
        return reservations.get(reservationId); 
    }
}