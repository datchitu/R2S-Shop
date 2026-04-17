package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.UserVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    @Query("SELECT uv FROM UserVoucher uv WHERE (:deleted IS NULL OR c.deleted = :deleted)")
    Page<UserVoucher> findAllByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);
    List<UserVoucher> findByUserUserNameAndDeleted(String userName, Boolean deleted);
    boolean existsByUserUserNameAndVoucherId(String userName, Long voucherId);
}
