package com.anderfolg.testpr.controller.impl;

import com.anderfolg.testpr.controller.TaskControllerSpec;
import com.anderfolg.testpr.model.DTO.TaskDTO;
import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;
import com.anderfolg.testpr.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskControllerSpec {

    private final TaskService taskService;


    @Override
    @PostMapping
    public ResponseEntity<Task> createTask( @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskDTO));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById( @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Override
    @GetMapping("/filter/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask( @PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus( @PathVariable Long id, @RequestParam Status status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @Override
    public ResponseEntity<Task> deleteTask( Long id ) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}

