package com.habitflow.resources;

import com.habitflow.dto.LoginRequest;
import com.habitflow.dto.LoginResponse;
import com.habitflow.entities.User;
import com.habitflow.util.JwtUtils;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @POST
    @Path("/login")
    @Transactional
    public Response login(LoginRequest request){
        User user = User.find("email", request.email).firstResult();
        if(user == null || !user.password.equals(request.password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password")
                    .build();
        }

        String token = JwtUtils.generateToken(user.email, user.id);
        return Response.ok(new LoginResponse(token)).build();
    }
    
}
