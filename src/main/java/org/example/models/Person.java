package org.example.models;

import com.alibaba.fastjson2.annotation.JSONField;

public class Person {

    private int id;
    private String name;
    private String job;
    @JSONField(name = "is_adult")
    private boolean isAdult;
    @JSONField(name = "favorite_number")
    private short favoriteNumber;

    public Person(int id, String name, String job, boolean isAdult, short favoriteNumber) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.isAdult = isAdult;
        this.favoriteNumber = favoriteNumber;
    }

    public Person (String name, String job, boolean isAdult, short favoriteNumber) {
        this.name = name;
        this.job = job;
        this.isAdult = isAdult;
        this.favoriteNumber = favoriteNumber;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public short getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public void setFavoriteNumber(short favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name=" + name +
                ", job=" + job +
                ", isAdult=" + isAdult +
                ", favoriteNumber=" + favoriteNumber +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person person) {
            return person.getId() == this.getId() && person.getName().equals(this.getName()) && person.getJob().equals(this.getJob()) && person.isAdult() == this.isAdult() && person.getFavoriteNumber() == this.getFavoriteNumber();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId() + this.getName().hashCode() + this.getJob().hashCode() + (this.isAdult() ? 1 : 0) + this.getFavoriteNumber();
    }
}
