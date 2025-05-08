package com.gabryel.task.controller;

import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.service.TaskService;
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
    public ResponseEntity<Mono<List<TaskDetailDTO>>> getAllTasks() {
        return ResponseEntity.ok(taskService.listTasks());
    }

    @PostMapping
    public ResponseEntity<Mono<TaskDetailDTO>> createTask(@RequestBody TaskSaveDTO task) {
        return ResponseEntity.ok(taskService.insertTask(task));
    }

}
