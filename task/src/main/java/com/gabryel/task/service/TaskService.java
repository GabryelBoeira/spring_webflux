package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TaskService {

    private final TaskConverter converter;
    private final TaskRepository repository;

    public TaskService(TaskConverter converter, TaskRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public Mono<TaskDetailDTO> insertTask(TaskSaveDTO task) {
        return Mono
                 .just(task)
                .map(converter::toEntity)
                .flatMap(this::save)
                .map(converter::toDetail);
    }

    public Mono<List<TaskDetailDTO>> listTasks() {
        return Mono.just(repository.findAll())
                .map(converter::toDetailList);
    }

    private Mono<TaskEntity> save(TaskEntity task) {
        return Mono.just(task)
                .map(repository::save);
    }

}
