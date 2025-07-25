package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.repository.TaskRepository;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
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

//    @Test
//    void testInsertTask_mustReturnTaskDetailDTO_whenSuccess() {
//        when(converter.toEntity(any())).thenReturn(TaskUtils.TASK_ENTITY);
//        when(converter.toDetail(any())).thenReturn(TaskUtils.TASK_DETAIL);
//        when(repository.save(any())).thenReturn(Mono.just(TaskUtils.TASK_ENTITY));
//
//        Mono<ServerResponse> response = taskService.insertTask(TASK_ENTITY);
//
//        StepVerifier.create(response)
//                .expectNextMatches(serverResponse ->
//                        serverResponse.statusCode().equals(HttpStatus.OK))
//                .verifyComplete();
//
//    }

    @Test
    void testFindPaginate_shouldReturnPagedResponseDTO_whenCalled() {
        // O mock do repositório deve retornar um Flux, não um Mono<Page>
        var taskEntity = TaskUtils.TASK_ENTITY;
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
        var result = taskService.findPaginate(
                "id", "title", "desc", 1, TaskState.INSERT, 0, 10);

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