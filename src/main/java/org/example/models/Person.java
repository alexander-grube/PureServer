package org.example.models;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Person {

    private int id;
    private String name;
    private String job;
    @JSONField(name = "is_adult")
    private boolean isAdult;
    @JSONField(name = "favorite_number")
    private short favoriteNumber;
    private String[] hobbies;
    private Integer[] luckyNumbers;
    private Double[] favoriteNumbers;
    private ArrayList<String> favoriteFoods;

    public Person(int id, String name, String job, boolean isAdult, short favoriteNumber, String[] hobbies, Integer[] luckyNumbers, Double[] favoriteNumbers, ArrayList<String> favoriteFoods) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.isAdult = isAdult;
        this.favoriteNumber = favoriteNumber;
        this.hobbies = hobbies;
        this.luckyNumbers = luckyNumbers;
        this.favoriteNumbers = favoriteNumbers;
        this.favoriteFoods = favoriteFoods;
    }

    public Person(String name, String job, boolean isAdult, short favoriteNumber, String[] hobbies, Integer[] luckyNumbers, Double[] favoriteNumbers, ArrayList<String> favoriteFoods) {
        this.name = name;
        this.job = job;
        this.isAdult = isAdult;
        this.favoriteNumber = favoriteNumber;
        this.hobbies = hobbies;
        this.luckyNumbers = luckyNumbers;
        this.favoriteNumbers = favoriteNumbers;
        this.favoriteFoods = favoriteFoods;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public short getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(short favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public Integer[] getLuckyNumbers() {
        return luckyNumbers;
    }

    public void setLuckyNumbers(Integer[] luckyNumbers) {
        this.luckyNumbers = luckyNumbers;
    }

    public Double[] getFavoriteNumbers() {
        return favoriteNumbers;
    }

    public void setFavoriteNumbers(Double[] favoriteNumbers) {
        this.favoriteNumbers = favoriteNumbers;
    }

    public ArrayList<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(ArrayList<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", isAdult=" + isAdult +
                ", favoriteNumber=" + favoriteNumber +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", luckyNumbers=" + Arrays.toString(luckyNumbers) +
                ", favoriteNumbers=" + Arrays.toString(favoriteNumbers) +
                ", favoriteFoods=" + favoriteFoods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && isAdult == person.isAdult && favoriteNumber == person.favoriteNumber && Objects.equals(name, person.name) && Objects.equals(job, person.job) && Objects.deepEquals(hobbies, person.hobbies) && Objects.deepEquals(luckyNumbers, person.luckyNumbers) && Objects.deepEquals(favoriteNumbers, person.favoriteNumbers) && Objects.equals(favoriteFoods, person.favoriteFoods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, job, isAdult, favoriteNumber, Arrays.hashCode(hobbies), Arrays.hashCode(luckyNumbers), Arrays.hashCode(favoriteNumbers), favoriteFoods);
    }
}
