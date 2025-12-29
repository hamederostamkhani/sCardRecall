package ir.hamedrostamkhani.backend.repository;

import ir.hamedrostamkhani.backend.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.deletedAt is null")
    Page<Category> findAllNotDeleted(Pageable pageable);

    @Query("select c from Category c where c.deletedAt is not null")
    Page<Category> findAllDeleted(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Category c set c.deletedAt = :deleteDateTime where c.id = :id and c.deletedAt is null")
    int softDeleteById(@Param("id") Long id, @Param("deleteDateTime") LocalDateTime deleteDateTime);

    @Transactional
    @Modifying
    @Query("update Category c set c.deletedAt = null where c.id = :id and c.deletedAt is not null")
    int restoreById(Long id);
}
