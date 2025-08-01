package com.gabryel.task.controller;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Mono<PagedResponseDTO<TaskDetailDTO>> getAllTasks(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "0") Integer priority,
            @RequestParam(required = false) TaskState state,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        LOGGER.debug("Request getAllTasks(): id={}, title={}, description={}, priority={}, state={}, page={}, size={}",
                id, title, description, priority, state, page, size);

        return taskService
                .findPaginate(id, title, description, priority, state, page, size)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    @GetMapping("/{id}")
    public Mono<TaskDetailDTO> getTaskById(@PathVariable("id") String id) {
        LOGGER.debug("Request getTaskById(): id={}", id);

        return taskService
                .getTaskById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskDetailDTO> createTask(@RequestBody @Valid TaskSaveDTO task) {
        LOGGER.debug("Request createTask(): {}", task);

        return taskService.insertTask(task);
    }

    @PutMapping("/{id}")
    public Mono<TaskDetailDTO> updateTask(@RequestBody @Valid TaskSaveDTO task, @PathVariable String id) {
        LOGGER.debug("Request updateTask(): {}", task);

        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@PathVariable("id") String id) {
        LOGGER.debug("Request deleteTask(): id={}", id);

        if (StringUtils.isAllBlank(id)) {
            return Mono.error(new BadRequestException("`id` é obrigatório"));
        }

        return taskService.deleteById(id)
                .onErrorMap(t -> new ResponseStatusException(HttpStatus.BAD_REQUEST, t.getMessage()));
    }

    @PostMapping("/start")
    public Mono<TaskDetailDTO> startTask(@RequestParam String id, @RequestParam String zipCode) {

        return taskService.start(id, zipCode);
    }

    @PutMapping("/done")
    public Flux<TaskDetailDTO> doneTasks(@RequestBody List<String> ids) {

        return taskService.doneTaskByIdList(ids);
    }

}
