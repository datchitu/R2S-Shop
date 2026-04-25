package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.UserVoucher;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    @Query("SELECT uv FROM UserVoucher uv WHERE (:deleted IS NULL OR uv.deleted = :deleted)")
    Page<UserVoucher> findAllByDeleted(@Param("deleted") Boolean deleted, Pageable pageable);

    @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.userName = :userName " +
            "AND (:deleted IS NULL OR uv.deleted = :deleted)")
    List<UserVoucher> findByUserUserNameAndDeleted(@Param("userName") String userName,
                                                   @Param("deleted") Boolean deleted);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT uv FROM UserVoucher uv WHERE uv.id = :id")
    Optional<UserVoucher> findByIdWithLock(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserVoucher uv SET uv.status = :status " +
            "WHERE uv.voucher.id = :voucherId AND uv.status <> :status")
    int chargeStatusAllByVoucherId(@Param("voucherId") Long voucherId, @Param("status") Integer status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserVoucher uv SET uv.status = 0 " +
            "WHERE uv.voucher.id = :voucherId AND uv.status = 1")
    int disableAllByVoucherId(@Param("voucherId") Long voucherId);
}
