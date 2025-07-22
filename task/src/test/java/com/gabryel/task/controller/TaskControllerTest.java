package com.gabryel.task.controller;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.service.TaskService;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@WebFluxTest(TaskController.class)
public class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("controller_mustReturnOk_whenSaveSuccessFully")
    void getSaveSuccessFully() {
        TaskSaveDTO taskSaveDTO = TaskUtils.TASK_SAVED;
        TaskDetailDTO taskDetailDTO = TaskUtils.TASK_DETAIL;

        when(taskService.insertTask(any(TaskSaveDTO.class)))
                .thenReturn(Mono.just(taskDetailDTO));

        webTestClient.post()
                .uri("/task")
                .bodyValue(taskSaveDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDetailDTO.class)
                .isEqualTo(taskDetailDTO);
    }

    @Test
    @DisplayName("getAllTasks_shouldReturnOk_withValidFilter")
    void getAllTasksShouldReturnOkWithValidFilter() {
        PagedResponseDTO<TaskDetailDTO> pagedResponse = TaskUtils.PAGED_RESPONSE;

        when(taskService.findPaginate(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(pagedResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/task")
                        .queryParam("title", "Test Title")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagedResponseDTO.class)
                .isEqualTo(pagedResponse);
    }

    @Test
    @DisplayName("createTask_shouldReturnBadRequest_withInvalidData")
    void createTaskShouldReturnBadRequestWithInvalidData() {
        TaskSaveDTO invalidTaskDTO = new TaskSaveDTO();

        webTestClient.post()
                .uri("/task")
                .bodyValue(invalidTaskDTO)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @DisplayName("deleteTask_shouldReturnNoContent_withValidId")
    void deleteTaskShouldReturnNoContentWithValidId() {
        String validId = UUID.randomUUID().toString();

        when(taskService.deleteById(validId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/task/{id}", validId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    @DisplayName("deleteTask_shouldReturnBadRequest_withInvalidId")
    void deleteTaskShouldReturnBadRequestWithInvalidId() {
        String invalidId = "invalid-id";
        String expectedErrorMessage = "Simulated Error: Invalid ID provided"; // More specific error message

        when(taskService.deleteById(invalidId))
                .thenReturn(Mono.error(new IllegalArgumentException(expectedErrorMessage)));

        webTestClient.delete()
                .uri("/task/{id}","")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class) // Expecting a String body from the error message
                .isEqualTo(expectedErrorMessage); // Verify the exact message

        // Verify that the deleteById method was actually called with the invalidId
        verify(taskService).deleteById(invalidId);
    }
}