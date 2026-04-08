package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    List<Address> findByUserIdAndDeleted(String userName, Boolean deleted);
    Address addWithUser(String userName, Map<String, Object> newAddress);
    Address updateByIdAndUserName(String userName, Long id,Map<String, Object> address);
    Address deleteByIdAndUserName(String userName, Long id);
    Address reactivateById(Long id);
}
