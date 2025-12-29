package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryEntityTest {

    @Test
    void testCategoryEntityEquals_WhenIdsAreEqual_ShouldReturnTrue() {
        // Arrange
        Category c1 = new Category();
        Category c2 = new Category();

        ReflectionTestUtils.setField(c1, "id", 1L);
        ReflectionTestUtils.setField(c2, "id", 1L);

        // Act & Assert
        assertThat(c1).isEqualTo(c2);
    }

    @Test
    void testCategoryEntityEquals_WhenIdsAreDifferent_ShouldReturnFalse() {
        // Arrange
        Category c1 = new Category();
        Category c2 = new Category();

        ReflectionTestUtils.setField(c1, "id", 1L);
        ReflectionTestUtils.setField(c2, "id", 2L);

        // Act & Assert
        assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    void testCategoryEntityEquals_WhenIdIsNull_ShouldReturnFalse() {
        // Arrange
        Category c1 = new Category();
        Category c2 = new Category();

        // Act & Assert
        assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    void testCategoryEntityHashCode_ForSameClass_ShouldBeSame() {
        // Arrange
        Category c1 = new Category();
        Category c2 = new Category();

        // Act & Assert
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }
}
