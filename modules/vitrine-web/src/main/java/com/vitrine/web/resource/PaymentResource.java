package com.vitrine.web.resource;

import com.vitrine.api.model.Payment;
import com.vitrine.api.model.PaymentMethod;
import com.vitrine.api.service.PaymentService;
import com.vitrine.web.mapper.PaymentMapperUtil;
import com.vitrine.web.security.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Payments", description = "Payment management")
@Secured
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Process a payment",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @POST
    public Response process(@QueryParam("orderId") Long orderId, @QueryParam("method") PaymentMethod paymentMethod) {
        Payment payment = paymentService.process(orderId, paymentMethod);
        return Response.status(Response.Status.CREATED).entity(PaymentMapperUtil.toResponse(payment))
                .build();
    }

    @Operation(
            summary = "Find payment by order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment found"),
                    @ApiResponse(responseCode = "404", description = "Payment not found")
            }
    )
    @GET
    @Path("/order/{orderId}")
    public Response findByOrder(@PathParam("orderId") Long orderId) {
        return paymentService.findByOrder(orderId)
                .map(payment -> Response.ok(PaymentMapperUtil.toResponse(payment)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
