package model;

public enum RoomType {
    SINGLE("Single Occupancy"),
    DOUBLE("Double Occupancy");

    private final String name;

    RoomType(String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return name;
    }
}
