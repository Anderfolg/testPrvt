package com.anderfolg.testpr.service;

import com.anderfolg.testpr.model.DTO.TaskDTO;
import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;
import com.anderfolg.testpr.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(TaskDTO taskDTO) {
        log.info("Creating new task");
        Task task = Task.builder()
                .taskName(validateTaskName(taskDTO.taskName()))
                .description(taskDTO.description())
                .createdAt(LocalDateTime.now())
                .dueDate(taskDTO.dueDate())
                .status(Status.PENDING)
                .build();
        return taskRepository.save(task);
    }

    private String validateTaskName(String taskName) {
        if (taskName == null || taskName.isBlank()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }
        return taskName;
    }

    @Override
    public Task getTaskById(Long id) {
        log.info("Getting task by id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public List<Task> getAllTasks() {
        log.info("Getting all tasks");
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByStatus(Status status) {
        log.info("Getting tasks by status: {}", status);
        return taskRepository.findAllByStatus(status);
    }

    @Override
    public Task updateTask(Long id, TaskDTO taskDTO) {
        log.info("Updating task with id: {}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    if (taskDTO.taskName() != null) {
                        task.setTaskName(validateTaskName(taskDTO.taskName()));
                    }
                    if (taskDTO.description() != null) {
                        task.setDescription(taskDTO.description());
                    }
                    if (taskDTO.dueDate() != null) {
                        task.setDueDate(taskDTO.dueDate());
                    }
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public Task updateTaskStatus(Long id, Status status) {
        log.info("Updating task status with id: {}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(status);
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }
}

