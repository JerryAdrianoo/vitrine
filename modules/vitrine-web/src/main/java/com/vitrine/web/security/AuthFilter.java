package com.vitrine.web.security;

import com.vitrine.web.JwtUtil;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
@Secured
public class AuthFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("Request blocked: missing or invalid Authorization header");
            abortUnauthorized(containerRequestContext);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            JwtUtil.validateToken(token);
        } catch (Exception exception) {
            logger.warn("Request blocked: invalid or expired token - {}", exception.getMessage());
            abortUnauthorized(containerRequestContext);
        }
    }

    private void abortUnauthorized(ContainerRequestContext containerRequestContext) {
        containerRequestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Unauthorized\"}")
                        .build()
        );
    }
}
