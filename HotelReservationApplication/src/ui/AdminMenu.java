package ui;

import api.AdminResource;
import model.FreeRoom;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void open(Scanner scanner) {
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            try {
                printMenu();
                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1":
                        seeAllCustomers();
                        break;
                    case "2":
                        seeAllRooms();
                        break;
                    case "3":
                        seeAllReservations();
                        break;
                    case "4":
                        addARoom(scanner);
                        break;
                    case "5":
                        System.out.println("Returning to Main Menu...");
                        inAdminMenu = false;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Invalid option. Please select a number between 1 and 5."
                        );
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error occurred. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nAdmin Menu");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
        System.out.print("Please select an option: ");
    }

    private static void seeAllCustomers() {
        Collection<?> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addARoom(Scanner scanner) {
        try {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();

            if (roomNumber.isEmpty()) {
                throw new IllegalArgumentException("Room number cannot be empty.");
            }

            System.out.print("Enter price (0 for free): ");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid price. Please enter a valid number.");
            }

            if (price < 0) {                                                     // ← added
                throw new IllegalArgumentException("Price cannot be negative.");
            }

            System.out.print("Enter room type (1 = Single, 2 = Double): ");
            String typeInput = scanner.nextLine().trim();

            RoomType roomType;
            if (typeInput.equals("1")) {
                roomType = RoomType.SINGLE;
            } else if (typeInput.equals("2")) {
                roomType = RoomType.DOUBLE;
            } else {
                throw new IllegalArgumentException("Invalid room type. Enter 1 for Single or 2 for Double.");
            }

            IRoom room = (Double.compare(price, 0.0) == 0)
                    ? new FreeRoom(roomNumber, roomType)
                    : new Room(roomNumber, price, roomType);

            adminResource.addRoom(Collections.singletonList(room));
            System.out.println("Room added successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}