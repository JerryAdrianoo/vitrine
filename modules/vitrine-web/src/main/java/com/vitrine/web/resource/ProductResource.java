package com.vitrine.web.resource;

import com.vitrine.api.model.Product;
import com.vitrine.api.service.ProductService;
import com.vitrine.web.security.Secured;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Products", description = "Product management")
@Secured
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Register a new product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @POST
    public Response register(@QueryParam("initialStock") int initialStock, @Valid Product product) {
        productService.register(product, initialStock);
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(
            summary = "List all products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
            }
    )
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return Response.ok(productService.findAllPaginated(page, size)).build();
    }

    @Operation(
            summary = "Find product by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return productService.findById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Operation(
            summary = "Find products by category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
            }
    )
    @GET
    @Path("/category/{categoryId}")
    public List<Product> findByCategory(@PathParam("categoryId") Long categoryId) {
        return productService.findByCategory(categoryId);
    }

    @Operation(
            summary = "Update product",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Product product) {
        product.setId(id);
        productService.update(product);
        return Response.noContent().build();
    }

    @Operation(
            summary = "Delete product",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }
}
