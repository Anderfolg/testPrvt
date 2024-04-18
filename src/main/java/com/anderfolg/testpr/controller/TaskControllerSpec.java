package com.anderfolg.testpr.controller;

import com.anderfolg.testpr.model.DTO.TaskDTO;
import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Tag(name = "Task", description = "Task management")
public interface TaskControllerSpec {

    @Operation(
            summary = "Create a new task",
            description = "Create a new task by providing task details",
            tags = {"createTask"},
            responses = {@ApiResponse(responseCode = "201", description = "Task created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))})
    ResponseEntity<Task> createTask( @RequestBody TaskDTO taskDTO);

    @Operation(summary = "Retrieve a Task by ID",
            description = "Retrieves a task by its unique identifier. The response includes the task details (id, title, description, etc.).",
            tags = {"tasks", "get"},
            responses = {@ApiResponse(responseCode = "200", description = "Task retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404" , description = "Task not found")})
    ResponseEntity<Task> getTaskById( @PathVariable Long id);


    @Operation(summary = "Retrieve All Tasks",
            description = "Retrieves a list of all tasks.",
            tags = {"tasks", "getAll"},  // Mimic the format from TutorialController
            responses = {@ApiResponse(responseCode = "200", description = "List of tasks", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))})
    ResponseEntity<List<Task>> getAllTasks();

    @Operation(summary = "Filter Tasks by Status",
            description = "Retrieves a list of tasks with the specified status.",
            tags = {"tasks", "filter"},  // Mimic the format from TutorialController
            responses = {@ApiResponse(responseCode = "200", description = "List of tasks with specified status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid status parameter")})
    ResponseEntity<List<Task>> getTasksByStatus( @RequestParam Status status);

    @Operation(summary = "Update a Task",
            description = "Updates an existing task with the provided details (optional body).",
            tags = {"tasks", "update"},
            responses = {@ApiResponse(responseCode = "200", description = "Task updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (e.g., invalid data)"),
                    @ApiResponse(responseCode = "404", description = "Task not found")})
    ResponseEntity<Task> updateTask( @PathVariable Long id, @RequestBody TaskDTO taskDTO);

    @Operation(summary = "Update Task Status",
            description = "Updates the status of an existing task.",
            tags = {"tasks", "updateStatus"},
            responses = {@ApiResponse(responseCode = "200", description = "Task status updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request (e.g., invalid status)"),
                    @ApiResponse(responseCode = "404", description = "Task not found")})
    ResponseEntity<Task> updateTaskStatus( @PathVariable Long id, @RequestParam Status status);

    @Operation(summary = "Delete Task",
            description = "Deletes the existing task.",
            tags = {"tasks", "delete"},
            responses = {@ApiResponse(responseCode = "200", description = "Task status deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found")})
    ResponseEntity<Task> deleteTask( @PathVariable Long id);
}


