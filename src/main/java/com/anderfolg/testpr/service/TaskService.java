package com.anderfolg.testpr.service;

import com.anderfolg.testpr.model.DTO.TaskDTO;
import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;

import java.util.List;

public interface TaskService {
    Task createTask( TaskDTO taskDTO);
    Task getTaskById( Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByStatus( Status status);
    Task updateTask( Long id, TaskDTO taskDTO);
    Task updateTaskStatus( Long id, Status status);
    void deleteTask( Long id);
}
