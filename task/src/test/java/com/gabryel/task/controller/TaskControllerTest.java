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
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
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
    @DisplayName("controller_mustReturnOk_whenSaveSuccessFully")
    void getSaveSuccessFully() {
        // Arrange
        TaskSaveDTO input = new TaskSaveDTO("Teste", "Descrição de teste", 1);
        TaskDetailDTO detailDTO = new TaskDetailDTO("123", "Teste", "Descrição de teste", 1, TaskState.INSERT);

        // Crie um ServerResponse para o mock retornar
        ServerResponse mockResponse = ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(detailDTO)
                .block(); // Isso cria um ServerResponse real para o teste

        assert mockResponse != null;
        when(taskService.insertTask(eq(input))).thenReturn(Mono.just(mockResponse));

        // Act
        Mono<ServerResponse> response = taskController.createTask(input);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }



    @Test
    @DisplayName("getAllTasks_shouldReturnOk_withValidFilter")
    void getAllTasksShouldReturnOkWithValidFilter() {
        // Arrange
        PagedResponseDTO<TaskDetailDTO> pagedResponse = TaskUtils.PAGED_RESPONSE;
        String title = "Test Title";

        when(taskService.findPaginate(any(), eq(title), any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(pagedResponse));

        // Act
        Mono<ServerResponse> response = taskController.getAllTasks(
                null, title, null, 0, null, 0, 10);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    @DisplayName("getAllTasks_shouldReturnNoContent_whenEmpty")
    void getAllTasksShouldReturnNoContentWhenEmpty() {
        // Arrange
        when(taskService.findPaginate(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = taskController.getAllTasks(
                null, null, null, 0, null, 0, 10);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();
    }

    @Test
    @DisplayName("getTaskById_shouldReturnOk_withValidId")
    void getTaskByIdShouldReturnOkWithValidId() {
        // Arrange
        String validId = "123";
        TaskDetailDTO task = new TaskDetailDTO(validId, "Teste", "Descrição", 1, TaskState.INSERT);

        when(taskService.getTaskById(validId)).thenReturn(Mono.just(task));

        // Act
        Mono<ServerResponse> response = taskController.getTaskById(validId);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    @DisplayName("getTaskById_shouldReturnNoContent_whenNotFound")
    void getTaskByIdShouldReturnNoContentWhenNotFound() {
        // Arrange
        String id = "nonexistent";
        when(taskService.getTaskById(id)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = taskController.getTaskById(id);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteTask_shouldReturnNoContent_withValidId")
    void deleteTaskShouldReturnNoContentWithValidId() {
        // Arrange
        String validId = UUID.randomUUID().toString();

        when(taskService.deleteById(validId))
                .thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = taskController.deleteTask(validId);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();

        verify(taskService).deleteById(validId);
    }

    @Test
    @DisplayName("deleteTask_shouldReturnBadRequest_withBlankId")
    void deleteTaskShouldReturnBadRequestWithBlankId() {
        // Arrange
        String blankId = "  ";

        // Act
        Mono<ServerResponse> response = taskController.deleteTask(blankId);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(throwable ->
                        throwable instanceof BadRequestException &&
                                throwable.getMessage().contains("obrigatorio"))
                .verify();
    }

    @Test
    @DisplayName("deleteTask_shouldReturnBadRequest_withInvalidId")
    void deleteTaskShouldReturnBadRequestWithInvalidId() {
        // Arrange
        String invalidId = "invalid-id";
        String expectedErrorMessage = "ID inválido";

        when(taskService.deleteById(invalidId))
                .thenReturn(Mono.error(new IllegalArgumentException(expectedErrorMessage)));

        // Act
        Mono<ServerResponse> response = taskController.deleteTask(invalidId);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(taskService).deleteById(invalidId);
    }

    @Test
    @DisplayName("createTask_shouldReturnNoContent_whenEmpty")
    void createTaskShouldReturnNoContentWhenEmpty() {
        // Arrange
        TaskSaveDTO task = new TaskSaveDTO("Teste", "Descrição", 1);

        when(taskService.insertTask(task)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = taskController.createTask(task);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();
    }
}
