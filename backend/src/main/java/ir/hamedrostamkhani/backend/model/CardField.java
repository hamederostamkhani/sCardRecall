package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.abstracts.Auditable;
import ir.hamedrostamkhani.backend.model.enums.FieldType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_fields", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_card_field_key_card",
                columnNames = {"key", "card_id"}
        )
})
public class CardField extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String key;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType type;
    @Column(columnDefinition = "TEXT")
    private String metaData; // Todo: check which is better: String or HashMap<String, String>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardField other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CardField{" +
                "id=" + id +
                ", key=" + key +
                ", value=" + value +
                ", type=" + type +
                ", metaData=" + metaData +
                ", card=" + (card != null ? card.getId() : null) +
                ", createdBy='" + this.getCreatedBy() + '\'' +
                ", updatedBy='" + this.getUpdatedBy() + '\'' +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() +
                ", deletedAt=" + this.getDeletedAt() +
                '}';
    }
}
