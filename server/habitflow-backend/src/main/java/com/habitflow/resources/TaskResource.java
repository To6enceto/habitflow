package com.habitflow.resources;

import com.habitflow.entities.Task;
import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    
    public static class TaskRequest {
        public String title;
        public String description;
        public String dueDate; // YYYY-MM-DD
        public Boolean completed;
        public Long userId;
    }

    @POST
    @Transactional
    public Response createTask(TaskRequest request) {
        User user = User.findById(request.userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        Task task = new Task();
        task.title = request.title;
        task.description = request.description;
        if(request.dueDate != null) {
            task.dueDate = LocalDate.parse(request.dueDate);
        }
        task.completed = request.completed != null ? request.completed : false;
        task.user = user;
        task.persist();

        return Response.status(Response.Status.CREATED)
                .entity("Task created with ID: " + task.id)
                .build();
    }
}
