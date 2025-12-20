package ir.hamedrostamkhani.backend.repository;

import ir.hamedrostamkhani.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email and u.deletedAt is null")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("select u from User u where u.deletedAt is null")
    Page<User> findAllNotDeleted(Pageable pageable);

    @Query("select u from User u where u.deletedAt is not null")
    Page<User> findAllDeleted(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update User u set u.deletedAt = :deleteDateTime where u.id = :id and u.deletedAt is null")
    int softDeleteById(@Param("id") Long id, @Param("deleteDateTime") LocalDateTime deleteDateTime);

    @Transactional
    @Modifying
    @Query("update User u set u.deletedAt = null where u.id = :id and u.deletedAt is not null")
    int restoreById(Long id);

    boolean existsByEmail(String email);
}
