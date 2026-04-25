package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.UserVoucherDTORequest;
import com.r2s.R2Sshop.model.UserVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserVoucherService {
    UserVoucher findById(Long id);
    Page<UserVoucher> findAllByDeleted(Integer status, Pageable pageable);
    List<UserVoucher> findAllByUserNameAndDeleted(String userName, Integer status);
    UserVoucher addWithUserAndVoucher(UserVoucherDTORequest dtoRequest, Long userId, Long voucherId);
    UserVoucher updateById(Long id, UserVoucherDTORequest dtoRequest);
    void deleteById(Long id);
    void restoreById(Long id);
    void releaseById(Long id);
    void releaseAllByVoucherId(Long voucherId);
    UserVoucher useById(Long id);
    void disableById(Long id);
    void disableAllById(Long voucherId);
}
