package com.example.hungryneat.table;

public enum TableStatus {
    AVAILABLE("Available", "#008000"),
    PENDING("Pending", "#FFBB33"),
    CONFIRMED("Confirmed", "#FF4444");

    private String displayName;
    private String colorCode;

    TableStatus(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }
}