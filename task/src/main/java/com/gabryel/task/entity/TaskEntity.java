package com.gabryel.task.entity;

import com.gabryel.task.enums.TaskState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TaskEntity {

    @Id
    private String id;

    private String title;

    private String description;

    private int priority;

    private TaskState state;

    private AddressEntity address;

    public TaskEntity() {
    }

    public TaskEntity(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.state = builder.state;
        this.address = builder.address;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public TaskState getState() {
        return state;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .priority(this.priority)
                .state(this.state)
                .address(this.address);
    }

    public TaskEntity start() {
        return toBuilder()
                .state(TaskState.DOING)
                .build();
    }

    public static class Builder {
        private String id;
        private String title;
        private String description;
        private int priority;
        private TaskState state;
        private AddressEntity address;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder state(TaskState state) {
            this.state = state;
            return this;
        }

        public Builder address(AddressEntity address) {
            this.address = address;
            return this;
        }

        public TaskEntity build() {
            return new TaskEntity(this);
        }
    }
}
