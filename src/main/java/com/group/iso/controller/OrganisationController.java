package com.group.iso.controller;

import com.group.iso.dto.OrganisationDto;
import com.group.iso.dto.PagedResponse;
import com.group.iso.mapper.OrganisationMapper;
import com.group.iso.model.Organisation;
import com.group.iso.repository.OrganisationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
@Tag(name = "Organisation", description = "API for managing organisations")
public class OrganisationController {

    private final OrganisationRepository organisationRepository;

    @GetMapping
    @Operation(summary = "Get all organisations", description = "Returns a paginated list of all organisations")
    public PagedResponse<OrganisationDto> getAllOrganisations(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-based)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") int size,
            @RequestParam(defaultValue = "name,asc") @Parameter(description = "Sorting field and direction, e.g. name,asc or email,desc") String sort) {

        Sort sortOrder = Sort.by(sort.split(",")[0]);
        if (sort.toLowerCase().endsWith(",desc")) {
            sortOrder = sortOrder.descending();
        }
        var pageable = PageRequest.of(page, size, sortOrder);

        var pageResult = organisationRepository.findAll(pageable)
                .map(OrganisationMapper::toDto);

        return PagedResponse.<OrganisationDto>builder()
                .content(pageResult.getContent())
                .pageNumber(pageResult.getNumber())
                .pageSize(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organisation by ID", description = "Returns a single organisation by its ID")
    public ResponseEntity<OrganisationDto> getOrganisationById(
            @Parameter(description = "ID of the organisation", example = "1")
            @PathVariable Long id) {

        return organisationRepository.findById(id)
                .map(OrganisationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new organisation", description = "Creates a new organisation")
    public ResponseEntity<OrganisationDto> createOrganisation(
            @Valid @RequestBody OrganisationDto organisationDto) {

        Organisation entity = OrganisationMapper.toEntity(organisationDto);
        Organisation saved = organisationRepository.save(entity);
        return ResponseEntity.ok(OrganisationMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organisation", description = "Updates organisation data by ID")
    public ResponseEntity<OrganisationDto> updateOrganisation(
            @Parameter(description = "ID of the organisation", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody OrganisationDto updatedDto) {

        Optional<Organisation> existing = organisationRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Organisation org = existing.get();
        // Update fields from DTO
        org.setName(updatedDto.getName());
        org.setAddress(updatedDto.getAddress());
        org.setEmail(updatedDto.getEmail());
        org.setPhone(updatedDto.getPhone());

        Organisation saved = organisationRepository.save(org);
        return ResponseEntity.ok(OrganisationMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organisation", description = "Deletes the organisation with the given ID")
    public ResponseEntity<Void> deleteOrganisation(
            @Parameter(description = "ID of the organisation", example = "1")
            @PathVariable Long id) {

        if (!organisationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organisationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
