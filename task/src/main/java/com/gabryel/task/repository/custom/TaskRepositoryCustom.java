package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepositoryCustom {

    Flux<TaskEntity> findPageableByFilters(TaskFindDTO filters, Pageable pageable);

    Mono<Long> countByPageableByFilters(TaskFindDTO filters);

}
