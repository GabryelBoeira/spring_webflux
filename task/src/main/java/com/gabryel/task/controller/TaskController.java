package com.gabryel.task.controller;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO> getAllTasks(@RequestParam(required = false) String id,
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
        var result = taskService.insertTask(task);
        if (result == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable("id") String id) {
        // Validação: ID não pode ser vazio
        if (id == null || id.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return taskService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }


}
