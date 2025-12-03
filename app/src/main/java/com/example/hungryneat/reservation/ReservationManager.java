package com.example.hungryneat.reservation;

import com.example.hungryneat.table.Table;
import com.example.hungryneat.table.TableStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationManager {
    private static ReservationManager instance;
    private List<Table> tables;
    private Map<String, Reservation> reservations;

    private ReservationManager() {
        this.reservations = new HashMap<>();
        initializeTables();
    }

    public static synchronized ReservationManager getInstance() {
        if (instance == null) {
            instance = new ReservationManager();
        }
        return instance;
    }

    private void initializeTables() {
        tables = new ArrayList<>();
        // Initialize all tables as available
        for (int i = 1; i <= 8; i++) {
            tables.add(new Table("Table " + i, TableStatus.AVAILABLE));
        }
    }

    public List<Table> getTables() {
        return tables;
    }

    public boolean makeReservation(String tableName, String customerName, String time) {
        for (Table table : tables) {
            if (table.getName().equals(tableName) && table.getStatus() == TableStatus.AVAILABLE) {
                // Change table status to PENDING
                table.setStatus(TableStatus.PENDING);

                // Create reservation record
                Reservation reservation = new Reservation(tableName, customerName, time, ReservationStatus.PENDING);
                reservations.put(tableName, reservation);

                return true;
            }
        }
        return false;
    }

    public boolean confirmReservation(String tableName) {
        Reservation reservation = reservations.get(tableName);
        if (reservation != null && reservation.getStatus() == ReservationStatus.PENDING) {
            reservation.setStatus(ReservationStatus.CONFIRMED);

            // Update table status to CONFIRMED
            for (Table table : tables) {
                if (table.getName().equals(tableName)) {
                    table.setStatus(TableStatus.CONFIRMED);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cancelReservation(String tableName) {
        Reservation reservation = reservations.get(tableName);
        if (reservation != null) {
            reservations.remove(tableName);

            // Reset table status to AVAILABLE
            for (Table table : tables) {
                if (table.getName().equals(tableName)) {
                    table.setStatus(TableStatus.AVAILABLE);
                    return true;
                }
            }
        }
        return false;
    }

    public Reservation getReservation(String tableName) {
        return reservations.get(tableName);
    }
}