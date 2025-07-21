package com.reservation.model;

public class Room {
    private int id;
    private String type;
    private double price;
    private String facility;

    public Room() {}

    public Room(int id, String type, double price, String facility) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.facility = facility;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getFacility() { return facility; }
    public void setFacility(String facility) { this.facility = facility; }
}
