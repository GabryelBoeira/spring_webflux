package com.gabryel.task.util;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;

@TestComponent
public class TaskUtils {

    public static TaskSaveDTO TASK_SAVED = new TaskSaveDTO("task-title", "task-description", 5);

    public static TaskEntity TASK_ENTITY = TaskEntity.builder()
            .id("task-id")
            .title("task-title")
            .description("task-description")
            .priority(5)
            .state(TaskState.INSERT)
            .build();

    public static TaskDetailDTO TASK_DETAIL = new TaskDetailDTO("task-id", "task-title", "task-description", 5, TaskState.INSERT);

    public static final PagedResponseDTO<TaskDetailDTO> PAGED_RESPONSE = new PagedResponseDTO<>();

}
