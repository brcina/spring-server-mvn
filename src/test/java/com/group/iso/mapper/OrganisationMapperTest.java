package com.group.iso.mapper;

import com.group.iso.dto.OrganisationDto;
import com.group.iso.model.Organisation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationMapperTest {

    @Test
    void toDto() {
        // given
        var entity = Organisation.builder()
                .id(Long.valueOf(1L))
                .name("aName")
                .phone("1234567")
                .address("Street 123, Berlin")
                .email("info@test.de").build();
        // execute
        var dto = OrganisationMapper.toDto(entity);
        // verify
        assertEquals(1L, dto.getId());
        assertEquals("aName", entity.getName());
        assertEquals("1234567", entity.getPhone());
        assertEquals("Street 123, Berlin", entity.getAddress());
        assertEquals("info@test.de", entity.getEmail());
    }

    @Test
    void toEntity() {
        // given
        var dto = OrganisationDto.builder()
                .id(Long.valueOf(1L))
                .name("aName")
                .phone("1234567")
                .address("Street 123, Berlin")
                .email("info@test.de").build();
        // execute
        var entity = OrganisationMapper.toEntity(dto);
        // verify
        assertEquals(1L, entity.getId());
        assertEquals("aName", entity.getName());
        assertEquals("1234567", entity.getPhone());
        assertEquals("Street 123, Berlin", entity.getAddress());
        assertEquals("info@test.de", entity.getEmail());
    }
}