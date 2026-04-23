package com.vitrine.web.resource;

import com.vitrine.api.model.Payment;
import com.vitrine.api.model.PaymentMethod;
import com.vitrine.api.service.PaymentService;
import com.vitrine.web.mapper.PaymentMapperUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    public Response process(@QueryParam("orderId") Long orderId, @QueryParam("method") PaymentMethod paymentMethod) {
        Payment payment = paymentService.process(orderId, paymentMethod);
        return Response.status(Response.Status.CREATED).entity(PaymentMapperUtil.toResponse(payment))
                .build();
    }

    @GET
    @Path("/order/{orderId}")
    public Response findByOrder(@PathParam("orderId") Long orderId) {
        return paymentService.findByOrder(orderId)
                .map(payment -> Response.ok(PaymentMapperUtil.toResponse(payment)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
