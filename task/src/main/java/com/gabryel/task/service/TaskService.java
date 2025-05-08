package com.gabryel.task.service;

import com.gabryel.task.converter.TaskConverter;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    public static List<TaskEntity> list = new ArrayList<>();
    private final TaskConverter converter;

    public TaskService(TaskConverter converter) {
        this.converter = converter;
    }

    public Mono<TaskDetailDTO> insertTask(TaskSaveDTO task) {
        return Mono
                .just(task)
                .map(converter::toEntity)
                .flatMap(this::save)
                .map(converter::toDetail);
    }

    public Mono<List<TaskDetailDTO>> listTasks() {
        return Mono.just(list).map(converter::toDetailList);
    }

    private Mono<TaskEntity> save(TaskEntity task) {
       return Mono.just(task).map(TaskEntity::insert);
    }

}
