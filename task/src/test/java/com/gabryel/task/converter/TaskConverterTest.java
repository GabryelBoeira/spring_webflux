package com.gabryel.task.converter;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskConverterTest {

    @Test
    public void testToEntity_NullTaskSaveDTO() {
        TaskConverter converter = new TaskConverter();

        TaskEntity result = converter.toEntity(null);

        assertNull(result);
    }

    @Test
    public void testToEntityList_EmptyList() {
        TaskConverter converter = new TaskConverter();

        List<TaskEntity> result = converter.toEntityList(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToEntityList_NullList() {
        TaskConverter converter = new TaskConverter();

        List<TaskEntity> result = converter.toEntityList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToDetailList_NullList() {
        TaskConverter converter = new TaskConverter();

        List<TaskDetailDTO> result = converter.toDetailList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPagedResponseDTO_NullPageContent() {
        TaskConverter converter = new TaskConverter();

        Page<TaskEntity> page = Page.empty();
        PagedResponseDTO<TaskDetailDTO> result = converter.pagedResponseDTO(page);

        assertNotNull(result);
        assertNotNull(result.content());
        assertTrue(result.content().isEmpty());
    }

    @Test
    public void testToDetail_ValidEntity() {
        TaskConverter converter = new TaskConverter();
        TaskEntity entity = TaskEntity.builder()
                .id("task-id")
                .title("task-title")
                .description("task-description")
                .priority(1)
                .state(TaskState.INSERT)
                .build();

        TaskDetailDTO result = converter.toDetail(entity);

        assertNotNull(result);
        assertEquals("task-id", result.getId());
        assertEquals("task-title", result.getTitle());
        assertEquals("task-description", result.getDescription());
        assertEquals(1, result.getPriority());
        assertEquals(TaskState.INSERT, result.getState());
    }

    @Test
    public void testToDetail_NullEntity() {
        TaskConverter converter = new TaskConverter();

        TaskDetailDTO result = converter.toDetail(null);

        assertNull(result);
    }

}
