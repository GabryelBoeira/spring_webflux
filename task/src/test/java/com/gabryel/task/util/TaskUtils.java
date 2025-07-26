package com.gabryel.task.util;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@TestComponent
public class TaskUtils {

    public static TaskEntity TASK_ENTITY = TaskEntity.builder()
            .id("task-id")
            .title("task-title")
            .description("task-description")
            .priority(5)
            .state(TaskState.INSERT)
            .build();

    public static TaskDetailDTO TASK_DETAIL_DTO = new TaskDetailDTO("task-id", "task-title", "task-description", 5, TaskState.INSERT);

    public static final PagedResponseDTO<TaskDetailDTO> PAGED_RESPONSE = new PagedResponseDTO<>(List.of(TaskUtils.TASK_DETAIL_DTO), 1, 0, 1);

    public static final PageRequest PAGEABLE = PageRequest.of(0, 10, Sort.by("title").ascending());

    public static final TaskSaveDTO TASK_SAVE_DTO = new TaskSaveDTO("task-title", "task-description", 5);
}
