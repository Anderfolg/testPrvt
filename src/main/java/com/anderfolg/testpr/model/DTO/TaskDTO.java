package com.anderfolg.testpr.model.DTO;

import java.time.LocalDateTime;

public record TaskDTO(
        String taskName,
        String description,
        LocalDateTime createdAt,
        LocalDateTime dueDate) {
}
