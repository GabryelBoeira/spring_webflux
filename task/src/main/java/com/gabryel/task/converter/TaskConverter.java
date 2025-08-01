package com.gabryel.task.converter;

import com.gabryel.task.dto.PagedResponseDTO;
import com.gabryel.task.dto.TaskDetailDTO;
import com.gabryel.task.dto.TaskSaveDTO;
import com.gabryel.task.entity.TaskEntity;
import com.gabryel.task.enums.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TaskConverter {

    private final AddressConverter addressConverter;

    @Autowired
    public TaskConverter(AddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }

    public TaskEntity toEntity(TaskSaveDTO saveDto) {
        return Optional.ofNullable(saveDto)
                .map(it -> TaskEntity.builder()
                        .title(it.getTitle())
                        .description(it.getDescription())
                        .priority(it.getPriority())
                        .state(TaskState.INSERT)
                        .build())
                .orElse(null);
    }

    public List<TaskEntity> toEntityList(List<TaskSaveDTO> saveDtos) {
        return Optional.ofNullable(saveDtos)
                .map(it -> it.stream().map(this::toEntity).toList())
                .orElse(new ArrayList<>());
    }

    public TaskDetailDTO toDetail(TaskEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    TaskDetailDTO dto = new TaskDetailDTO();
                    dto.setId(it.getId());
                    dto.setTitle(it.getTitle());
                    dto.setDescription(it.getDescription());
                    dto.setPriority(it.getPriority());
                    dto.setState(it.getState());
                    dto.setAddress(addressConverter.toDto(it.getAddress()));
                    dto.setCreatedAt(it.getCreatedAt());
                    return dto;
                }).orElse(null);
    }

    public List<TaskDetailDTO> toDetailList(List<TaskEntity> entities) {
        return Optional.ofNullable(entities)
                .map(it -> it.stream().map(this::toDetail).toList())
                .orElse(new ArrayList<>());
    }

    public PagedResponseDTO<TaskDetailDTO> pagedResponseDTO(Page<TaskEntity> page) {
        return new PagedResponseDTO<>(toDetailList(page.getContent()), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
    }

    public TaskEntity toEntityDetail(TaskDetailDTO detail) {
        return Optional.ofNullable(detail)
                .map(it -> TaskEntity.builder()
                        .id(it.getId())
                        .title(it.getTitle())
                        .description(it.getDescription())
                        .priority(it.getPriority())
                        .state(TaskState.INSERT)
                        .address(addressConverter.toEntity(it.getAddress()))
                        .createdAt(it.getCreatedAt())
                        .build()).orElse(null);
    }
}
