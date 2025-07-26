package com.gabryel.task.dto;

import com.gabryel.task.enums.TaskState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto contendo os dados de uma tarefa encontrada na API.")
public class TaskFindDTO extends TaskDetailDTO {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TaskFindDTO instance;

        private Builder() {
            instance = new TaskFindDTO();
        }

        public Builder id(String id) {
            instance.setId(id);
            return this;
        }

        public Builder title(String title) {
            instance.setTitle(title);
            return this;
        }

        public Builder description(String description) {
            instance.setDescription(description);
            return this;
        }

        public Builder priority(int priority) {
            instance.setPriority(priority);
            return this;
        }

        public Builder state(TaskState state) {
            instance.setState(state);
            return this;
        }

        public TaskFindDTO build() {
            return instance;
        }
    }
}
