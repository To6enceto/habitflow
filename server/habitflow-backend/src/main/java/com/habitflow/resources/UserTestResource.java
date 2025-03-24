package com.habitflow.resources;

import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test-user")
public class UserTestResource {

    @GET
    @Transactional // Required to persist entities
    public String createUser() {
        User user = new User();
        user.email = "test@habitflow.com";
        user.password = "123456";
        user.persist(); // Panache magic ✨

        return "User created with ID: " + user.id;
    }
}
