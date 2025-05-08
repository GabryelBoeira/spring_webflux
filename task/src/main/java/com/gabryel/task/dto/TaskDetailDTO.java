package com.gabryel.task.dto;

import com.gabryel.task.enums.TaskState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class TaskDetailDTO {

    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;

    public TaskDetailDTO() {
    }

    public TaskDetailDTO(String title, String description, int priority, TaskState state) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TaskDetailDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", state=" + state +
                '}';
    }
    
}
