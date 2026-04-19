package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.UserVoucherDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.model.UserVoucher;
import com.r2s.R2Sshop.model.Voucher;
import com.r2s.R2Sshop.repository.UserVoucherRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.UserService;
import com.r2s.R2Sshop.service.UserVoucherService;
import com.r2s.R2Sshop.service.VoucherService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {
    Instant instant = Instant.now();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private ModelMapper modelMapper;
    /**
     * Return userVoucher by id.
     * <p>
     * This function returns userVoucher by id, with the id as the input parameter
     * @param id
     * @return Information of userVoucher by id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if the userVoucher cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public UserVoucher findById(Long id) {
        return userVoucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.USERVOUCHER_NOT_FOUND));
    }

    /**
     * Return userVoucher list by deleted.
     * <p>
     * This function returns all userVoucher by deleted (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status and pageable as the input parameter.
     * @param status
     * @param pageable
     * @return UserVoucher list by deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<UserVoucher> findAllByDeleted(Integer status, Pageable pageable) {
        if (status == -1) {
            return userVoucherRepository.findAllByDeleted(null, pageable);
        } else if (status == 0) {
            return userVoucherRepository.findAllByDeleted(false, pageable);
        } else {
            return userVoucherRepository.findAllByDeleted(true, pageable);
        }
    }

    /**
     * Return userVoucher list by userName and deleted.
     * <p>
     * This function returns all userVoucher by userName and deleted (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the userName and status as the input parameter.
     * @param status
     * @param userName
     * @return UserVoucher list by userName and deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public List<UserVoucher> findAllByUserNameAndDeleted(String userName, Integer status) {
        if (status == -1) {
            return userVoucherRepository.findByUserUserNameAndDeleted(userName, null);
        } else if (status == 0) {
            return userVoucherRepository.findByUserUserNameAndDeleted(userName, false);
        } else {
            return userVoucherRepository.findByUserUserNameAndDeleted(userName, true);
        }
    }
    /**
     * Add new userVoucher.
     * <p>
     * This function is used to add a new userVoucher with userName and voucherId.
     * @param dtoRequest
     * @return information of userVoucher with userName and voucherId if the add process is successful
     * @author HoangVu
     * @since 1.0
     */
    @Transactional
    @Override
    public UserVoucher addWithUserAndVoucher(UserVoucherDTORequest dtoRequest,
                                           Long userId, Long voucherId) {
        User foundUser = userService.findById(userId);
        Voucher foundVoucher = voucherService.findById(voucherId);
        UserVoucher userVoucher = modelMapper.map(dtoRequest, UserVoucher.class);
        userVoucher.setVoucher(foundVoucher);
        userVoucher.setUser(foundUser);
        return userVoucherRepository.save(userVoucher);
    }
    /**
     * Update userVoucher by id.
     * <p>
     * This function updates userVoucher by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return userVoucher by id if the update process is successful
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND) if userVoucher does not exist in the database
     * @author HoangVu
     * @since 1.0
     */
    @Transactional
    @Override
    public UserVoucher updateById(Long id, UserVoucherDTORequest dtoRequest) {
        UserVoucher foundUserVoucher = findById(id);
        modelMapper.map(dtoRequest, foundUserVoucher);
        return userVoucherRepository.save(foundUserVoucher);
    }
    /**
     * Delete userVoucher by id.
     * <p>
     * This function deletes userVoucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if userVoucher does not exist in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_DELETED)
     * if userVoucher already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void deleteById(Long id) {
        UserVoucher foundUserVoucher = findById(id);
        if (Boolean.TRUE.equals(foundUserVoucher.getDeleted())) {
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_DELETED);
        }
        foundUserVoucher.setDeleted(true);
        userVoucherRepository.save(foundUserVoucher);
    }
    /**
     * Reactivate userVoucher by id.
     * <p>
     * This function reactivates userVoucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if userVoucher does not exist in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_REACTIVATED)
     * if userVoucher already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void reactivateById(Long id) {
        UserVoucher foundUserVoucher = findById(id);
        if (Boolean.TRUE.equals(foundUserVoucher.getDeleted())) {
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_REACTIVATED);
        }
        foundUserVoucher.setDeleted(true);
        userVoucherRepository.save(foundUserVoucher);
    }
    /**
     * Release userVoucher by id.
     * <p>
     * This function releases userVoucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if userVoucher does not exist in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_RELEASED)
     * if userVoucher already been released in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void releaseById(Long id) {
        UserVoucher foundUserVoucher = findById(id);
        if (foundUserVoucher.getStatus() == 1){
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_RELEASED);
        }
        foundUserVoucher.setStatus(1);
    }
    /**
     * Use userVoucher by id.
     * <p>
     * This function uses userVoucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if userVoucher does not exist in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_USED)
     * if userVoucher already been used in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void useById(Long id) {
        UserVoucher foundUserVoucher = findById(id);
        if (foundUserVoucher.getStatus() == 2){
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_USED);
        }
        foundUserVoucher.setStatus(2);
        foundUserVoucher.setUsedAt(localDateTime);
    }
    /**
     * Disable userVoucher by id.
     * <p>
     * This function disables userVoucher by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USERVOUCHER_NOT_FOUND)
     * if userVoucher does not exist in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_NOT_RELEASED)
     * if userVoucher already been disabled in the database
     * @throws AppException(ResponseCode.USERVOUCHER_ALREADY_USED)
     * if userVoucher already been used in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public void disableById(Long id) {
        UserVoucher foundUserVoucher = findById(id);
        if (foundUserVoucher.getStatus() == 0){
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_NOT_RELEASED);
        }
        if (foundUserVoucher.getStatus() == 2){
            throw new AppException(ResponseCode.USERVOUCHER_ALREADY_USED);
        }
        foundUserVoucher.setStatus(0);
        foundUserVoucher.setUsedAt(null);
    }
}
