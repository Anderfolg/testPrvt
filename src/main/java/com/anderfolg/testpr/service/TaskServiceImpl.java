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
import java.util.stream.Stream;

/**
 * Provides an implementation of the `TaskService` interface for CRUD operations on tasks.
 * This service class interacts with the `TaskRepository` to perform data persistence
 * and retrieval operations related to tasks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Creates a new task entity from the provided TaskDTO and persists it to the database.
     * The task name is validated to ensure it's not empty or blank before creating the task.
     *
     * @param taskDTO The data transfer object containing task details.
     * @return The created Task entity.
     * @throws IllegalArgumentException if the task name is empty or blank.
     */
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

    /**
     * Retrieves a task entity by its ID from the database. If no task is found with the provided ID,
     * a `ResourceNotFoundException` is thrown.
     *
     * @param id The ID of the task to retrieve.
     * @return The retrieved Task entity or throws an exception if not found.
     * @throws RuntimeException if the task with the provided ID is not found.
     */
    @Override
    public Task getTaskById(Long id) {
        log.info("Getting task by id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    /**
     * Retrieves a list of all task entities from the database.
     *
     * @return A list of all Task entities.
     */
    @Override
    public List<Task> getAllTasks() {
        log.info("Getting all tasks");
        return taskRepository.findAll();
    }

    /**
     * Retrieves a list of task entities with the specified status from the database.
     *
     * @param status The status to filter tasks by.
     * @return A list of Task entities with the specified status.
     */
    @Override
    public List<Task> getTasksByStatus(Status status) {
        log.info("Getting tasks by status: {}", status);
        return taskRepository.findAllByStatus(status);
    }

    /**
     * Updates an existing task entity with the provided details (optional) from the TaskDTO object.
     * If a field is not provided in the TaskDTO, the corresponding field in the existing task entity remains unchanged.
     * The updated task entity is persisted to the database.
     *
     * @param id     The ID of the task to update.
     * @param taskDTO The data transfer object containing task details (optional for update).
     * @return The updated Task entity.
     * @throws RuntimeException if the task with the provided ID is not found.
     */
    @Override
    public Task updateTask(Long id, TaskDTO taskDTO) {
        log.info("Updating task with id: {}", id);

        // Retrieve the task by ID
        Task taskToUpdate = getTaskById(id);

        // Update task fields if provided in the DTO
        if (taskDTO.taskName() != null) {
            taskToUpdate.setTaskName(validateTaskName(taskDTO.taskName()));
        }
        if (taskDTO.description() != null) {
            taskToUpdate.setDescription(taskDTO.description());
        }
        if (taskDTO.dueDate() != null) {
            taskToUpdate.setDueDate(taskDTO.dueDate());
        }

        // Save the updated task
        return taskRepository.save(taskToUpdate);
    }


    /**
     * Updates the status of an existing task entity with the provided new status.
     * Retrieves the task by ID, sets the new status, and persists the updated task to the database.
     *
     * @param id     The ID of the task to update the status for.
     * @param status The new status to set for the task.
     * @return The updated Task entity.
     * @throws RuntimeException if the task with the provided ID is not found.
     */
    @Override
    public Task updateTaskStatus(Long id, Status status) {
        log.info("Updating task status with id: {}", id);
        Task taskToUpdate = getTaskById(id);
        taskToUpdate.setStatus(status);
        return taskRepository.save(taskToUpdate);
    }

    /**
     * Deletes an existing task entity from the database based on the provided ID.
     *
     * @param id The ID of the task to delete.
     * @throws RuntimeException if the task with the provided ID is not found.
     */
    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);

        Task taskToDelete = getTaskById(id);
        taskRepository.delete(taskToDelete);
    }

    /**
     * Validates the task name to ensure it's not empty or blank. An `IllegalArgumentException`
     * is thrown if the validation fails.
     *
     * @param taskName The task name to be validated.
     * @return The validated task name.
     * @throws IllegalArgumentException if the task name is empty or blank.
     */
    private String validateTaskName(String taskName) {
        if (taskName == null || taskName.isBlank()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }
        return taskName;
    }
}

