package model;

import java.util.Objects;

public class Room implements IRoom {
    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber,Double price,RoomType roomType){
        this.roomNumber=roomNumber;
        this.price=price;
        this.roomType=roomType;
    }

    @Override
    public String getRoomNumber(){
        return roomNumber;
    }

    @Override
    public Double getRoomPrice(){
        return price;
    }

    @Override
    public RoomType getRoomType(){
        return roomType;
    }

    @Override
    public boolean isfree(){
        return price == null || price == 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        String priceDisplay = isfree() ? "Free" : String.format("$%.2f / night", price);
        return String.format("Room %s | %s | %s", roomNumber, roomType, priceDisplay);
    }
}
