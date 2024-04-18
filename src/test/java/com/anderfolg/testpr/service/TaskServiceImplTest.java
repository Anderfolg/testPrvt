package com.anderfolg.testpr.service;

import com.anderfolg.testpr.model.DTO.TaskDTO;
import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;
import com.anderfolg.testpr.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl underTest;

    @Test
    public void testCreateTask_createsTaskAndReturnsIt_whenGivenValidDTO() {

        // Arrange
        TaskDTO taskDTO = new TaskDTO("Test Task", "Test Description", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMonths(1));

        // Mock behavior
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = (Task) invocation.getArgument(0);
            // Optional validation: assert task fields match DTO
            assertEquals(taskDTO.taskName(), savedTask.getTaskName());
            assertEquals(taskDTO.description(), savedTask.getDescription());
            assertEquals(taskDTO.dueDate(), savedTask.getDueDate());
            return savedTask;
        });

        // Act
        Task actualTask = underTest.createTask(taskDTO);

        // Assert
        assertNotNull(actualTask, "Created task should not be null");
        assertEquals(taskDTO.taskName(), actualTask.getTaskName());
        assertEquals(taskDTO.description(), actualTask.getDescription());
        assertEquals(taskDTO.dueDate(), actualTask.getDueDate());
        assertEquals(Status.PENDING, actualTask.getStatus()); // Verify default status
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testCreateTask_givenEmptyTaskName_thenThrowsException() {

        // Arrange
        TaskDTO taskDTO = new TaskDTO("", "This is a test task", LocalDateTime.now().plusDays(1),LocalDateTime.now().plusMonths(1));

        // Act & Assert (expecting exception)
        assertThrows(IllegalArgumentException.class, () -> underTest.createTask(taskDTO));
    }

    @Test
    public void testGetTaskById_ExistingId_Success() {
        // Arrange
        Long id = 1L;
        Task expectedTask = new Task(id, "Test Task", "This is a test task", Status.PENDING,LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        when(taskRepository.findById(id)).thenReturn(Optional.of(expectedTask));

        // Act
        Task retrievedTask = underTest.getTaskById(id);

        // Assert
        assertNotNull(retrievedTask);
        assertEquals(expectedTask, retrievedTask);
    }

    @Test
    public void testGetTaskById_NonexistentId_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert (expecting exception)
        assertThrows(RuntimeException.class, () -> underTest.getTaskById(id));
    }

    @Test
    public void testGetAllTasks_TasksExist_ReturnsAllTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(
                new Task(1L, "Task 1", "Desc 1", Status.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
                new Task(2L, "Task 2", "Desc 2", Status.DONE ,LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = underTest.getAllTasks();

        // Assert
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository).findAll();
    }

    @Test
    public void testGetAllTasks_NoTasksExist_ReturnsEmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Task> actualTasks = underTest.getAllTasks();

        // Assert
        assertTrue(actualTasks.isEmpty());
        verify(taskRepository).findAll();
    }

    @Test
    public void testGetTasksByStatus_ExistingTasks_ReturnsMatchingTasks() {
        // Arrange
        Status status = Status.DONE;
        List<Task> expectedTasks = Arrays.asList(
                new Task(1L, "Task 1", "Desc 1", Status.DONE, LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
                new Task(2L, "Task 2", "Desc 2", Status.DONE, LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
        when(taskRepository.findAllByStatus(status)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = underTest.getTasksByStatus(status);

        // Assert
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository).findAllByStatus(status);
    }

    @Test
    public void testGetTasksByStatus_NoMatchingTasks_ReturnsEmptyList() {
        // Arrange
        Status status = Status.DONE;
        when(taskRepository.findAllByStatus(status)).thenReturn(Collections.emptyList());

        // Act
        List<Task> actualTasks = underTest.getTasksByStatus(status);

        // Assert
        assertTrue(actualTasks.isEmpty());
        verify(taskRepository).findAllByStatus(status);
    }

    @Test
    public void testUpdateTask_ExistingTaskAndValidDTO_UpdatesTaskSuccessfully() {
        // Arrange
        Long id = 1L;
        Task existingTask = new Task(id, "Original Name", "Original Desc", Status.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        TaskDTO updateDTO = new TaskDTO("Updated Name", "Updated Desc", LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        Task expectedTask = Task.builder()
                .id(id)
                .createdAt(existingTask.getCreatedAt())
                .dueDate(updateDTO.dueDate())
                .status(existingTask.getStatus())
                .taskName(updateDTO.taskName())
                .description(updateDTO.description())
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        // Act
        Task updatedTask = underTest.updateTask(id, updateDTO);

        // Assert
        assertEquals(expectedTask, updatedTask);
    }

    @Test
    public void testUpdateTask_TaskNotFound_ThrowsException() {
        // Arrange
        Long id = 1L;
        TaskDTO updateDTO = new TaskDTO("Updated Name", "Updated Desc", null, null);

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert (expecting exception)
        assertThrows(RuntimeException.class, () -> underTest.updateTask(id, updateDTO));
    }

}
