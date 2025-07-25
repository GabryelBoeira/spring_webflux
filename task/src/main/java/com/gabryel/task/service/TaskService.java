package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
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

    private final TaskConverter converter;
    private final TaskRepository repository;

    public TaskService(TaskConverter converter, TaskRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public Mono<PagedResponseDTO<TaskDetailDTO>> findPaginate(String id, String title, String description, Integer priority, TaskState state, Integer page, Integer size) {
        var filters = TaskFindDTO.builder().id(id).title(title).description(description).priority(priority).state(state).build();
        var pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        Mono<List<TaskDetailDTO>> detailDTOsMono = repository.findPageableByFilters(filters, pageable)
                .map(converter::toDetail)
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
                .map(converter::toEntity)
                .flatMap(this::save)
                .map(converter::toDetail)
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
                .map(converter::toDetail)
                .doOnError(t -> LOGGER.error("Erro ao buscar tarefa {} -> : {}", id, t.getMessage()));
    }

    public Mono<TaskDetailDTO> updateTask(@Valid TaskSaveDTO task) {
        return Mono
                .just(task)
                .map(converter::toEntity)
                .flatMap(this::save)
                .map(converter::toDetail)
                .doOnError(t -> LOGGER.error("Erro ao atualizar tarefa {} -> : {}", task, t.getMessage()));
    }
}
