package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {

    private static ReservationService instance;

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException(
                    "Room " + room.getRoomNumber() + " already exists."
            );
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    private boolean isRoomAvailable(IRoom room, Date checkIn, Date checkOut) {
        for (Reservation reservation : reservations) {
            if (reservation.conflictsWith(room, checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {

        if (!isRoomAvailable(room, checkInDate, checkOutDate)) {
            throw new IllegalArgumentException(
                    "Room already booked for selected dates."
            );
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        List<IRoom> availableRooms = new ArrayList<>();

        for (IRoom room : rooms.values()) {
            if (isRoomAvailable(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public Collection<IRoom> findRecommendedRooms(Date checkInDate, Date checkOutDate) {

        Date newCheckIn = addDays(checkInDate, 7);
        Date newCheckOut = addDays(checkOutDate, 7);

        return findRooms(newCheckIn, newCheckOut);
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                result.add(reservation);
            }
        }

        return result;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }
}