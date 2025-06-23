package com.group.iso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.iso.dto.OrganisationDto;
import com.group.iso.model.Organisation;
import com.group.iso.repository.OrganisationRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrganisationControllerTest {

    @Autowired
    private OrganisationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.reset();
        OrganisationController controller = new OrganisationController(repository);
        RestAssuredMockMvc.standaloneSetup(controller);
        repository.deleteAll();
    }

    @Test
    void shouldReturnPagedOrganisations() {
        repository.saveAll(List.of(
                Organisation.builder().name("Org A").email("a@example.com").address("Addr A").phone("111").build(),
                Organisation.builder().name("Org B").email("b@example.com").address("Addr B").phone("222").build(),
                Organisation.builder().name("Org C").email("c@example.com").address("Addr C").phone("333").build()
        ));

        RestAssuredMockMvc.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 0)
                .queryParam("size", 2)
                .queryParam("sort", "name,asc")
                .when()
                .get("/api/organisations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(2))
                .body("content[0].name", equalTo("Org A"))
                .body("content[1].name", equalTo("Org B"))
                .body("totalElements", equalTo(3));
    }

    @Test
    void shouldCreateOrganisation() throws Exception {
        OrganisationDto dto = OrganisationDto.builder()
                .name("Org X")
                .email("x@example.com")
                .address("X Street")
                .phone("123")
                .build();

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/api/organisations")
                .then()
                .statusCode(200)
                .body("name", equalTo("Org X"));
    }

    @Test
    void shouldUpdateOrganisation() throws Exception {
        Organisation saved = repository.save(Organisation.builder()
                .name("Old Org")
                .email("old@example.com")
                .address("Old Street")
                .phone("111")
                .build());

        OrganisationDto dto = OrganisationDto.builder()
                .name("New Org")
                .email("new@example.com")
                .address("New Street")
                .phone("222")
                .build();

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .put("/api/organisations/{id}", saved.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("New Org"));
    }

    @Test
    void shouldDeleteOrganisation() {
        Organisation saved = repository.save(Organisation.builder()
                .name("To Delete")
                .email("del@example.com")
                .address("Del Street")
                .phone("333")
                .build());

        RestAssuredMockMvc.when()
                .delete("/api/organisations/{id}", saved.getId())
                .then()
                .statusCode(204);
    }

}