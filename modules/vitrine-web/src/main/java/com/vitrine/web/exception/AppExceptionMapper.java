package com.vitrine.web.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Provider
public class AppExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger logger = LoggerFactory.getLogger(AppExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException runtimeException) {
        if (runtimeException instanceof IllegalArgumentException) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("error", runtimeException.getMessage()))
                    .build();
        }

        if (runtimeException instanceof IllegalStateException) {
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("error", runtimeException.getMessage()))
                    .build();
        }

        if (runtimeException instanceof SecurityException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("error", runtimeException.getMessage()))
                    .build();
        }

        logger.error("Unexpected error", runtimeException);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("error", "Unexpected error"))
                .build();
    }

}
