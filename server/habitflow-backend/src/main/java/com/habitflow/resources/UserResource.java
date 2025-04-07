package com.habitflow.resources;


import com.habitflow.entities.Habit;
import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(User user) {
        user.persist();
        return Response
                .status(Response.Status.CREATED)
                .entity("User created with ID: " + user.id)
                .build();
    }
    
    @GET
    @Path("/{id}/habits")
    public Response getUserHabits(@PathParam("id") Long userId) {
        User user = User.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<Habit> habits = Habit.list("user", user);
        return Response.ok(habits).build();
    }
}
