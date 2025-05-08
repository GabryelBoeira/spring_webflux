package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class TaskCustomRepositoryImpl implements TaskCustomRepository{

    private final MongoOperations mongo;
    private final MongoOperations mongoOperations;

    public TaskCustomRepositoryImpl(MongoOperations mongo, MongoOperations mongoOperations) {
        this.mongo = mongo;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Page<TaskEntity> findPageableByFilters(TaskFindDTO find, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Query query = new Query().with(pageable);
        Criteria criteria = new Criteria();

        if (StringUtils.hasText(find.getTitle())) {
            criteria.and("title").regex(find.getTitle(), "i");
        }
        if (StringUtils.hasText(find.getDescription())) {
            criteria.and("description").regex(find.getDescription(), "i");
        }
        if (find.getPriority() > 0) {
            criteria.and("priority").is(find.getPriority());
        }
        if (find.getState() != null) {
            criteria.and("state").is(find.getState());
        }

        query.addCriteria(criteria);

        return PageableExecutionUtils.getPage(mongoOperations.find(query, TaskEntity.class), pageable, () -> mongoOperations.count(query, TaskEntity.class));
    }
}
