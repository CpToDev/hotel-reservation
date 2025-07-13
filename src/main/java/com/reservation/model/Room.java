package com.reservation.model;

public class Room {
    private int id;
    private String type;
    private double price;
    private String facility;
    private boolean available;

    public Room(int id, String type, double price, String facility, boolean available) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.facility = facility;
        this.available = available;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getFacility() { return facility; }
    public boolean isAvailable() { return available; }
}
