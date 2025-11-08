package ecoride;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the collection of cars.
 */
public class CarInventory {
    private final Map<String, Car> cars = new HashMap<>();

    public void addCar(Car car) {
        cars.put(car.getCarId(), car);
    }

    public void updateCar(Car car) {
        cars.put(car.getCarId(), car);
    }

    public void removeCar(String carId) {
        cars.remove(carId);
    }

    public Car getCar(String carId) {
        return cars.get(carId);
    }

    public List<Car> listAllCars() {
        return new ArrayList<>(cars.values());
    }

    public List<Car> listAvailableByCategory(Category category) {
        return cars.values().stream()
                .filter(c -> c.getCategory() == category && c.isAvailable())
                .collect(Collectors.toList());
    }

    public List<Car> listAvailableCars() {
        return cars.values().stream().filter(Car::isAvailable).collect(Collectors.toList());
    }
}
