package com.habitflow.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.quarkus.elytron.security.common.BcryptUtil;

@Entity
@Table(name = "users")
public class User extends PanacheEntity{

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){ 
        this.createdAt = LocalDateTime.now();
    }

    public void hashPassword() {
        this.password = BcryptUtil.bcryptHash(password);
    }

    public boolean checkPassword(String rawPassword){
        return BcryptUtil.matches(rawPassword, this.password);
    }
}
