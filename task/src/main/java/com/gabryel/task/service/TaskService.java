package com.gabryel.task.service;

import com.gabryel.task.converter.AddressConverter;
import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.*;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.exception.TaskNotFoundException;
import com.gabryel.task.repository.TaskRepository;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskConverter taskConverter;
    private final AddressConverter addressConverter;
    private final TaskRepository repository;
    private final AddressService addressService;

    public TaskService(TaskConverter taskConverter, AddressConverter addressConverter, TaskRepository repository, AddressService addressService) {
        this.taskConverter = taskConverter;
        this.addressConverter = addressConverter;
        this.repository = repository;
        this.addressService = addressService;
    }

    public Mono<PagedResponseDTO<TaskDetailDTO>> findPaginate(String id, String title, String description, Integer priority, TaskState state, Integer page, Integer size) {
        var filters = TaskFindDTO.builder().id(id).title(title).description(description).priority(priority).state(state).build();
        var pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        Mono<List<TaskDetailDTO>> detailDTOsMono = repository.findPageableByFilters(filters, pageable)
                .map(taskConverter::toDetail)
                .collectList();

        Mono<Long> totalCountMono = repository.countByPageableByFilters(filters);

        // Combina os dois Mono/Flux para construir um Mono<Page>
        return Mono.zip(detailDTOsMono, totalCountMono)
                .map(tuple -> {
                    List<TaskDetailDTO> content = tuple.getT1();
                    long totalElements = tuple.getT2();

                    // Calcula o total de páginas. Se não houver elementos, considera 1 página.
                    int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
                    if (totalElements == 0) {
                        totalPages = 1; // Para que isLast seja true na primeira (e única) página vazia
                    }

                    boolean isFirst = pageable.getPageNumber() == 0;
                    boolean isLast = pageable.getPageNumber() == totalPages - 1;

                    return new PagedResponseDTO<>(
                            content,
                            pageable.getPageNumber(),    // pageNumber
                            pageable.getPageSize(),      // pageSize
                            totalElements,               // totalElements
                            totalPages,                  // totalPages
                            isFirst,                     // isFirst
                            isLast                       // isLast
                    );
                })
                .doOnError(t -> LOGGER.error("Erro ao buscar tarefas -> : {}", t.getMessage()))
                .onErrorResume(t -> Mono.error(new BadRequestException("Erro ao buscar tarefas -> : " + t.getMessage())));
    }

    public Mono<TaskDetailDTO> insertTask(TaskSaveDTO task) {
        return Mono
                .just(task)
                .map(taskConverter::toEntity)
                .flatMap(this::save)
                .map(taskConverter::toDetail)
                .doOnError(t -> LOGGER.error("Erro ao inserir tarefa {} -> : {}", task, t.getMessage()));
    }

    public Mono<Void> deleteById(final String id) {
        return repository.deleteById(id);
    }

    private Mono<TaskEntity> save(TaskEntity task) {
        return Mono.just(task)
                .flatMap(repository::save);
    }

    public Mono<TaskDetailDTO> getTaskById(final String id) {

        return repository
                .findById(id)
                .map(taskConverter::toDetail)
                .doOnError(t -> LOGGER.error("Erro ao buscar tarefa {} -> : {}", id, t.getMessage()));
    }

    public Mono<TaskDetailDTO> updateTask(final String id, @Valid TaskSaveDTO task) {
        return repository
                .findById(id)
                .map(taskEntity -> taskEntity.toBuilder()
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .priority(task.getPriority())
                        .build())
                .flatMap(this::save)
                .map(taskConverter::toDetail)
                .doOnError(t -> LOGGER.error("Erro ao atualizar tarefa {} -> : {}", task, t.getMessage()));
    }

    public Mono<TaskEntity> updateAddress(TaskEntity task, AddressDTO address) {

        return Mono.just(address)
                .map(addressConverter::toEntity)
                .flatMap(addressEntity -> Mono.just(task.toBuilder().address(addressEntity).build()))
                .doOnError(t -> LOGGER.error("Erro ao atualizar endereço da tarefa {} -> : {}", task.getId(), t.getMessage()));
    }

    public Mono<TaskDetailDTO> start(final String taskId, final String zipCode) {
        return repository
                .findById(taskId)
                .zipWhen(it -> addressService.getAddressByCep(zipCode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(TaskEntity::start)
                .flatMap(this::save)
                .map(taskConverter::toDetail)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(t -> LOGGER.error("Erro ao iniciar tarefa ID: {}", taskId, t));
    }

}
