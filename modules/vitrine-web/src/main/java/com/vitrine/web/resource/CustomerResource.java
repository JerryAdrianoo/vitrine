package com.vitrine.web.resource;

import com.vitrine.api.dto.CustomerRequest;
import com.vitrine.api.model.Customer;
import com.vitrine.api.service.CustomerService;
import com.vitrine.web.security.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Customers", description = "Customer management")
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Register a new customer",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @POST
    public Response register(@Valid CustomerRequest request) {
        customerService.register(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(
            summary = "List all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
            }
    )
    @Secured
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return Response.ok(customerService.findAllPaginated(page, size)).build();
    }

    @Operation(
            summary = "Find customer by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer found"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @Secured
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return customerService.findById(id)
                .map(customer -> Response.ok(customer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Operation(
            summary = "Update customer",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Customer updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @Secured
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Customer customer) {
        customer.setId(id);

        customerService.update(customer);
        return Response.noContent().build();
    }

    @Operation(
            summary = "Delete customer",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @Secured
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        customerService.delete(id);
        return Response.noContent().build();
    }
}
