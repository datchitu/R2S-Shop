package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> findByUserIdAndDeleted(String userName, Boolean deleted);
}
