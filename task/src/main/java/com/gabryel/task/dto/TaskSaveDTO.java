package com.gabryel.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto contendo os dados de uma nova tarefa a ser criada na API.")
public class TaskSaveDTO {

    @Schema(description = "Titulo da tarefa")
    @Size(min = 3, max = 40, message = "{size.title}")
    @NotBlank(message = "{blank.title}")
    private String title;

    @Schema(description = "Descricao da tarefa")
    @Size(min = 10, max = 80, message = "{size.description}")
    @NotBlank(message = "{blank.description}")
    private String description;

    @Schema(description = "Prioridade da tarefa. Valores entre 1 e 5.")
    @Size(min = 1, max = 5, message = "{size.priority}")
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
