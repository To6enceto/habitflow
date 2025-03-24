package com.habitflow.resources;

import com.habitflow.entities.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        // You could add validation logic here if needed
        user.persist();
        return Response
                .status(Response.Status.CREATED)
                .entity("User created with ID: " + user.id)
                .build();
    }
}
