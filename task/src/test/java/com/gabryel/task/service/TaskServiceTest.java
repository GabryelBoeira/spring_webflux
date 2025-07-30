package com.gabryel.task.service;

import com.gabryel.task.converter.AddressConverter;
import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.producer.TaskNotificationProducer;
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

import static com.gabryel.task.util.TaskUtils.TASK_ENTITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskConverter converter;

    @Mock
    private AddressConverter addressConverter;

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskNotificationProducer taskProducer;

    @Test
    void testInsertTask_mustReturnTaskDetailDTO_whenSuccess() {
        // Arrange
        TaskSaveDTO saveDTO = TaskUtils.TASK_SAVE_DTO;
        TaskEntity entity = TaskUtils.TASK_ENTITY;
        TaskDetailDTO detailDTO = TaskUtils.TASK_DETAIL_DTO;

        when(converter.toEntity(any(TaskSaveDTO.class))).thenReturn(entity);
        when(repository.save(any(TaskEntity.class))).thenReturn(Mono.just(entity));
        when(converter.toDetail(any(TaskEntity.class))).thenReturn(detailDTO);
        when(taskProducer.sendNotification(any())).thenReturn(Mono.just(detailDTO));

        // Act & Assert
        StepVerifier.create(taskService.insertTask(saveDTO))
                .expectNext(detailDTO)
                .verifyComplete();

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
        when(converter.toDetail(any())).thenReturn(TaskUtils.TASK_DETAIL_DTO);

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
        // Arrange
        String id = "test-id";

        when(repository.deleteById(eq(id))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(taskService.deleteById(id))
                .verifyComplete();

        verify(repository).deleteById(eq(id));
    }

    @Test
    void testDeleteById_shouldPropagateException_whenDeleteFails() {
        // Arrange
        String id = "test-id";
        RuntimeException exception = new RuntimeException("Delete failed");

        when(repository.deleteById(eq(id))).thenReturn(Mono.error(exception));

        // Act & Assert
        StepVerifier.create(taskService.deleteById(id))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Delete failed"))
                .verify();

        verify(repository).deleteById(eq(id));
    }

    @Test
    void testGetTaskById_shouldReturnTaskDetailDTO_whenFound() {
        // Arrange
        String id = "test-id";
        TaskEntity entity = TaskUtils.TASK_ENTITY;
        TaskDetailDTO detailDTO = TaskUtils.TASK_DETAIL_DTO;

        when(repository.findById(eq(id))).thenReturn(Mono.just(entity));
        when(converter.toDetail(any(TaskEntity.class))).thenReturn(detailDTO);

        // Act & Assert
        StepVerifier.create(taskService.getTaskById(id))
                .expectNext(detailDTO)
                .verifyComplete();

        verify(repository).findById(eq(id));
        verify(converter).toDetail(any(TaskEntity.class));
    }

    @Test
    void testGetTaskById_shouldReturnEmptyMono_whenNotFound() {
        // Arrange
        String id = "test-id";

        when(repository.findById(eq(id))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(taskService.getTaskById(id))
                .verifyComplete();

        verify(repository).findById(eq(id));
        verify(converter, never()).toDetail(any(TaskEntity.class));
    }

}