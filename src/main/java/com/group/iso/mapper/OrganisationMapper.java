package com.group.iso.mapper;

import com.group.iso.dto.OrganisationDto;
import com.group.iso.model.Organisation;

public class OrganisationMapper {
    public static OrganisationDto toDto(Organisation entity) {
        if (entity == null) return null;

        return OrganisationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }

    public static Organisation toEntity(OrganisationDto dto) {
        if (dto == null) return null;

        return Organisation.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();

    }
}
