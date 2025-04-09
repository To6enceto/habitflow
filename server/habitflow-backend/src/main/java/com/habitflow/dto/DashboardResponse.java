package com.habitflow.dto;

public class DashboardResponse {
    public int totalHabits;
    public int totalTasks;
    public int completedTasks;
    public int currentStreak;

    public DashboardResponse(int totalHabits, int totalTasks,int completedTasks, int currentStreak) {
        this.totalHabits = totalHabits;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.currentStreak = currentStreak;
    }
}
