package ir.hamedrostamkhani.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String avatar,
        String role,
        boolean active
) { }
