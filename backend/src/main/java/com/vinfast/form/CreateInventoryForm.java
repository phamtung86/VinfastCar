package com.vinfast.form;

public class CreateInventoryForm {
    private String name;
    private String location;
    private int capacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "CreateInventoryForm{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                '}';
    }

}

