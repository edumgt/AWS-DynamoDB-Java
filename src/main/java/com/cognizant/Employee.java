package com.cognizant;

public class Employee {
    private String id;
    private String name;
    private String position;

    public Employee(String id, String name, String position) {
        this.id = id;
        this.name = name;

        this.position = position;

    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }

    public String getPosition() { return position; }

}
