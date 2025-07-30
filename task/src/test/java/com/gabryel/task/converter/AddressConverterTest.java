package com.gabryel.task.converter;

import com.gabryel.task.dto.AddressDTO;
import com.gabryel.task.entity.AddressEntity;
import com.gabryel.task.util.TaskUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AddressConverterTest {

    @InjectMocks
    private AddressConverter converter;

    @Test
    public void testToEntity_NullTaskSaveDTO() {

        AddressEntity result = converter.toEntity(null);
        assertNull(result);
    }

    @Test
    public void testToEntityList_EmptyList() {

        List<AddressEntity> result = converter.toEntityList(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToEntityList_NullList() {

        List<AddressEntity> result = converter.toEntityList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToDetailList_NullList() {

        List<AddressDTO> result = converter.toDtoList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testToDetail_ValidEntity() {

        AddressEntity entity = TaskUtils.ADDRESS_ENTITY;
        AddressDTO result = converter.toDto(entity);

        assertNotNull(result);
        
        assertEquals("address-city", result.getCity());
        assertEquals("address-state", result.getState());
        assertEquals("address-complement", result.getComplement());
        assertEquals("address-zip-code", result.getZipCode());
        assertEquals("address-street", result.getStreet());
        assertEquals("address-neighborhood", result.getNeighborhood());
    }

    @Test
    public void testToDetail_NullEntity() {

        AddressDTO result = converter.toDto(null);
        
        assertNull(result);
    }

}
