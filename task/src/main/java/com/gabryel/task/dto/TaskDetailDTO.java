package com.gabryel.task.dto;

import com.gabryel.task.enums.TaskState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto contendo os dados de uma tarefa na API.")
public class TaskDetailDTO {

    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;
    private AddressDTO address;

    public TaskDetailDTO() {
    }

    public TaskDetailDTO(String id, String title, String description, int priority, TaskState state) {
        this.id = id;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
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
