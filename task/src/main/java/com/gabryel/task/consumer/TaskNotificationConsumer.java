package com.gabryel.task.consumer;

import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TaskNotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskNotificationConsumer.class);


    private final TaskService taskService;

    public TaskNotificationConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @KafkaListener(topics = "${kafka.task.notification-output}", groupId = "${kafka.task.notification-group-id}")
    public void receiveAndFinishTask(TaskDetailDTO task) {
        Mono.just(task)
                .doOnNext(it -> LOGGER.info("Task notification received for finish: {}", task.getId()))
                .flatMap(taskService::doneTask)
                .subscribe();
    }

}
