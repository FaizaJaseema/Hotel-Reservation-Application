package ui;

import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    static {
        DATE_FORMAT.setLenient(false);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Reservation Application");

        while (true) {
            try {
                printMenu();
                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1":
                        findAndReserveRoom();
                        break;

                    case "2":
                        seeMyReservations();
                        break;

                    case "3":
                        createAccount();
                        break;

                    case "4":
                        AdminMenu.open(scanner);
                        break;

                    case "5":
                        System.out.println("Goodbye!");
                        return;

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
        System.out.println("\n1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.print("Please select a number: ");
    }

    private static void findAndReserveRoom() {
        try {
            System.out.print("Enter check-in date (MM/dd/yyyy): ");
            Date checkIn = readDate();

            System.out.print("Enter check-out date (MM/dd/yyyy): ");
            Date checkOut = readDate();

            Date today = new Date();

            if (checkIn.before(today)) {
                throw new IllegalArgumentException("Check-in date cannot be in the past.");
            }

            if (!checkOut.after(checkIn)) {
                throw new IllegalArgumentException("Check-out must be after check-in.");
            }

            Collection<IRoom> rooms = hotelResource.findARoom(checkIn, checkOut);

            if (rooms.isEmpty()) {
                System.out.println("No rooms available.");

                Collection<IRoom> recommended =
                        hotelResource.findAlternativeRooms(checkIn, checkOut);

                if (recommended.isEmpty()) {
                    System.out.println("No recommended rooms available.");
                    return;
                }

                System.out.println("\nRecommended rooms (7 days later):");
                printRooms(recommended);

                System.out.print("Book one of these? (y/n): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("y")) return;

                Date newCheckIn = addDays(checkIn, 7);
                Date newCheckOut = addDays(checkOut, 7);

                bookRoom(recommended, newCheckIn, newCheckOut);
                return;
            }

            printRooms(rooms);

            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();

            IRoom selectedRoom = rooms.stream()
                    .filter(r -> r.getRoomNumber().equals(roomNumber))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid room selection."));

            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            if (hotelResource.getCustomer(email) == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            Reservation reservation =
                    hotelResource.bookARoom(email, selectedRoom, checkIn, checkOut);

            System.out.println("Reservation successful!");
            System.out.println(reservation);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void seeMyReservations() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            Collection<Reservation> reservations =
                    hotelResource.getCustomersReservations(email);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
            } else {
                reservations.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving reservations.");
        }
    }

    private static void createAccount() {
        try {
            System.out.print("First name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Last name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Date readDate() {
        try {
            return DATE_FORMAT.parse(scanner.nextLine().trim());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use MM/dd/yyyy");
        }
    }

    private static void printRooms(Collection<IRoom> rooms) {
        System.out.println("\nAvailable rooms:");
        rooms.forEach(System.out::println);
    }

    private static Date addDays(Date date, int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.add(java.util.Calendar.DATE, days);
        return cal.getTime();
    }

    private static void bookRoom(Collection<IRoom> rooms, Date checkIn, Date checkOut) {
        try {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();

            IRoom selectedRoom = rooms.stream()
                    .filter(r -> r.getRoomNumber().equals(roomNumber))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid room selection."));

            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            if (hotelResource.getCustomer(email) == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            Reservation reservation =
                    hotelResource.bookARoom(email, selectedRoom, checkIn, checkOut);

            System.out.println("Reservation successful!");
            System.out.println(reservation);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}