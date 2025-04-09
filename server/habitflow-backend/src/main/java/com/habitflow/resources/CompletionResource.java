package com.habitflow.resources;

import com.habitflow.entities.Completion;
import com.habitflow.entities.Habit;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/completions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompletionResource {
    
    public static class CompletionRequest {
        public Long habitId;
        public String date; // YYYY-MM-DD
    }

    @POST
    @Transactional
    public Response completeHabit(CompletionRequest request) {
        Habit habit = Habit.findById(request.habitId);
        if(habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }

        LocalDate completionDate = request.date != null ? LocalDate.parse(request.date) : LocalDate.now();

        if (completionDate.isAfter(LocalDate.now())) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Cannot complete a habit for a future date").build(); 
        }
        

        boolean exists = Completion.count("habit = ?1 and date = ?2", habit, completionDate) > 0;
        if (exists) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Habit already completed on this date")
                    .build();
        }

        Completion completion = new Completion();
        completion.habit = habit;
        completion.date = completionDate;
        completion.persist();

        return Response.status(Response.Status.CREATED)
                .entity("Habit marked as completed for " + completionDate)
                .build();
    }

    @GET
    @Path("/habit/{habitId}")
    public Response getCompletions(@PathParam("habitId") Long habitId) {
        Habit habit = Habit.findById(habitId);
        if(habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }

        List<Completion> completions = Completion.list("habit", habit);
        return Response.ok(completions).build();
    }

    @GET 
    @Path("/habit/{habitId}/streak")
    public Response getStreakForHabit(@PathParam("habitId") Long habitId) {
        Habit habit = Habit.findById(habitId);
        if(habit == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Habit not found").build();
        }
    
        List<Completion> completions = Completion
            .list("habit = ?1 order by date desc", habit);
    
        if (completions.isEmpty()) {
            return Response.ok(Map.of(
                "habitId", habitId,
                "streak", 0,
                "unit", "days"
            )).build();
        }
    
        // Convert to Set for fast lookup
        var completedDates = completions.stream()
            .map(c -> c.date)
            .collect(java.util.stream.Collectors.toSet());
    
        LocalDate today = LocalDate.now();
        int streak = 0;
    
        // Start from today and count backwards
        while (completedDates.contains(today.minusDays(streak))) {
            streak++;
        }
    
        return Response.ok(Map.of(
            "habitId", habitId,
            "streak", streak,
            "unit", "days"
        )).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteCompletion(@PathParam("id") Long id) {
        Completion completion = Completion.findById(id);
        if(completion == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Completion not found").build();
        }

        completion.delete();
        return Response.noContent().build();// 204 No Content
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserCompletions(@PathParam("userId") Long userId) {
        List<Completion> completions = Completion.find("habit.user.id", userId).list();

        return Response.ok(completions).build();
    }
}
