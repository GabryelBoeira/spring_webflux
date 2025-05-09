package com.gabryel.task.controller;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    @DisplayName("controller_mustReturnOk_whenSaveSuccessFully")
    void getSaveSuccessFully() {
        WebTestClient client = WebTestClient.bindToController(taskController).build();
        when(taskService.insertTask(any())).thenReturn(Mono.just(new TaskDetailDTO(UUID.randomUUID().toString(), "Teste", "Teste", 5, TaskState.INSERT)));

        var response = client.post().uri("/task")
                .bodyValue(new TaskSaveDTO("Teste", "Teste", 5))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TaskDetailDTO.class)
                .consumeWith(result -> {
                    TaskDetailDTO responseBody = result.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getTitle().equals("Teste");
                    assert responseBody.getDescription().equals("Teste");
                    assert responseBody.getPriority() == 5;
                    assert responseBody.getState() == TaskState.INSERT;
                });

    }

    @Test
    @DisplayName("getAllTasks_shouldReturnOk_withValidFilter")
    void getAllTasksShouldReturnOkWithValidFilter() {
        WebTestClient client = WebTestClient.bindToController(taskController).build();
        var pagedResponseMock = new PagedResponseDTO();
        // Mock success response
        when(taskService.findPaginate(any(), any(), any(), any(), any(), any(), any())).thenReturn(pagedResponseMock);

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/task")
                        .queryParam("title", "Test Title")
                        .queryParam("description", "Test Description")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PagedResponseDTO.class)
                .consumeWith(result -> {
                    assert result.getResponseBody() != null;
                });
    }

    @Test
    @DisplayName("createTask_shouldReturnBadRequest_withInvalidData")
    void createTaskShouldReturnBadRequestWithInvalidData() {
        WebTestClient client = WebTestClient.bindToController(taskController).build();

        client.post()
                .uri("/task")
                .bodyValue(new TaskSaveDTO("", null, -1))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @DisplayName("deleteTask_shouldReturnNoContent_withValidId")
    void deleteTaskShouldReturnNoContentWithValidId() {
        WebTestClient client = WebTestClient.bindToController(taskController).build();
        when(taskService.deleteById(any())).thenReturn(Mono.empty());

        client.delete()
                .uri(uriBuilder -> uriBuilder.path("/task/{id}").build("e2f5a3bc-8fd4-4ed8-b35c-5ab6efedc3d1"))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @DisplayName("deleteTask_shouldReturnBadRequest_withInvalidId")
    void deleteTaskShouldReturnBadRequestWithInvalidId() {
        WebTestClient client = WebTestClient.bindToController(taskController).build();

        client.delete()
                .uri(uriBuilder -> uriBuilder.path("/task/{id}").build(" "))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
