package com.gabryel.task.entity;

import com.gabryel.task.enums.TaskState;
import com.gabryel.task.service.TaskService;

public class TaskEntity {

    private String title;

    private String description;

    private int priority;

    private TaskState state;

    public TaskEntity() {
    }

    public TaskEntity(String title, String description, int priority, TaskState state) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = state;
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


    public TaskEntity insert() {
        this.state = TaskState.INSERT;
        TaskService.list.add(this);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String description;
        private int priority;
        private TaskState state;

        private Builder() {
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

        public TaskEntity build() {
            return new TaskEntity(title, description, priority, state);
        }
    }
}
