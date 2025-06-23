package com.group.iso.repository;

import com.group.iso.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {


    Optional<Organisation> findByName(String name);

    boolean existsByName(String name);


}
