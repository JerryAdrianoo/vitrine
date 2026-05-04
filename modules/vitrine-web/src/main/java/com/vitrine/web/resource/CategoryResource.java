package com.vitrine.web.resource;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Category;
import com.vitrine.api.service.CategoryService;
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

@Tag(name = "Categories", description = "Category management")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "List all categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
            }
    )
    @GET
    @Secured
    public Response findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        PageResponse<Category> response = categoryService.findAllPaginated(page, size);
        return Response.ok(response).build();
    }

    @Operation(
            summary = "Find category by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @GET
    @Path("/{id}")
    @Secured
    public Response findById(@PathParam("id") Long id) {
        Category category = categoryService.findById(id);

        return Response.ok(category).build();
    }

    @Operation(
            summary = "Create a new category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @POST
    @Secured
    public Response create(@Valid Category category) {
        categoryService.create(category);

        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(
            summary = "Update category",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @PUT
    @Path("/{id}")
    @Secured
    public Response update(@PathParam("id") Long id, @Valid Category category) {
        categoryService.update(id, category);
        return Response.noContent().build();
    }

    @Operation(
            summary = "Delete category",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @DELETE
    @Path("/{id}")
    @Secured
    public Response delete(@PathParam("id") Long id) {
        categoryService.delete(id);

        return Response.noContent().build();
    }
}
