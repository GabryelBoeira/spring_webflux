package com.gabryel.task.aspect;

import com.gabryel.task.dto.TaskDetailDTO;
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
public class TaskServiceAspect {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${spring.application.name}")
    private String project;

    private final TaskService taskService;

    @Autowired
    public TaskServiceAspect(TaskService taskService) {
        this.taskService = taskService;
    }

    // Pointcut (define o que sera observado da classe ao method)
    @Pointcut("execution(* com.gabryel.task.service.TaskService.*(..))")
    public void newTaskServiceExecution() {
    }

    @Before(value = "newTaskServiceExecution()")
    public void beforeNewTask(final JoinPoint joinPoint) {
        LOGGER.info("Iniciou a execucao do metodo", joinPoint.getSignature().getName());
    }

    /// After NewAccount (execução após passar pelo method)
    @AfterReturning(value = "newTaskServiceExecution()")
    public void afterAllTask(final JoinPoint joinPoint) {
        LOGGER.info("Finalizou a execucao do metodo", joinPoint.getSignature().getName());
    }

}
