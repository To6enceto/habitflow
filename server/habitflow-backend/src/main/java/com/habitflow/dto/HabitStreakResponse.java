package com.habitflow.dto;

public class HabitStreakResponse {
    public Long habitId;
    public String name;
    public int streak;

    public HabitStreakResponse(Long habitId, String name, int streak) {
        this.habitId = habitId;
        this.name = name;
        this.streak = streak;
    }
}
