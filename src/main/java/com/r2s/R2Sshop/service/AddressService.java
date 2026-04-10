package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.AddressDTORequest;
import com.r2s.R2Sshop.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> findByUserIdAndDeleted(Integer status, String userName);
    Address findById(Long id);
    Address addWithUser(String userName, AddressDTORequest DTORequest);
    Address updateByIdAndUserName(String userName, Long id, AddressDTORequest DTORequest);
    Address deleteByIdAndUserName(String userName, Long id);
    Address reactivateById(Long id);
}
