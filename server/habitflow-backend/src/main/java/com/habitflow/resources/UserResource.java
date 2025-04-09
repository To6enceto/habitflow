package com.habitflow.resources;


import com.habitflow.dto.HabitStreakResponse;
import com.habitflow.entities.Completion;
import com.habitflow.entities.Habit;
import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @GET
    @Path("/{id}/habits/streaks")
    public Response getHabitStreaks(@PathParam("id") Long userId) {
        Logger log = Logger.getLogger(UserResource.class);
    
        User user = User.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    
        List<Habit> habits = Habit.list("user", user);
        List<HabitStreakResponse> response = new ArrayList<>();
        LocalDate today = LocalDate.now();
    
        for (Habit habit : habits) {
            List<Completion> completions = Completion.list("habit = ?1 and date <= ?2 order by date desc", habit, LocalDate.now());
            log.info("Habit ID: " + habit.id + " | Completions: " + completions.size());
    
            int streak = 0;
    
            if (!completions.isEmpty()) {
                LocalDate previous = completions.get(0).date;
                log.info("Latest completion date: " + previous + " | Today: " + today);
    
                if (previous.equals(today) || previous.equals(today.minusDays(1))) {
                    streak = 1;
    
                    for (int i = 1; i < completions.size(); i++) {
                        LocalDate current = completions.get(i).date;
                        log.info("Comparing: " + previous + " -> " + current);
    
                        if (previous.minusDays(1).equals(current)) {
                            streak++;
                            previous = current;
                        } else {
                            break;
                        }
                    }
                }
            }
    
            log.info("Final streak for habit " + habit.id + ": " + streak);
            response.add(new HabitStreakResponse(habit.id, habit.name, streak));
        }
    
        return Response.ok(response).build();
    }
    
}
