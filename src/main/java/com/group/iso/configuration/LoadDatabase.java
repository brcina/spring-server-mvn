package com.group.iso.configuration;

import com.group.iso.model.Organisation;
import com.group.iso.repository.OrganisationRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class LoadDatabase {
    @Bean
    @Profile("dev")
    CommandLineRunner initDbDev(OrganisationRepository organisationRepository) {
        return args -> {
            if (organisationRepository.count() > 0) {
                log.info("Initialisierung übersprungen – Organisationen existieren bereits.");
                return;
            }

            Faker faker = new Faker();


            int createdCount = 0;

            for (int i = 0; i < 500; i++) {
                String name = faker.company().name();

                // Prüfen, ob Organisation mit dem Namen schon existiert
                if (organisationRepository.findByName(name).isPresent()) {
                    log.info("Organisation mit Namen '{}' existiert bereits, überspringe.", name);
                    continue;
                }

                Organisation organisation = Organisation.builder()
                        .name(name)
                        .address(faker.address().fullAddress())
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().phoneNumber())
                        .build();

                organisationRepository.save(organisation);
                createdCount++;
                log.info("Organisation {} gespeichert: {}", createdCount, name);
            }

            log.info("{} Organisationen für DEV-Profil erfolgreich eingefügt (ohne Duplikate).", createdCount);
        };
    }

    @Bean
    @Profile("prod")
    CommandLineRunner initDbProd() {
        return args -> {
            log.info("Produktivprofil aktiv – keine Organisationen eingefügt.");
        };
    }

}




