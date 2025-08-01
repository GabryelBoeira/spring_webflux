package com.gabryel.task.converter;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskConverterTest {

    @InjectMocks
    private TaskConverter converter;

    @Mock
    private AddressConverter addressConverter;

    @Test
    public void testToEntity_NullTaskSaveDTO() {

        TaskEntity result = converter.toEntity(null);

        assertNull(result);
    }

    @Test
    public void testToEntityList_EmptyList() {

        List<TaskEntity> result = converter.toEntityList(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToEntityList_NullList() {

        List<TaskEntity> result = converter.toEntityList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToDetailList_NullList() {

        List<TaskDetailDTO> result = converter.toDetailList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPagedResponseDTO_NullPageContent() {

        Page<TaskEntity> page = Page.empty();
        PagedResponseDTO<TaskDetailDTO> result = converter.pagedResponseDTO(page);

        assertNotNull(result);
        assertNotNull(result.content());
        assertTrue(result.content().isEmpty());
    }

    @Test
    public void testToDetail_ValidEntity() {

        TaskEntity entity = TaskUtils.TASK_ENTITY;
        TaskDetailDTO result = converter.toDetail(entity);

        assertNotNull(result);
        assertEquals("task-id", result.getId());
        assertEquals("task-title", result.getTitle());
        assertEquals("task-description", result.getDescription());
        assertEquals(5, result.getPriority());
        assertEquals(TaskState.INSERT, result.getState());
    }

    @Test
    public void testToDetail_NullEntity() {

        TaskDetailDTO result = converter.toDetail(null);

        assertNull(result);
    }

}
