package com.group.iso.controller;

import com.group.iso.dto.OrganisationDto;
import com.group.iso.dto.PagedResponse;
import com.group.iso.mapper.OrganisationMapper;
import com.group.iso.model.Organisation;
import com.group.iso.repository.OrganisationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
@Tag(name = "Organisation", description = "API for managing organisations")
public class OrganisationController {

    private final OrganisationRepository repository;

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

        var pageResult = repository.findAll(pageable)
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

        return repository.findById(id)
                .map(OrganisationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new organisation", description = "Creates a new organisation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organisation created successfully"),
            @ApiResponse(responseCode = "409", description = "Organisation with this name already exists")
    })
    public ResponseEntity<OrganisationDto> createOrganisation(
            @Valid @RequestBody OrganisationDto dto) {
        if(repository.existsByName(dto.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Organisation entity = OrganisationMapper.toEntity(dto);
        Organisation saved = repository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrganisationMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organisation", description = "Updates organisation data by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organisation updated successfully"),
            @ApiResponse(responseCode = "409", description = "Another Organisation with that name exists"),
            @ApiResponse(responseCode = "404", description = "Organisation with this id cannot be found")
    })
    public ResponseEntity<OrganisationDto> updateOrganisation(
            @Parameter(description = "ID of the organisation", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody OrganisationDto dto) {

        Optional<Organisation> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Organisation> existingWithName = repository.findByName(dto.getName());
        if (existingWithName.isPresent() && !existingWithName.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Organisation org = existing.get();
        // Update fields from DTO
        org.setName(dto.getName());
        org.setAddress(dto.getAddress());
        org.setEmail(dto.getEmail());
        org.setPhone(dto.getPhone());

        Organisation saved = repository.save(org);
        return ResponseEntity.ok(OrganisationMapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organisation", description = "Deletes the organisation with the given ID")
    public ResponseEntity<Void> deleteOrganisation(
            @Parameter(description = "ID of the organisation", example = "1")
            @PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
