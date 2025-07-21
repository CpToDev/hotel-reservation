package com.reservation.model;

public class Booking {
    private int id;
    private String userEmail;
    private int roomId;
    private String roomType;
    private String checkinDate;
    private String checkoutDate;
    private int stayDays;
    private String name;
    private String dob;
    private String aadhar;
    private String address;
    private String mobile;
    private boolean cancelled;
    private String cancellationTime;
    private double cancellationFee;

    public Booking() {
    }

    public Booking(int id, String userEmail, int roomId, String roomType, String checkinDate, String checkoutDate, int stayDays,
                   String name, String dob, String aadhar, String address, String mobile) {
        this.id = id;
        this.userEmail = userEmail;
        this.roomId = roomId;
        this.roomType = roomType;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.stayDays = stayDays;
        this.name = name;
        this.dob = dob;
        this.aadhar = aadhar;
        this.address = address;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCheckinDate() {
        return checkinDate;
    }
    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }
    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getStayDays() {
        return stayDays;
    }
    public void setStayDays(int stayDays) {
        this.stayDays = stayDays;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhar() {
        return aadhar;
    }
    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancellationTime() {
        return cancellationTime;
    }
    public void setCancellationTime(String cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }
    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
}
