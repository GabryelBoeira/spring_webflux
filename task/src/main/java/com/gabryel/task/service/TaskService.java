package com.gabryel.task.service;

import com.gabryel.task.configuration.MessageConfiguration;
import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.repository.TaskRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final MessageConfiguration message;
    private final TaskConverter converter;
    private final TaskRepository repository;

    public TaskService(MessageConfiguration message, TaskConverter converter, TaskRepository repository) {
        this.message = message;
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

    public Mono<ServerResponse> insertTask(TaskSaveDTO task) {
        return Mono
                .just(task)
                .map(converter::toEntity)
                .flatMap(this::save)
                .doOnError(t -> LOGGER.error("Erro ao inserir tarefa {} -> : {}", task, t.getMessage()))
                .map(converter::toDetail)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .onErrorResume(t ->
                        ServerResponse.badRequest().bodyValue(
                                message.getMessage("teste", task.getTitle(), t.getMessage())
                        )
                );
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

}
