package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeleted(Boolean deleted);
}
