package com.vitrine.web.resource;

import com.vitrine.api.model.Order;
import com.vitrine.api.model.OrderItem;
import com.vitrine.api.model.Product;
import com.vitrine.api.service.OrderService;
import com.vitrine.web.security.Secured;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.vitrine.api.dto.OrderItemRequest;
import com.vitrine.api.dto.OrderResponse;
import com.vitrine.web.mapper.OrderMapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Orders", description = "Order management")
@Secured
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Place a new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order placed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Customer or product not found")
            }
    )
    @POST
    public Response placeOrder(@QueryParam("customerId") Long customerId,@Valid List<OrderItemRequest> items) {
        List<OrderItem> orderItems = items.stream()
                .map(req -> {
                    OrderItem item = new OrderItem();
                    Product product = new Product();
                    product.setId(req.getProductId());
                    item.setProduct(product);
                    item.setQuantity(req.getQuantity());
                    return item;
                })
                .toList();

        Order order = orderService.placeOrder(customerId, orderItems);

        return Response.status(Response.Status.CREATED)
                .entity(OrderMapperUtil.toResponse(order))
                .build();
    }

    @Operation(
            summary = "List all orders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
            }
    )
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return Response.ok(orderService.findAllPaginated(page, size)).build();
    }

    @Operation(
            summary = "Find order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return orderService.findById(id)
                .map(order -> Response.ok(OrderMapperUtil.toResponse(order)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Operation(
            summary = "Find orders by customer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
            }
    )
    @GET
    @Path("/customer/{customerId}")
    public List<OrderResponse> findByCustomer(@PathParam("customerId") Long customerId) {
        return orderService.findByCustomer(customerId).stream()
                .map(OrderMapperUtil::toResponse)
                .toList();
    }

    @Operation(
            summary = "Cancel order",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @POST
    @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") Long id) {
        orderService.cancel(id);
        return Response.noContent().build();
    }
}
