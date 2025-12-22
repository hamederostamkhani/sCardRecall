package ir.hamedrostamkhani.backend.repository;

import ir.hamedrostamkhani.backend.config.JpaAuditingConfig;
import ir.hamedrostamkhani.backend.model.User;
import ir.hamedrostamkhani.backend.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .name("Jack Smith")
                .email("jack@example.com")
                .password("securePass")
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(user);
    }

    @Test
    void testSaveUser_WhenAllAttributesAreValid_ShouldPersist() {
        // Arrange
        User validUser = User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("password1")
                .role(Role.ADMIN)
                .active(true)
                .build();

        // Act
        User savedUser = userRepository.save(validUser);

        // Assert
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(validUser.getName());
        assertThat(savedUser.getEmail()).isEqualTo(validUser.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(validUser.getPassword());
        assertThat(savedUser.getRole()).isEqualTo(validUser.getRole());
        assertThat(savedUser.isActive()).isTrue();
    }

    @Test
    void testSaveUser_WhenNameIsNotProvided_ShouldThrowException() {
        // Arrange
        User invalidUser = User.builder()
                .email("jack@example.com")
                .password("securePass")
                .role(Role.USER)
                .active(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userRepository.save(invalidUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveUser_WhenEmailIsNotProvided_ShouldThrowException() {
        // Arrange
        User invalidUser = User.builder()
                .name("Jack Smith")
                .password("securePass")
                .role(Role.USER)
                .active(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userRepository.save(invalidUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveUser_WhenPasswordIsNotProvided_ShouldThrowException() {
        // Arrange
        User invalidUser = User.builder()
                .name("Jack Smith")
                .email("jack@example.com")
                .role(Role.USER)
                .active(true)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> userRepository.save(invalidUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveUser_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        User user1 = User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("password1")
                .role(Role.USER)
                .active(true)
                .build();

        User user2 = User.builder()
                .name("Bob")
                .email("alice@example.com") // duplicate email
                .password("password2")
                .role(Role.USER)
                .active(false)
                .build();

        userRepository.save(user1);

        // Act + Assert
        assertThatThrownBy(() -> userRepository.saveAndFlush(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testHardDelete_WhenUserExists_ShouldRemoveFromDatabase() {
        // Act
        userRepository.delete(user);
        Optional<User> deletedUser = userRepository.findById(user.getId());

        // Assert
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void testSoftDelete_WhenUserExists_ShouldUpdateDeletedAt() {
        // Act
        var deletedUserCount = userRepository.softDeleteById(user.getId(), LocalDateTime.now());
        entityManager.flush();
        entityManager.clear();
        Optional<User> deletedUser = userRepository.findById(user.getId());

        // Assert
        assertThat(deletedUserCount).isEqualTo(1);
        assertThat(deletedUser).isPresent();
        assertThat(deletedUser.get().getId()).isEqualTo(user.getId());
        assertThat(deletedUser.get().getDeletedAt()).isNotNull();
    }

    @Test
    void testFindByEmail_WhenUserExists_ShouldReturnUser() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("jack@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jack Smith");
        assertThat(foundUser.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void testFindByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testEmailExistence_WhenEmailExist_ShouldReturnTrue() {
        // Act
        var result = userRepository.existsByEmail("jack@example.com");

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void testEmailExistence_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // Act
        var result = userRepository.existsByEmail("notfound@example.com");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void testFindAllUsers_ShouldReturnAllUsers() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            userRepository.save(
                    User.builder()
                            .name("User " + i)
                            .email("user" + i + "@example.com")
                            .password("pass")
                            .role(i % 2 == 0 ? Role.USER : Role.ADMIN)
                            .active(i % 3 == 0)
                            .build()
            );
        }
        userRepository.softDeleteById(user.getId(), LocalDateTime.now().minusHours(5));
        entityManager.flush();
        entityManager.clear();

        // Act
        Page<User> page = userRepository.findAll(PageRequest.of(0, 11));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(11);
        assertThat(page.getContent())
                .filteredOn(user -> user.getDeletedAt() != null)
                .hasSize(1);

        assertThat(page.getContent())
                .filteredOn(user -> user.getDeletedAt() == null)
                .hasSize(10);
    }

    @Test
    void testFindAllUsers_WhenUsersAreNotDeleted_ShouldReturnNotDeletedUsers() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            userRepository.save(
                    User.builder()
                            .name("User " + i)
                            .email("user" + i + "@example.com")
                            .password("pass")
                            .role(i % 2 == 0 ? Role.USER : Role.ADMIN)
                            .active(i % 3 == 0)
                            .build()
            );
        }

        // Act
        Page<User> page = userRepository.findAllNotDeleted(PageRequest.of(0, 10));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getContent())
                .allMatch(user -> user.getDeletedAt() == null);
    }

    @Test
    void testFindAllUsers_WhenUsersAreDeleted_ShouldReturnDeletedUsers() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            var user = User.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .password("pass")
                    .role(i % 2 == 0 ? Role.USER : Role.ADMIN)
                    .active(i % 3 == 0)
                    .build();
            userRepository.save(user);
            userRepository.softDeleteById(user.getId(), LocalDateTime.now().minusHours(5));
        }

        entityManager.flush();
        entityManager.clear();

        // Act
        Page<User> page = userRepository.findAllDeleted(PageRequest.of(0, 10));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getContent())
                .allMatch(user -> user.getDeletedAt() != null);
    }

    @Test
    void testUpdateUser_WhenUserBeSetEnabled_ShouldPersistChange() {
        // Act
        user.setActive(true);
        userRepository.save(user);

        // Assert
        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().isActive()).isTrue();
    }

    @Test
    void testUpdateUser_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        User user1 = User.builder().name("Eva").email("eva@example.com").password("evaPass")
                .role(Role.USER).active(false).build();
        User user2 = User.builder().name("Charlie").email("charlie@example.com").password("charliePass")
                .role(Role.ADMIN).active(true).build();
        userRepository.save(user1);
        userRepository.save(user2);

        // Act & Assert
        assertThatThrownBy(() -> {
            user2.setEmail("eva@example.com");
            userRepository.saveAndFlush(user2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testAuditingFields_WhenUserSaved_ShouldBePopulated() {
        // Assert
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getCreatedBy()).isNotNull();
        assertThat(user.getUpdatedBy()).isNotNull();
    }
}
