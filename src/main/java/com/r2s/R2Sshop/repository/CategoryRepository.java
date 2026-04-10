package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE (:deleted IS NULL OR c.deleted = :deleted)")
    List<Category> findAllByDeleted(@Param("deleted") Boolean deleted);
    boolean existsByName(String name);
}
