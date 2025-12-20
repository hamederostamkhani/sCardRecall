package ir.hamedrostamkhani.backend.mapper;

import ir.hamedrostamkhani.backend.dto.request.UserCreateDTO;
import ir.hamedrostamkhani.backend.dto.request.UserUpdateDTO;
import ir.hamedrostamkhani.backend.dto.response.UserResponseDTO;
import ir.hamedrostamkhani.backend.model.User;
import ir.hamedrostamkhani.backend.model.enums.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", source = "role")
    @Mapping(target = "avatar", ignore = true) // handled in Service
    User toEntity(UserCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "avatar", ignore = true) // handled in Service
    void update(UserUpdateDTO dto, @MappingTarget User user);

    @Mapping(target = "role", source = "role")
    UserResponseDTO toDto(User entity);

    /* =========================
       Role mapping
       ========================= */

    default Role map(String role) {
        return role == null ? null : Role.valueOf(role.toUpperCase());
    }

    default String map(Role role) {
        return role == null ? null : role.name().toLowerCase();
    }
}
