package com.vitrine.web.resource;

import com.vitrine.api.model.Product;
import com.vitrine.api.service.ProductService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    public final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @POST
    public Response register(@QueryParam("initialStock") int initialStock, @Valid Product product) {
        productService.register(product, initialStock);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return productService.findById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/category/{categoryId}")
    public List<Product> findByCategory(@PathParam("categoryId") Long categoryId) {
        return productService.findByCategory(categoryId);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Product product) {
        product.setId(id);
        productService.update(product);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }
}
