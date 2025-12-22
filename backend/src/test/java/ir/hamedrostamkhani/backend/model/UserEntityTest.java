package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


class UserEntityTest {

    @Test
    void testUserEntityBuilder_WhenOptionalFieldsOmitted_ShouldUseDefaults() {
        // Arrange
        User user = User.builder()
                .name("Jack")
                .email("jack@example.com")
                .password("secret123")
                .avatar("/path/to-images/my-image.jpg")
                .build();

        // Act & Assert
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.isActive()).isFalse();
    }

    @Test
    void testUserEntityEquals_WhenIdsAreEqual_ShouldReturnTrue() {
        // Arrange
        User u1 = new User();
        User u2 = new User();

        ReflectionTestUtils.setField(u1, "id", 1L);
        ReflectionTestUtils.setField(u2, "id", 1L);

        // Act & Assert
        assertThat(u1).isEqualTo(u2);
    }

    @Test
    void testUserEntityEquals_WhenIdsAreDifferent_ShouldReturnFalse() {
        // Arrange
        User u1 = new User();
        User u2 = new User();

        ReflectionTestUtils.setField(u1, "id", 1L);
        ReflectionTestUtils.setField(u2, "id", 2L);

        // Act & Assert
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void testUserEntityEquals_WhenIdIsNull_ShouldReturnFalse() {
        // Arrange
        User u1 = new User();
        User u2 = new User();

        // Act & Assert
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void testUserEntityHashCode_ForSameClass_ShouldBeSame() {
        // Arrange
        User u1 = new User();
        User u2 = new User();

        // Act & Assert
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }
}
