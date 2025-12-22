package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.abstracts.Auditable;
import ir.hamedrostamkhani.backend.model.abstracts.ShareableResource;
import ir.hamedrostamkhani.backend.model.enums.FieldType;
import ir.hamedrostamkhani.backend.model.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card extends Auditable implements ShareableResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardField> fields = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id", nullable = false)
    private Box box;

    @Column(nullable = false)
    private LocalDateTime nextReviewDate;

    // e.g. SM2 Algorithm -> {interval, repetition, easinessFactor}, Leitner -> {level}
    @Column(columnDefinition = "TEXT", nullable = false)
    private String metaData;


    public void addField(CardField field) {
        this.fields.add(field);
    }

    public void addField(String key, String value, FieldType type) {
        this.fields.add(CardField.builder().key(key).value(value).type(type).card(this).build());
    }

    public void addField(String key, String value, FieldType type, String metaData) {
        this.fields.add(CardField.builder().key(key).value(value).type(type).metaData(metaData).card(this).build());
    }

    @Override
    public Long getResourceId() {
        return this.id;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", fields=" + fields +
                ", box=" + (box != null ? box.getId() : null) +
                ", nextReviewDate=" + nextReviewDate +
                ", metaData=" + metaData +
                ", createdBy='" + this.getCreatedBy() + '\'' +
                ", updatedBy='" + this.getUpdatedBy() + '\'' +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() +
                ", deletedAt=" + this.getDeletedAt() +
                '}';
    }
}
