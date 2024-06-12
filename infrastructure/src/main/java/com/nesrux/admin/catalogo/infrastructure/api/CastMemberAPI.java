package com.nesrux.admin.catalogo.infrastructure.api;

import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/cast_members")
@Tag(name = "Cast Member")
public interface CastMemberAPI {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new Cast member")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfly"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);


    @GetMapping
    @Operation(summary = "List all Cast members paginated")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listed successfly"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was recived"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<Object> listCastMembers(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "0") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "") final String dir
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a Cast Member by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Object getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a Cast Member by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast Member updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody Object input);

    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Delete a Cast member by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast Member updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cast Member was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable(name = "id") String id);
}
