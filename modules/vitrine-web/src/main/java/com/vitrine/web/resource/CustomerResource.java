package com.vitrine.web.resource;

import com.vitrine.api.model.Customer;
import com.vitrine.api.service.CustomerService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    public Response register(Customer customer) {
        customerService.register(customer);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return customerService.findById(id)
                .map(customer -> Response.ok(customer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Customer customer) {
        customer.setId(id);

        customerService.update(customer);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        customerService.delete(id);
        return Response.noContent().build();
    }
}
