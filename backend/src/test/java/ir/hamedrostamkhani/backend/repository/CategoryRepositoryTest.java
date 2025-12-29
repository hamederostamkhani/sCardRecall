package ir.hamedrostamkhani.backend.repository;

import ir.hamedrostamkhani.backend.config.JpaAuditingConfig;
import ir.hamedrostamkhani.backend.model.Category;
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
public class CategoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Category rootCategory;

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

        rootCategory = Category.builder()
                .title("Language Learning")
                .description("This category is for learning languages")
                .owner(user)
                .build();
        categoryRepository.save(rootCategory);
    }

    @Test
    void testSaveCategory_WhenAllAttributesAreValid_ShouldPersist() {
        // Arrange
        Category validCategory = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();

        // Act
        Category savedCategory = categoryRepository.save(validCategory);

        // Assert
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getTitle()).isEqualTo(validCategory.getTitle());
        assertThat(savedCategory.getDescription()).isEqualTo(validCategory.getDescription());
        assertThat(savedCategory.getOwner()).isEqualTo(validCategory.getOwner());
        assertThat(savedCategory.getParent()).isEqualTo(validCategory.getParent());
    }

    @Test
    void testSaveCategory_WhenTitleIsNotProvided_ShouldThrowException() {
        // Arrange
        Category invalidCategory = Category.builder()
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> categoryRepository.save(invalidCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveCategory_WhenOwnerIsNotProvided_ShouldThrowException() {
        // Arrange
        Category invalidCategory = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .parent(rootCategory)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> categoryRepository.save(invalidCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveCategory_WhenSiblingCategoriesHaveSameTitle_ShouldThrowException() {
        // Arrange
        Category subCategory1 = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();
        categoryRepository.save(subCategory1);

        Category subCategory2 = Category.builder()
                .title("English Language")
                .description("This is another English language category")
                .owner(user)
                .parent(rootCategory)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> categoryRepository.save(subCategory2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testHardDelete_WhenCategoryExists_ShouldRemoveFromDatabase() {
        // Arrange
        Category subCategory = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();
        categoryRepository.save(subCategory);

        // Act
        categoryRepository.delete(subCategory);
        Optional<Category> deletedCategory = categoryRepository.findById(subCategory.getId());

        // Assert
        assertThat(deletedCategory).isEmpty();
    }

    // Todo: Add test for cascade delete in hard delete

    @Test
    void testSoftDelete_WhenCategoryExists_ShouldUpdateDeletedAt() {
        // Arrange
        Category subCategory = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();
        categoryRepository.save(subCategory);

        // Act
        var deletedCategoryCount = categoryRepository.softDeleteById(subCategory.getId(), LocalDateTime.now());
        entityManager.flush();
        entityManager.clear();
        Optional<Category> deletedCategory = categoryRepository.findById(subCategory.getId());

        // Assert
        assertThat(deletedCategoryCount).isEqualTo(1);
        assertThat(deletedCategory).isPresent();
        assertThat(deletedCategory.get().getId()).isEqualTo(subCategory.getId());
        assertThat(deletedCategory.get().getDeletedAt()).isNotNull();
    }

    // Todo: Add test for cascade delete in soft delete (update deleted_at for sub child)

    @Test
    void testFindById_WhenCategoryExists_ShouldReturnCategory() {
        // Act
        Optional<Category> foundCategory = categoryRepository.findById(1L); // Root Category Id=1

        // Assert
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getTitle()).isEqualTo(rootCategory.getTitle());
        assertThat(foundCategory.get().getDescription()).isEqualTo(rootCategory.getDescription());
        assertThat(foundCategory.get().getOwner()).isEqualTo(rootCategory.getOwner());
        assertThat(foundCategory.get().getParent()).isNull();
    }

    @Test
    void testFindById_WhenCategoryDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Category> foundCategory = categoryRepository.findById(50L);

        // Assert
        assertThat(foundCategory).isEmpty();
    }

    @Test
    void testFindAllCategories_ShouldReturnAllCategories() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            categoryRepository.save(
                    Category.builder()
                            .title("Category " + i)
                            .description("Description of category " + i)
                            .owner(user)
                            .parent(i % 2 == 0 ? rootCategory : null)
                            .build()
            );
        }
        categoryRepository.softDeleteById(rootCategory.getId(), LocalDateTime.now().minusHours(5));
        entityManager.flush();
        entityManager.clear();

        // Act
        Page<Category> page = categoryRepository.findAll(PageRequest.of(0, 11));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(11);
        assertThat(page.getContent())
                .filteredOn(category -> category.getDeletedAt() != null)
                .hasSize(1);

        assertThat(page.getContent())
                .filteredOn(category -> category.getDeletedAt() == null)
                .hasSize(10);
    }

    @Test
    void testFindAllCategories_WhenCategoriesAreNotDeleted_ShouldReturnNotDeletedCategories() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            categoryRepository.save(
                    Category.builder()
                            .title("Category " + i)
                            .description("Description of category " + i)
                            .owner(user)
                            .parent(i % 2 == 0 ? rootCategory : null)
                            .build()
            );
        }

        // Act
        Page<Category> page = categoryRepository.findAllNotDeleted(PageRequest.of(0, 10));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getContent())
                .allMatch(category -> category.getDeletedAt() == null);
    }

    @Test
    void testFindAllCategories_WhenCategoriesAreDeleted_ShouldReturnDeletedCategories() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            var category = Category.builder()
                    .title("Category " + i)
                    .description("Description of category " + i)
                    .owner(user)
                    .parent(i % 2 == 0 ? rootCategory : null)
                    .build();
            categoryRepository.save(category);
            categoryRepository.softDeleteById(category.getId(), LocalDateTime.now().minusHours(5));
        }

        entityManager.flush();
        entityManager.clear();

        // Act
        Page<Category> page = categoryRepository.findAllDeleted(PageRequest.of(0, 10));

        // Assert
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getContent())
                .allMatch(category -> category.getDeletedAt() != null);
    }

    @Test
    void testUpdateCategory_WhenDescriptionIsChanged_ShouldPersistChange() {
        // Act
        rootCategory.setDescription("Updated description");
        categoryRepository.save(rootCategory);

        // Assert
        Optional<Category> updatedCategory = categoryRepository.findById(rootCategory.getId());
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testUpdateCategory_WhenOwnerIsChanged_ShouldPersistChange() {
        // Arrange
        User user2 = User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("password1")
                .role(Role.ADMIN)
                .active(true)
                .build();
        userRepository.save(user2);

        // Act
        rootCategory.setOwner(user2);
        categoryRepository.save(rootCategory);

        // Assert
        Optional<Category> updatedCategory = categoryRepository.findById(rootCategory.getId());
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getOwner()).isEqualTo(user2);
    }

    @Test
    void testUpdateCategory_WhenParentIsChanged_ShouldPersistChange() {
        // Arrange
        Category category = Category.builder()
                .title("English Language Learning")
                .description("This category is for learning English")
                .parent(rootCategory)
                .owner(user)
                .build();
        categoryRepository.save(category);

        // Act
        category.setParent(null);
        categoryRepository.save(category);

        // Assert
        Optional<Category> updatedCategory = categoryRepository.findById(category.getId());
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getParent()).isNull();
    }

    @Test
    void testUpdateCategory_WhenChangingTitleToDuplicateUnderSameParentAndSameOwner_ShouldThrowException() {
        // Arrange
        Category subCategory1 = Category.builder()
                .title("English Language")
                .description("This category is for learning English language")
                .owner(user)
                .parent(rootCategory)
                .build();
        categoryRepository.save(subCategory1);

        Category subCategory2 = Category.builder()
                .title("German Language")
                .description("This category is for learning German language")
                .owner(user)
                .parent(rootCategory)
                .build();
        categoryRepository.saveAndFlush(subCategory2);

        // Act & Assert
        subCategory2.setTitle("English Language");
        categoryRepository.save(subCategory2);
        assertThatThrownBy(() -> {
            categoryRepository.save(subCategory2);
            categoryRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testAuditingFields_WhenCategorySaved_ShouldBePopulated() {
        // Assert
        assertThat(rootCategory.getCreatedAt()).isNotNull();
        assertThat(rootCategory.getUpdatedAt()).isNotNull();
        assertThat(rootCategory.getCreatedBy()).isNotNull();
        assertThat(rootCategory.getUpdatedBy()).isNotNull();
    }
}
