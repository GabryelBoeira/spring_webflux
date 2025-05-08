package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import org.springframework.data.domain.Page;

public interface TaskCustomRepository {

    Page<TaskEntity> findPageableByFilters(TaskFindDTO find, Integer page, Integer size);
}
