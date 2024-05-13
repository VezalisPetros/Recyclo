package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    int points;
    int plastic_recycled;
    int paper_recycled;
    int glass_recycled;
    int streak;
    List<String> recycleDates;
    public User() {
    }
    public User(String username) {
        this.username = username;
        this.points =0;
        this.plastic_recycled =0;
        this.paper_recycled =0;
        this.glass_recycled =0;
        this.streak =0;
        this.recycleDates = new ArrayList<>();
    }

    public void addPoints(int points) {
        this.points += points;
    }
    //Getters
    public String getUsername() {
        return username;
    }

    public List<String> getRecycleDates() { // Getter for recycleDates
        return recycleDates;
    }
    public int getStreak() {
        return streak;
    }
    public int getPoints() {
        return points;
    }
    public int getPlastic_recycled() {
        return plastic_recycled;
    }
    public int getPaper_recycled() {
        return paper_recycled;
    }
    public int getGlass_recycled() {
        return glass_recycled;
    }
    //Setters
    public void setPlastic_recycled(int plastic_recycled) {
        this.plastic_recycled = plastic_recycled;
    }
    public void setPaper_recycled(int paper_recycled) {
        this.paper_recycled = paper_recycled;
    }
    public void setGlass_recycled(int glass_recycled) {
        this.glass_recycled = glass_recycled;
    }
    public void setStreak(int streak) {
        this.streak = streak;
    }
    public void setRecycleDates(List<String> recycleDates) { // Setter for recycleDates
        this.recycleDates = recycleDates;
    }

}
