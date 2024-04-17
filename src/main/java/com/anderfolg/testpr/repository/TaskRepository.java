package com.anderfolg.testpr.repository;

import com.anderfolg.testpr.model.Task;
import com.anderfolg.testpr.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatus( Status status);

}
