package com.example.hungryneat.reservation;

public class Reservation {
    private String tableName;
    private String customerName;
    private String phoneNumber;
    private String reservationTime;
    private ReservationStatus status;

    public Reservation(String tableName, String customerName, String reservationTime, ReservationStatus status) {
        this.tableName = tableName;
        this.customerName = customerName;
        this.reservationTime = reservationTime;
        this.status = status;
    }

    // Getters and Setters
    public String getTableName() { return tableName; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getReservationTime() { return reservationTime; }
    public ReservationStatus getStatus() { return status; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}