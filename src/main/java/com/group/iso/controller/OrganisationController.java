package com.group.iso.controller;

import com.group.iso.model.Organisation;
import com.group.iso.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationRepository organisationRepository;

    // GET /api/organisations?page=0&size=10&sort=name,asc
    @GetMapping
    public Page<Organisation> getAllOrganisations(Pageable pageable) {
        return organisationRepository.findAll(pageable);
    }

    // GET /api/organisations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable Long id) {
        return organisationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/organisations
    @PostMapping
    public ResponseEntity<Organisation> createOrganisation(@RequestBody Organisation organisation) {
        Organisation saved = organisationRepository.save(organisation);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/organisations/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable Long id, @RequestBody Organisation updatedOrg) {
        Optional<Organisation> existing = organisationRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Organisation org = existing.get();
        org.setName(updatedOrg.getName());
        org.setAddress(updatedOrg.getAddress());
        org.setEmail(updatedOrg.getEmail());
        org.setPhone(updatedOrg.getPhone());

        Organisation saved = organisationRepository.save(org);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/organisations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        if (!organisationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organisationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
