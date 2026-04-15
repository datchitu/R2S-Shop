package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    @Query("SELECT u FROM User u WHERE (:deleted IS NULL OR u.deleted = :deleted)")
    Page<User> findAllByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);
}
