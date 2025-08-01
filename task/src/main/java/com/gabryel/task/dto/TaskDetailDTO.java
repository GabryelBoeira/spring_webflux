package com.gabryel.task.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import com.gabryel.task.enums.TaskState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Objeto contendo os dados de uma tarefa na API.")
public class TaskDetailDTO {

    @Schema(description = "Identificador da tarefa")
    private String id;

    @Schema(description = "Titulo da tarefa")
    private String title;

    @Schema(description = "Descricao da tarefa")
    private String description;

    @Schema(description = "Prioridade da tarefa. Valores entre 1 e 5.")
    private int priority;

    @Schema(description = "Estado da tarefa.")
    private TaskState state;

    @Schema(description = "Caso a tarefa tenha inciado o endereco")
    private AddressDTO address;

    @Schema(description = "Data de Criacao")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TaskDetailDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", state=" + state +
                ", addressZipCode=" + address.getZipCode() +
                ", createdAt=" + createdAt +
                '}';
    }
    
}
