package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.user.userName = :userName " +
            "AND (:deleted IS NULL OR a.deleted = :deleted)")
    List<Address> findByUserNameAndDeleted(@Param("userName") String userName,
                                           @Param("deleted") Boolean deleted);
    Optional<Address> findByIdAndUser_UserName(Long id, String userName);
}
