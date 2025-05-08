package com.gabryel.task.repository;

import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.repository.custom.TaskCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends TaskCustomRepository, MongoRepository<TaskEntity, String> {
}
