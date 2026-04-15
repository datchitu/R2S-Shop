package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.VoucherDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Voucher;
import com.r2s.R2Sshop.repository.VoucherRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.VoucherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Return voucher by id.
     * <p>
     * This function returns voucher by id, with the id as the input parameter
     * @param id
     * @return Information of voucher by id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if the voucher cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Voucher findById(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.VOUCHER_NOT_FOUND));
    }
    /**
     * Return voucher list by deleted.
     * <p>
     * This function returns all voucher by deleted (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status and pageable as the input parameter.
     * @param status
     * @param pageable
     * @return Voucher list by deleted
     * @author HoangVu
     * @since 1.0
     */
    public Page<Voucher> findAllByDeleted(Integer status, Pageable pageable) {
        if (status == -1) {
            return voucherRepository.findAllByDeleted(null, pageable);
        } else if (status == 0) {
            return voucherRepository.findAllByDeleted(false, pageable);
        } else {
            return voucherRepository.findAllByDeleted(true, pageable);
        }
    }
    /**
     * Add new voucher.
     * <p>
     * This function is used to add a new voucher.
     * @param dtoRequest
     * @return information of voucher if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if voucher be found by name
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Voucher add(VoucherDTORequest dtoRequest) {
        if (voucherRepository.existsByName(dtoRequest.getName())) {
            throw new AppException(ResponseCode.VOUCHER_ALREADY_EXISTS);
        }
        Voucher voucher = modelMapper.map(dtoRequest, Voucher.class);
        return voucherRepository.save(voucher);
    }
    /**
     * Update voucher by id.
     * <p>
     * This function updates voucher by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return voucher by id if the update process is successful
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND) if voucher does not exist in the database
     * @throws AppException(ResponseCode.VOUCHER_ALREADY_EXISTS)
     * if voucher already been existed in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Voucher updateById(Long id, VoucherDTORequest dtoRequest) {
        Voucher foundVoucher = findById(id);
        if (voucherRepository.existsByName(dtoRequest.getName())) {
            throw new AppException(ResponseCode.VOUCHER_ALREADY_EXISTS);
        }
        modelMapper.map(dtoRequest, foundVoucher);
        return voucherRepository.save(foundVoucher);
    }
    /**
     * Delete voucher by id.
     * <p>
     * This function deletes voucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if voucher does not exist in the database
     * @throws AppException(ResponseCode.VOUCHER_ALREADY_DELETED)
     * if voucher already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void deleteById(Long id) {
        Voucher foundVoucher = findById(id);
        if (Boolean.TRUE.equals(foundVoucher.getDeleted())) {
            throw new AppException(ResponseCode.VOUCHER_ALREADY_DELETED);
        }
        foundVoucher.setDeleted(true);
        voucherRepository.save(foundVoucher);
    }
    /**
     * Reactivate voucher by id.
     * <p>
     * This function reactivates voucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if voucher does not exist in the database
     * @throws AppException(ResponseCode.VOUCHER_ALREADY_REACTIVATED)
     * if voucher already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void reactivateById(Long id) {
        Voucher foundVoucher = findById(id);
        if (Boolean.FALSE.equals(foundVoucher.getDeleted())) {
            throw new AppException(ResponseCode.VOUCHER_ALREADY_REACTIVATED);
        }
        foundVoucher.setDeleted(false);
        voucherRepository.save(foundVoucher);
    }
}
