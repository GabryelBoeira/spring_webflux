package com.gabryel.task.producer;

import com.gabryel.task.dto.TaskDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TaskNotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskNotificationProducer.class);

    @Value("${kafka.task.notification-output}")
    private String topic;

    private final KafkaTemplate<Object, Object> template;

    public TaskNotificationProducer(KafkaTemplate<Object, Object> template) {
        this.template = template;
    }

    public Mono<TaskDetailDTO> sendNotification(TaskDetailDTO task) {

        return Mono.just(task)
                .map(it -> this.template.send(topic, it))
                .map(it -> task)
                .doOnNext(it -> LOGGER.info("Task notification sent: {}", task.getId()));
    }

}
