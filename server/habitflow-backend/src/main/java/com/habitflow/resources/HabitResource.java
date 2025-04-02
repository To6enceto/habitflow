package com.habitflow.resources;


import com.habitflow.entities.Habit;
import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/habits")
public class HabitResource {
    
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

    public static class HabitRequest {
        public String name;
        public Long userId;
    }
}
