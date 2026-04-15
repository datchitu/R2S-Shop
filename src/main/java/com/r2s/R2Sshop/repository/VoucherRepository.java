package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    boolean existsByName(String name);
    @Query("SELECT v FROM Voucher v WHERE (:deleted IS NULL OR v.deleted = :deleted)")
    Page<Voucher> findAllByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);
}
