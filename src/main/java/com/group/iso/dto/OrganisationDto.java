package com.group.iso.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Organisation data transfer object")
public class OrganisationDto {

    @Schema(description = "Unique identifier", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Organisation name")
    private String name;

    @Size(max = 255)
    @Schema(description = "Address of the organisation")
    private String address;

    @Email
    @Schema(description = "Email address")
    private String email;

    @Size(max = 20)
    @Schema(description = "Phone number")
    private String phone;

}
