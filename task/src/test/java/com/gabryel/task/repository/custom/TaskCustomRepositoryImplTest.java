package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataMongoTest
public class TaskCustomRepositoryImplTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private TaskCustomRepositoryImpl repository;

    @Test
    public void testFindPageableByFilters_WithTitleFilter() {
        TaskFindDTO filters = TaskFindDTO.builder().title("Task").build();
        int page = 0, size = 10;

        TaskEntity task = TaskUtils.TASK_ENTITY;

        List<TaskEntity> tasks = List.of(task);

        when(mongoOperations.find(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(tasks);
        when(mongoOperations.count(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(1L);

        Page<TaskEntity> result = repository.findPageableByFilters(filters, page, size);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.getContent(), hasSize(1));
        assertThat(result.getContent().get(0).getTitle(), is("task-title"));
    }

    @Test
    public void testFindPageableByFilters_WithEmptyFilters() {
        TaskFindDTO filters = TaskFindDTO.builder().build();
        int page = 0, size = 10;

        when(mongoOperations.find(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(Collections.emptyList());
        when(mongoOperations.count(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(0L);

        Page<TaskEntity> result = repository.findPageableByFilters(filters, page, size);

        assertThat(result.getTotalElements(), is(0L));
        assertThat(result.getContent(), is(empty()));
    }

    @Test
    public void testFindPageableByFilters_WithPriorityFilter() {
        TaskFindDTO filters = TaskFindDTO.builder().priority(5).build();
        int page = 0, size = 10;

        TaskEntity task = TaskUtils.TASK_ENTITY;

        List<TaskEntity> tasks = List.of(task);

        when(mongoOperations.find(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(tasks);
        when(mongoOperations.count(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(1L);

        Page<TaskEntity> result = repository.findPageableByFilters(filters, page, size);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.getContent(), hasSize(1));
        assertThat(result.getContent().get(0).getPriority(), is(5));
    }

    @Test
    public void testFindPageableByFilters_WithStateFilter() {
        TaskFindDTO filters = TaskFindDTO.builder().state(TaskState.INSERT).build();
        int page = 0, size = 10;

        TaskEntity task = TaskUtils.TASK_ENTITY;

        List<TaskEntity> tasks = List.of(task);

        when(mongoOperations.find(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(tasks);
        when(mongoOperations.count(any(Query.class), Mockito.eq(TaskEntity.class))).thenReturn(1L);

        Page<TaskEntity> result = repository.findPageableByFilters(filters, page, size);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.getContent(), hasSize(1));
        assertThat(result.getContent().get(0).getState(), is(TaskState.INSERT));
    }

    @Test
    public void testFindPageableByFilters_ThrowsException() {
        TaskFindDTO filters = TaskFindDTO.builder().title("Task").build();
        int page = 0, size = 10;

        when(mongoOperations.find(any(Query.class), Mockito.eq(TaskEntity.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> repository.findPageableByFilters(filters, page, size)
        );

        assertThat(exception.getMessage(), is("Database error"));
    }

}