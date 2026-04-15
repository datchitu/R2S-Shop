package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.VoucherDTORequest;
import com.r2s.R2Sshop.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService {
    Voucher findById(Long id);
    Page<Voucher> findAllByDeleted(Integer status, Pageable pageable);
    Voucher add(VoucherDTORequest dtoRequest);
    Voucher updateById(Long id, VoucherDTORequest dtoRequest);
    void deleteById(Long id);
    void reactivateById(Long id);
}
