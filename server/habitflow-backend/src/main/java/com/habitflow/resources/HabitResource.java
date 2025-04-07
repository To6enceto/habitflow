package com.habitflow.resources;


import com.habitflow.entities.Habit;
import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/habits")
@Produces(MediaType.APPLICATION_JSON)
public class HabitResource {

    public static class HabitRequest {
        public String name;
        public Long userId;
    }
    
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createHabit(HabitRequest request) {
        User user = User.findById(request.userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        Habit habit = new Habit();
        habit.name = request.name;
        habit.user = user;
        habit.persist();

        return Response
                .status(Response.Status.CREATED)
                .entity("Habit created with ID: " + habit.id)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getHabit(@PathParam("id") Long id) {
        Habit habit = Habit.findById(id);
        if (habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }
        return Response.ok(habit).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateHabit(@PathParam("id") Long id, HabitRequest request) {
        Habit habit = Habit.findById(id);
        if(habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }
        habit.name = request.name;
        return Response.ok("Habit updated successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteHabit(@PathParam("id") Long id) {
        Habit habit = Habit.findById(id);
        if(habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }

        habit.delete();
        return Response.ok("Habit deleted successfully").build();
    }
}
