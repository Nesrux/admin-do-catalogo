package com.nesrux.admin.catalogo.infrastructure.api;

import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.CreateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.UpdateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoListResponse;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RequestMapping(value = "videos")
@Tag(name = "video")
public interface VideoApi {
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new Video with medias")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "video created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal sercer error was thrown")
    })
    ResponseEntity<?> createFull(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "year_launched", required = false) Integer yearLaunched,
            @RequestParam(name = "duration", required = false) Double duration,
            @RequestParam(name = "rating", required = false) String rating,
            @RequestParam(name = "opened", required = false) Boolean opened,
            @RequestParam(name = "published", required = false) Boolean published,
            @RequestParam(name = "categories_id", required = false) Set<String> categories,
            @RequestParam(name = "genres_id", required = false) Set<String> genres,
            @RequestParam(name = "cast_members_id", required = false) Set<String> members,
            @RequestParam(name = "video_file", required = false) MultipartFile videoFile,
            @RequestParam(name = "trailer_file", required = false) MultipartFile trailerFile,
            @RequestParam(name = "banner_file", required = false) MultipartFile bannerFile,
            @RequestParam(name = "thumb_file", required = false) MultipartFile thumbFile,
            @RequestParam(name = "thumb_half_file", required = false) MultipartFile thumbHalfFile
    );

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new Video with medias")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "video created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal sercer error was thrown")
    })
    ResponseEntity<?> createPartial(@RequestBody CreateVideoRequest payload);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a video by it's identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Video retrived successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "500", description = "An internal sercer error was thrown")
    })
    VideoResponse getById(@PathVariable String id);

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update a video by it's identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Video retrived successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal sercer error was thrown")
    })
    ResponseEntity<?> update(
            @PathVariable String id,
            @RequestBody UpdateVideoRequest payload);

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete a video by it's identifier")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Video Deleted"),
            @ApiResponse(responseCode = "500", description = "An internal sercer error was thrown")
    })
    void deleteById(@PathVariable(name = "id") String id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List all videos paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Videos listed"),
            @ApiResponse(responseCode = "422", description = "A query param was invalid"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<VideoListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "25") int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "title") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "cast_members_ids", required = false, defaultValue = "") Set<String> castMembers,
            @RequestParam(name = "categoriess_ids", required = false, defaultValue = "") Set<String> categories,
            @RequestParam(name = "genres_ids", required = false, defaultValue = "") Set<String> genres
    );

    @GetMapping(value = "{id}/medias/{type}")
    @Operation(summary = "Get a video media by it's type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media retrived successfully"),
            @ApiResponse(responseCode = "404", description = "Media was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<byte[]> getMediaByType(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "type") String type
    );

}
