package com.gabryel.task.controller;

import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TeskController {

    private final TaskService taskService;

    public TeskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskDetailDTO>> getAllTasks(@RequestParam(required = false) String id,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String description,
                                                        @RequestParam(required = false, defaultValue = "0") Integer priority,
                                                        @RequestParam(required = false) TaskState state,
                                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size
                                                                 ) {

        return ResponseEntity.ok(taskService.findPaginate(id, title, description, priority, state, page, size));
    }

    @PostMapping
    public ResponseEntity<Mono<TaskDetailDTO>> createTask(@RequestBody TaskSaveDTO task) {
        return ResponseEntity.ok(taskService.insertTask(task));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@RequestParam String id) {
        return Mono.just(id)
                .flatMap(taskService::deleteById);
    }

}
