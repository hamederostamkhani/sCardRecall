package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.abstracts.Auditable;
import ir.hamedrostamkhani.backend.model.abstracts.ShareableResource;
import ir.hamedrostamkhani.backend.model.enums.Algorithm;
import ir.hamedrostamkhani.backend.model.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boxes", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_box_title_category",
                columnNames = {"category_id", "title"}
        )
})
public class Box extends Auditable implements ShareableResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Algorithm algorithm = Algorithm.LEITNER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    @Override
    public Long getResourceId() {
        return this.id;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.BOX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Box{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category.getId() +
                ", algorithm=" + algorithm +
                ", createdBy='" + this.getCreatedBy() + '\'' +
                ", updatedBy='" + this.getUpdatedBy() + '\'' +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() +
                ", deletedAt=" + this.getDeletedAt() +
                '}';
    }
}
