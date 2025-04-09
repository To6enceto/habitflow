package com.habitflow.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "completions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"habit_id", "date"})
})
public class Completion extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    public Habit habit;

    @Column(nullable = false)
    public LocalDate date;
}
