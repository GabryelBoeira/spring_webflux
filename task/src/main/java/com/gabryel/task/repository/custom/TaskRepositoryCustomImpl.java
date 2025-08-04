package com.gabryel.task.repository.custom;

import com.gabryel.task.dto.TaskFindDTO;
import com.gabryel.task.entity.TaskEntity;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;
    private final ReactiveMongoOperations mongoOperations;

    public TaskRepositoryCustomImpl(ReactiveMongoTemplate mongoTemplate, ReactiveMongoOperations mongoOperations) {
        this.mongoTemplate = mongoTemplate;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Flux<TaskEntity> findPageableByFilters(TaskFindDTO filters, Pageable pageable) {
        Query query = new Query().with(pageable);
        query.addCriteria(buildCriteria(filters));

        return mongoTemplate
                .find(query, TaskEntity.class)
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    @Override
    public Mono<Long> countByPageableByFilters(TaskFindDTO filters) {

        return mongoTemplate.count(new Query(buildCriteria(filters)), TaskEntity.class);
    }

    @Override
    public Mono<Page<TaskEntity>> findPaginated(TaskFindDTO filters, int page, int size) {
        return Mono.just(filters)
                .zipWith(pageable(page, size))
                .flatMap(tuple -> execute(tuple.getT1(), tuple.getT2()));
    }

    private Mono<Page<TaskEntity>> execute(TaskFindDTO filters, Pageable pageable) {
        Query query = new Query().with(pageable);
        query.addCriteria(buildCriteria(filters));

        return mongoOperations.find(query, TaskEntity.class)
                .collectList()
                .flatMap(tasks -> paginate(tasks, pageable, filters));
    }

    private Mono<Page<TaskEntity>> paginate(List<TaskEntity> tasks, Pageable pageable, TaskFindDTO filters) {
        return mongoOperations.count(new Query(buildCriteria(filters)), TaskEntity.class)
                .map(count -> PageableExecutionUtils.getPage(tasks, pageable, () -> count));
    }

    private Mono<Pageable> pageable(int page, int size) {
        return Mono.just(PageRequest.of(page, size, Sort.by("title").ascending()));
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
