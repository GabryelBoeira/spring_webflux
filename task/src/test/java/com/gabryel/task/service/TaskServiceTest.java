package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.repository.TaskRepository;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.gabryel.task.util.TaskUtils.TASK_ENTITY;
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
        // Arrange
        TaskSaveDTO taskSaveDTO = TaskUtils.TASK_SAVE_DTO; // Você precisa ter esta constante no TaskUtils
        when(converter.toEntity(any())).thenReturn(TASK_ENTITY);
        when(repository.save(any())).thenReturn(Mono.just(TASK_ENTITY));
        when(converter.toDetail(any())).thenReturn(TaskUtils.TASK_DETAIL);

        // Act
        Mono<ServerResponse> response = taskService.insertTask(taskSaveDTO);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        // Verificações adicionais (opcional)
        verify(converter).toEntity(any(TaskSaveDTO.class));
        verify(repository).save(any(TaskEntity.class));
        verify(converter).toDetail(any(TaskEntity.class));
    }


    @Test
    void testFindPaginate_shouldReturnPagedResponseDTO_whenCalled() {
        // O mock do repositório deve retornar um Flux, não um Mono<Page>
        var taskEntity = TASK_ENTITY;
        var taskEntityFlux = Flux.just(taskEntity);

        // Mockar a contagem para PagedResponseDTO
        Mono<Long> countMono = Mono.just(1L);

        // Configurar o comportamento do repositório
        when(repository.findPageableByFilters(any(TaskFindDTO.class), any(Pageable.class)))
                .thenReturn(taskEntityFlux);
        when(repository.countByPageableByFilters(any(TaskFindDTO.class)))
                .thenReturn(countMono);

        // Configurar o comportamento do converter
        when(converter.toDetail(any())).thenReturn(TaskUtils.TASK_DETAIL);

        // Executar o method a ser testado
        var result = taskService.findPaginate("id", "title", "desc", 1, TaskState.INSERT, 0, 10);

        // Verificar se os métodos foram chamados com os parâmetros corretos
        verify(repository, times(1)).findPageableByFilters(any(TaskFindDTO.class), any(Pageable.class));
        verify(repository, times(1)).countByPageableByFilters(any(TaskFindDTO.class));

        // Verificar o resultado usando StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.pageNumber() == 0 &&
                                response.pageSize() == 10 &&
                                response.totalElements() == 1L)
                .verifyComplete();
    }

    @Test
    void testDeleteById_shouldCallRepositoryDeleteById_andReturnVoidMono() {
        String id = "task-id";

        // Corrigir o mock para retornar um Mono vazio
        when(repository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(taskService.deleteById(id))
                .expectComplete()
                .verify();

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteById_shouldPropagateException_whenDeleteFails() {
        String id = "fail-id";
        when(repository.deleteById(id)).thenReturn(Mono.error(new RuntimeException("Error"))); // Simula erro reativo

        StepVerifier.create(taskService.deleteById(id))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error"))
                .verify();

        verify(repository).deleteById(id);
    }

}