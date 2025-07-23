package com.gabryel.task.repository;

import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.repository.custom.TaskRepositoryCustom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends  ReactiveMongoRepository<TaskEntity, String>, TaskRepositoryCustom {
}
