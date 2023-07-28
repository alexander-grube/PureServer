package org.example.models;

public class Person {
    private String name;

    public Person() {
    }

    public Person(String name){
        this.setName(name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters long");
        }
        this.name = name;
    }
}
