package com.vitrine.web.resource;

import com.vitrine.api.dto.LoginRequest;
import com.vitrine.api.model.Customer;
import com.vitrine.api.service.AuthService;
import com.vitrine.web.JwtUtil;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;

@Tag(name = "Auth", description = "Authentication")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Authenticate and get JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful — returns JWT token"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        Customer customer = authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        String token = JwtUtil.generateToken(customer);
        return Response.ok(Map.of("token", token)).build();
    }
}
