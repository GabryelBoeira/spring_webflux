package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    public TaskRepositoryCustomImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<TaskEntity> findPageableByFilters(TaskFindDTO filters, Pageable pageable) {
        Query query = new Query().with(pageable);
        query.addCriteria(buildCriteria(filters));

        return mongoTemplate.find(query, TaskEntity.class)
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    @Override
    public Mono<Long> countByPageableByFilters(TaskFindDTO filters) {

        return mongoTemplate.count(new Query(buildCriteria(filters)), TaskEntity.class);
    }

    private Criteria buildCriteria(TaskFindDTO find) {
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
        return criteria;
    }

}
