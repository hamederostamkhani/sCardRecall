package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.abstracts.Auditable;
import ir.hamedrostamkhani.backend.model.abstracts.ShareableResource;
import ir.hamedrostamkhani.backend.model.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_category_owner_parent_title",
                columnNames = {"user_id", "parent_id", "title"}
        )
})
public class Category extends Auditable implements ShareableResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Override
    public Long getResourceId() {
        return this.id;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CATEGORY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + (owner != null ? owner.getId() : null) +
                ", parent=" + (parent != null ? parent.getId() : null) +
                ", createdBy='" + this.getCreatedBy() + '\'' +
                ", updatedBy='" + this.getUpdatedBy() + '\'' +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() +
                ", deletedAt=" + this.getDeletedAt() +
                '}';
    }
}
