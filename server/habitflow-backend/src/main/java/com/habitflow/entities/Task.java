package com.habitflow.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task extends PanacheEntity{

    @Column(nullable = false)
    public String title; 

    public String description;

    @Column(name = "due_date")
    public LocalDate dueDate;

    @Column(name = "is_completed")
    public boolean completed;

    @ManyToOne 
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "created_at")
    public LocalDate createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDate.now();
    }
}
