package com.gabryel.task.converter;

import com.gabryel.task.dto.AddressDTO;
import com.gabryel.task.entity.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressConverter {

    public AddressEntity toEntity(AddressDTO dto) {
        return Optional.ofNullable(dto)
                .map(it -> AddressEntity.builder()
                        .city(it.getCity())
                        .state(it.getState())
                        .complement(it.getComplement())
                        .zipCode(it.getZipCode())
                        .street(it.getStreet())
                        .neighborhood(it.getNeighborhood())
                        .build()
                )
                .orElse(null);
    }

    public AddressDTO toDto(AddressEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> AddressDTO.builder()
                        .city(it.getCity())
                        .state(it.getState())
                        .complement(it.getComplement())
                        .zipCode(it.getZipCode())
                        .street(it.getStreet())
                        .neighborhood(it.getNeighborhood())
                        .build()
                )
                .orElse(null);
    }

}
