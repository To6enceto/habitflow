package com.habitflow.resources;

import com.habitflow.dto.LoginRequest;
import com.habitflow.dto.LoginResponse;
import com.habitflow.dto.RegisterRequest;
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
        
        if(user == null || !user.checkPassword(request.password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password")
                    .build();
        }

        String token = JwtUtils.generateToken(user.email, user.id);
        return Response.ok(new LoginResponse(token)).build();
    }

    @POST 
    @Path("/register")
    @Transactional
    public Response register(RegisterRequest request) {
        if(User.find("email", request.email).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT).entity("Email already registered").build();
        }

        User user = new User();
        user.email = request.email;
        user.password = request.password;
        user.hashPassword();
        user.persist();

        return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
    }
    
}
