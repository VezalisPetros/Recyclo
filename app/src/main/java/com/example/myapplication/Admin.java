package com.example.myapplication;

public class Admin {

    private String username;
    private int highestPoints;
    private int highestPlasticRecycled;
    private int highestStreak;

    public Admin() {
    }
    public Admin(String username) {
        this.username = username;
        this.highestPoints = 0;
        this.highestPlasticRecycled = 0;
        this.highestStreak = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getHighestPoints() {
        return highestPoints;
    }

    public int getHighestPlasticRecycled() {
        return highestPlasticRecycled;
    }

    public int getHighestStreak() {
        return highestStreak;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHighestPoints(int highestPoints) {
        this.highestPoints = highestPoints;
    }

    public void setHighestPlasticRecycled(int highestPlasticRecycled) {
        this.highestPlasticRecycled = highestPlasticRecycled;
    }

    public void setHighestStreak(int highestStreak) {
        this.highestStreak = highestStreak;
    }
}
