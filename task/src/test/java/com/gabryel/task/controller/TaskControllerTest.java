package com.gabryel.task.controller;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import com.gabryel.task.util.TaskUtils;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    @DisplayName("controller_mustReturnTaskDetail_whenSaveSuccessFully")
    void getSaveSuccessFully() {
        // Arrange
        TaskSaveDTO dto = TaskUtils.TASK_SAVE_DTO;
        TaskDetailDTO detailDTO = TaskUtils.TASK_DETAIL_DTO;

        when(taskService.insertTask(any(TaskSaveDTO.class)))
                .thenReturn(Mono.just(detailDTO));

        // Act & Assert
        StepVerifier.create(taskController.createTask(dto))
                .expectNext(detailDTO)
                .verifyComplete();

        verify(taskService).insertTask(any(TaskSaveDTO.class));
    }

    @Test
    @DisplayName("getAllTasks_shouldReturnPagedResponse_withValidFilter")
    void getAllTasksShouldReturnOkWithValidFilter() {
        // Arrange
        String id = null;
        String title = "title";
        String description = null;
        Integer priority = 0;
        TaskState state = null;
        Integer page = 0;
        Integer size = 10;

        PagedResponseDTO<TaskDetailDTO> pagedResponse = TaskUtils.PAGED_RESPONSE;

        when(taskService.findPaginate(eq(id), eq(title), eq(description),
                eq(priority), eq(state), eq(page), eq(size)))
                .thenReturn(Mono.just(pagedResponse));

        // Act & Assert
        StepVerifier.create(taskController.getAllTasks(id, title, description, priority, state, page, size))
                .expectNext(pagedResponse)
                .verifyComplete();

        verify(taskService).findPaginate(eq(id), eq(title), eq(description),
                eq(priority), eq(state), eq(page), eq(size));
    }

    @Test
    @DisplayName("getAllTasks_shouldThrowNoContent_whenEmpty")
    void getAllTasksShouldReturnNoContentWhenEmpty() {
        // Arrange
        String id = null;
        String title = "title";
        String description = null;
        Integer priority = 0;
        TaskState state = null;
        Integer page = 0;
        Integer size = 10;

        when(taskService.findPaginate(eq(id), eq(title), eq(description),
                eq(priority), eq(state), eq(page), eq(size)))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(taskController.getAllTasks(id, title, description, priority, state, page, size))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.NO_CONTENT))
                .verify();

        verify(taskService).findPaginate(eq(id), eq(title), eq(description),
                eq(priority), eq(state), eq(page), eq(size));
    }

    @Test
    @DisplayName("getTaskById_shouldReturnTaskDetail_withValidId")
    void getTaskByIdShouldReturnOkWithValidId() {
        // Arrange
        String id = UUID.randomUUID().toString();
        TaskDetailDTO detailDTO = TaskUtils.TASK_DETAIL_DTO;

        when(taskService.getTaskById(eq(id)))
                .thenReturn(Mono.just(detailDTO));

        // Act & Assert
        StepVerifier.create(taskController.getTaskById(id))
                .expectNext(detailDTO)
                .verifyComplete();

        verify(taskService).getTaskById(eq(id));
    }

    @Test
    @DisplayName("getTaskById_shouldThrowNoContent_whenNotFound")
    void getTaskByIdShouldReturnNoContentWhenNotFound() {
        // Arrange
        String id = UUID.randomUUID().toString();

        when(taskService.getTaskById(eq(id)))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(taskController.getTaskById(id))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.NO_CONTENT))
                .verify();

        verify(taskService).getTaskById(eq(id));
    }

    @Test
    @DisplayName("deleteTask_shouldReturnVoid_withValidId")
    void deleteTaskShouldReturnNoContentWithValidId() {
        // Arrange
        String id = UUID.randomUUID().toString();

        when(taskService.deleteById(eq(id)))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(taskController.deleteTask(id))
                .verifyComplete();

        verify(taskService).deleteById(eq(id));
    }

    @Test
    @DisplayName("deleteTask_shouldThrowBadRequest_withBlankId")
    void deleteTaskShouldReturnBadRequestWithBlankId() {
        // Arrange
        String id = " ";

        // Act & Assert
        StepVerifier.create(taskController.deleteTask(id))
                .expectErrorMatches(throwable ->
                        throwable instanceof BadRequestException &&
                                throwable.getMessage().equals("`id` é obrigatório"))
                .verify();
    }

    @Test
    @DisplayName("deleteTask_shouldThrowBadRequest_withInvalidId")
    void deleteTaskShouldReturnBadRequestWithInvalidId() {
        // Arrange
        String id = UUID.randomUUID().toString();

        when(taskService.deleteById(eq(id)))
                .thenReturn(Mono.error(new RuntimeException("Erro ao deletar")));

        // Act & Assert
        StepVerifier.create(taskController.deleteTask(id))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.BAD_REQUEST) &&
                                throwable.getMessage().contains("Erro ao deletar"))
                .verify();

        verify(taskService).deleteById(eq(id));
    }
}
