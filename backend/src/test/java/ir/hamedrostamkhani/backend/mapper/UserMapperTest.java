package ir.hamedrostamkhani.backend.mapper;

import ir.hamedrostamkhani.backend.dto.request.UserCreateDTO;
import ir.hamedrostamkhani.backend.dto.request.UserUpdateDTO;
import ir.hamedrostamkhani.backend.dto.response.UserResponseDTO;
import ir.hamedrostamkhani.backend.model.User;
import ir.hamedrostamkhani.backend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testUserCreateDTOToEntity_WhenAllAttributesAreValid_ShouldMapCorrectly() {
        // Arrange
        UserCreateDTO dto = new UserCreateDTO(
                "Hamed Rostamkhani",
                "h@test.com",
                "1234",
                null,
                "admin",
                true
        );

        // Act
        User user = mapper.toEntity(dto);

        // Assert
        assertThat(user.getName()).isEqualTo("Hamed Rostamkhani");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void testUserUpdateDTOToEntity_WhenSomeAttributesAreNotChanged_ShouldMapCorrectly() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Old Name")
                .email("old@example.com")
                .password("oldpass")
                .avatar("old.png")
                .role(Role.USER)
                .active(false)
                .build();

        UserUpdateDTO dto = new UserUpdateDTO(
                "NewFirst Name",
                "new@example.com",
                null, // password is not changed
                null, // avatar is not changed
                "admin",
                true
        );

        // Act
        mapper.update(dto, user);

        // Assert
        assertThat(user.getName()).isEqualTo("NewFirst Name"); // Just firstName is changed
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("oldpass"); // Password is not changed
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void testEntityToDTO_ShouldMapCorrectly() {
        User user = User.builder()
                .name("Hamed Rostamkhani")
                .email("h@test.com")
                .password("MyPass")
                .avatar("path/to/avatar.png")
                .role(Role.USER)
                .active(false)
                .build();

        UserResponseDTO dto = mapper.toDto(user);

        assertThat(dto.name()).isEqualTo("Hamed Rostamkhani");
        assertThat(dto.role()).isEqualTo("user");
    }
}
