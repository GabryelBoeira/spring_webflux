package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TaskService {

    private final TaskConverter converter;
    private final TaskRepository repository;

    public TaskService(TaskConverter converter, TaskRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public PagedResponseDTO<TaskDetailDTO> findPaginate(String id, String title, String description, Integer priority, TaskState state, Integer page, Integer size) {
        var find = TaskFindDTO.builder().id(id).title(title).description(description).priority(priority).state(state).build();
        var pageResult = repository.findPageableByFilters(find, page, size);
        return converter.pagedResponseDTO(pageResult);
    }

    public Mono<TaskDetailDTO> insertTask(TaskSaveDTO task) {
        return Mono
                .just(task)
                .map(converter::toEntity)
                .flatMap(this::save)
                .map(converter::toDetail);
    }

    /**
     * Remove uma tarefa pelo id de forma bloqueante.
     * Não use este método para grandes volumes em ambientes reativos.
     */
    public Mono<Void> deleteById(String id) {
        return Mono.fromRunnable(() -> deleteByIdSync(id));
    }

    private void deleteByIdSync(String id) {
        repository.deleteById(id);
    }

    private Mono<TaskEntity> save(TaskEntity task) {
        return Mono.just(task)
                .map(repository::save);
    }

}
