package ir.hamedrostamkhani.backend.model;

import ir.hamedrostamkhani.backend.model.abstracts.Auditable;
import ir.hamedrostamkhani.backend.model.enums.Permission;
import ir.hamedrostamkhani.backend.model.enums.ResourceType;
import ir.hamedrostamkhani.backend.model.enums.ShareStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resource_shares")
public class ResourceShare extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // کاربر فرستنده
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    // کاربر گیرنده
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    // نوع منبع (Category, Box, Card)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;

    // شناسه منبع
    @Column(nullable = false)
    private Long resourceId;

    // سطح دسترسی
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    // وضعیت اشتراک‌گذاری
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShareStatus status = ShareStatus.PENDING;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceShare other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ResourceShare{" +
                "id=" + id +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", resourceType=" + resourceType +
                ", resourceId=" + resourceId +
                ", permission=" + permission +
                ", status=" + status +
                ", createdBy='" + this.getCreatedBy() + '\'' +
                ", updatedBy='" + this.getUpdatedBy() + '\'' +
                ", createdAt=" + this.getCreatedAt() +
                ", updatedAt=" + this.getUpdatedAt() +
                ", deletedAt=" + this.getDeletedAt() +
                '}';
    }
}
