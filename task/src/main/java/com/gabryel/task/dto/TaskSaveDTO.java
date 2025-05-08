package com.gabryel.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class TaskSaveDTO {

    private String title;
    private String description;
    private int priority;

    public TaskSaveDTO() {
    }

    public TaskSaveDTO(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
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

}
