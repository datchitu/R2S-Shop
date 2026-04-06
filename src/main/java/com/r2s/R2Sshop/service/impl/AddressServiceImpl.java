package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.repository.AddressRepository;
import com.r2s.R2Sshop.service.AddressService;
import com.r2s.R2Sshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserService userService;

    /**
     * Find list by userId and deleted.
     * <p>
     * This function returns address list by userId and deleted, with the userName and deleted as the input parameter.
     * @param userName
     * @param deleted
     * @return user id by userId
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public List<Address> findByUserIdAndDeleted(String userName, Boolean deleted) {
        return addressRepository.findByUserNameAndDeleted(userName, deleted);
    }

}
