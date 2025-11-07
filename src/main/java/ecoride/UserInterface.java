package ecoride;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Simple console-based UI for the EcoRide system.
 * Use this for demo and screenshots in your report.
 */
public class UserInterface {
    private final CarInventory inventory = new CarInventory();
    private final PricingPolicy pricing = new PricingPolicy();
    private final ReservationManager manager = new ReservationManager(inventory, pricing);
    private final Scanner sc = new Scanner(System.in);

    public void run() {
        seedSampleData();
        printWelcome();
        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addCar(); break;
                    case "2": listCars(); break;
                    case "3": registerAndBook(); break;
                    case "4": cancelReservation(); break;
                    case "5": searchReservation(); break;
                    case "6": generateInvoice(); break;
                    case "7": updateReservation(); break;
                    case "0": System.out.println("Goodbye."); sc.close(); return;
                    default: System.out.println("Invalid option."); break;
                }
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
    }

    private void printWelcome() {
        System.out.println("======================================");
        System.out.println("   EcoRide Car Rental System (Demo)   ");
        System.out.println("======================================");
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add Car to Inventory");
        System.out.println("2. List All Cars");
        System.out.println("3. Register Customer & Book Car");
        System.out.println("4. Cancel Reservation");
        System.out.println("5. Search Reservations by Name");
        System.out.println("6. Generate Invoice (by Reservation ID)");
        System.out.println("7. Update Reservation (within 2 days of booking)");
        System.out.println("0. Exit");
        System.out.print("Select option: ");
    }

    private void seedSampleData() {
        Car c1 = new Car("C001", "Toyota Aqua", Category.COMPACT_PETROL);
        Car c2 = new Car("C002", "Nissan Leaf", Category.ELECTRIC);
        Car c3 = new Car("C003", "Toyota Prius", Category.HYBRID);
        Car c4 = new Car("C004", "BMW X5", Category.LUXURY_SUV);
        inventory.addCar(c1); inventory.addCar(c2); inventory.addCar(c3); inventory.addCar(c4);
    }

    private void addCar() {
        System.out.print("Enter Car ID: "); String id = sc.nextLine().trim();
        System.out.print("Enter Model: "); String model = sc.nextLine().trim();
        System.out.println("Select Category: 1-Compact Petrol, 2-Hybrid, 3-Electric, 4-Luxury SUV");
        String catChoice = sc.nextLine().trim();
        Category cat = parseCategory(catChoice);
        Car car = new Car(id, model, cat);
        inventory.addCar(car);
        System.out.println("Car added: " + car);
    }

    private Category parseCategory(String choice) {
        switch (choice) {
            case "1": return Category.COMPACT_PETROL;
            case "2": return Category.HYBRID;
            case "3": return Category.ELECTRIC;
            case "4": return Category.LUXURY_SUV;
            default: return Category.COMPACT_PETROL;
        }
    }

    private void listCars() {
        System.out.println("\nInventory:");
        for (Car c : inventory.listAllCars()) {
            System.out.println(c);
        }
    }

    private void registerAndBook() {
        System.out.println("\n--- Register Customer & Book ---");
        System.out.print("NIC/Passport: "); String idDoc = sc.nextLine().trim();
        System.out.print("Full name: "); String name = sc.nextLine().trim();
        System.out.print("Contact number: "); String phone = sc.nextLine().trim();
        System.out.print("Email: "); String email = sc.nextLine().trim();
        Customer customer = new Customer(idDoc, name, phone, email);

        System.out.println("Choose Category: 1-Compact Petrol,2-Hybrid,3-Electric,4-Luxury SUV");
        String catChoice = sc.nextLine().trim();
        Category cat = parseCategory(catChoice);

        System.out.print("Rental start date (YYYY-MM-DD): ");
        String dateStr = sc.nextLine().trim();
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.print("Number of days: "); int days = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Expected total kilometers: "); int kms = Integer.parseInt(sc.nextLine().trim());

        try {
            String resId = manager.createReservation(customer, cat, startDate, days, kms);
            System.out.println("Reservation successful. Reservation ID: " + resId);
        } catch (IllegalArgumentException ex) {
            System.out.println("Booking failed: " + ex.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        String id = sc.nextLine().trim();
        boolean ok = manager.cancelReservation(id);
        if (ok) System.out.println("Reservation cancelled.");
        else System.out.println("Unable to cancel reservation (may be too late or not found).");
    }

    private void updateReservation() {
        System.out.print("Enter Reservation ID to update: ");
        String id = sc.nextLine().trim();
        System.out.print("New number of days: "); int days = Integer.parseInt(sc.nextLine().trim());
        System.out.print("New expected total km: "); int kms = Integer.parseInt(sc.nextLine().trim());
        boolean ok = manager.updateReservation(id, days, kms);
        if (ok) System.out.println("Reservation updated.");
        else System.out.println("Unable to update (either not found or update window expired).");
    }

    private void searchReservation() {
        System.out.print("Enter customer name to search: ");
        String name = sc.nextLine().trim();
        List<Reservation> results = manager.searchByCustomerName(name);
        if (results.isEmpty()) {
            System.out.println("No reservations found for: " + name);
        } else {
            System.out.println("Reservations:");
            for (Reservation r : results) System.out.println(r);
        }
    }

    private void generateInvoice() {
        System.out.print("Enter Reservation ID for invoice: ");
        String id = sc.nextLine().trim();
        Reservation r = manager.findById(id);
        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }
        Map<String, Double> details = manager.calculateInvoiceDetails(r);
        Invoice inv = new Invoice("INV-" + id.substring(0, Math.min(6, id.length())), r);
        inv.setAmounts(details.get("base"), details.get("extraCharge"), details.get("discount"),
                details.get("tax"), details.get("deposit"), details.get("payable"));
        System.out.println("\n" + inv.formatForPrint());
    }
}
