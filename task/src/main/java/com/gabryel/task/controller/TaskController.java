package com.gabryel.task.controller;

import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Mono<ServerResponse> getAllTasks(@RequestParam(required = false) String id,
                                            @RequestParam(required = false) String title,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false, defaultValue = "0") Integer priority,
                                            @RequestParam(required = false) TaskState state,
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {

        LOGGER.debug("Request getAllTasks(): id={}, title={}, description={}, priority={}, state={}, page={}, size={}", id, title, description, priority, state, page, size);
        return taskService
                .findPaginate(id, title, description, priority, state, page, size)
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    @GetMapping("/{id}")
    public Mono<ServerResponse> getTaskById(@PathVariable("id") String id) {

        LOGGER.debug("Request getTaskById(): id={}", id);
        return taskService
                .getTaskById(id)
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    @PostMapping
    public Mono<ServerResponse> createTask(@RequestBody TaskSaveDTO task) {
        LOGGER.debug("Request createTask(): {}", task);
        return taskService.insertTask(task)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ServerResponse> deleteTask(@PathVariable("id") String id) {
        LOGGER.debug("Request deleteTask(): id={}", id);
        if (StringUtils.isAllBlank(id)) return Mono.error(new BadRequestException("`id` e obrigatorio"));

        return taskService
                .deleteById(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(t -> Mono.defer(() -> ServerResponse.badRequest().bodyValue(t.getMessage())));
    }


}
