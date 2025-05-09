package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.repository.TaskRepository;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskConverter converter;

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testInsertTask_mustReturnTaskDetailDTO_whenSuccess() {
        when(converter.toEntity(any())).thenReturn(TaskUtils.TASK_ENTITY);
        when(converter.toDetail(any())).thenReturn(TaskUtils.TASK_DETAIL);
        when(repository.save(any())).thenReturn(Mono.just(TaskUtils.TASK_ENTITY));

        StepVerifier.create(taskService.insertTask(TaskUtils.TASK_SAVED))
                .expectNext(TaskUtils.TASK_DETAIL)
                .expectComplete()
                .verify();
    }

    @Test
    void testFindPaginate_shouldReturnPagedResponseDTO_whenCalled() {
        var pagedResponseMock = Mockito.mock(PagedResponseDTO.class);
        var pageResultMock = Mockito.mock(Page.class);
        when(repository.findPageableByFilters(any(TaskFindDTO.class), any(), any()))
                .thenReturn(pageResultMock);
        when(converter.pagedResponseDTO(pageResultMock)).thenReturn(pagedResponseMock);

        PagedResponseDTO<TaskDetailDTO> result = taskService.findPaginate(
                "id", "title", "desc", 1, TaskState.INSERT, 0, 10);

        verify(repository, times(1)).findPageableByFilters(any(TaskFindDTO.class), eq(0), eq(10));
        verify(converter, times(1)).pagedResponseDTO(pageResultMock);
        assert result == pagedResponseMock;
    }

    @Test
    void testDeleteById_shouldCallRepositoryDeleteById_andReturnVoidMono() {
        String id = "task-id";
        doNothing().when(repository).deleteById(id);

        StepVerifier.create(taskService.deleteById(id))
                .expectComplete()
                .verify();

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteById_shouldPropagateException_whenDeleteFails() {
        String id = "fail-id";
        doThrow(new RuntimeException("Error")).when(repository).deleteById(id);

        StepVerifier.create(taskService.deleteById(id))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error"))
                .verify();

        verify(repository).deleteById(id);
    }
}