package com.example.hungryneat.table;

public class Table {
    private String name;
    private TableStatus status;

    public Table(String name, TableStatus status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}