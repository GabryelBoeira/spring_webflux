package com.gabryel.task.aspect;

import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.repository.TaskRepository;
import com.gabryel.task.service.TaskService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Aspect
@Configuration
public class TaskAspect {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${spring.application.name}")
    private String project;

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskAspect(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    // Pointcut (método a ser observado)
    @Pointcut("execution(* com.gabryel.task.service.TaskService.*(..))")
    public void newTaskServiceExecution() {
    }

    @Before(value = "newTaskServiceExecution()")
    public void beforeNewTask(final JoinPoint joinPoint) {
        CompletableFuture.runAsync(() -> {
            LOGGER.debug(":: AccountAspect.beforeNewTask: " + joinPoint);
        });
    }

    /// After NewAccount (execução após passar pelo método)
    @AfterReturning(value = "newTaskServiceExecution()", returning = "response")
    public void afterNewTask(final JoinPoint joinPoint, final Mono<TaskDetailDTO> response) {
        TaskDetailDTO newAccountResponse = response.share().block();
        CompletableFuture.runAsync(() -> {
            LOGGER.debug(":: AccountAspect.afterNewTask: " + newAccountResponse.toString());
        });
    }

}
