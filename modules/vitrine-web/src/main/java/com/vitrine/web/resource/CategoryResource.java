package com.vitrine.web.resource;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Category;
import com.vitrine.api.service.CategoryService;
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

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GET
    @Secured
    public Response findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        PageResponse<Category> response = categoryService.findAllPaginated(page, size);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @Secured
    public Response findById(@PathParam("id") Long id) {
        Category category = categoryService.findById(id);

        return Response.ok(category).build();
    }

    @POST
    @Secured
    public Response create(@Valid Category category) {
        categoryService.create(category);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response update(@PathParam("id") Long id, @Valid Category category) {
        categoryService.update(id, category);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response delete(@PathParam("id") Long id) {
        categoryService.delete(id);

        return Response.noContent().build();
    }
}
