package com.gabryel.task.dto;

import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public class TaskDetailDTO {

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

    public static TaskDetailDTO toDTO(TaskEntity task) {
        return new TaskDetailDTO(task.getTitle(), task.getDescription(), task.getPriority(), task.getState());
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

    public static List<TaskDetailDTO> toDTOList(List<TaskEntity> taskEntities) {
        return taskEntities.stream().map(TaskDetailDTO::toDTO).toList();
    }
}
