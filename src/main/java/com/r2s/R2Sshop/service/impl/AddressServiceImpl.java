package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.AddressDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.AddressRepository;
import com.r2s.R2Sshop.rest.AppException;
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
     * This function returns address list by userId and deleted(
     * With the passed-in status -1, return all by userName works;
     * with 0, return all by userName and deleted == false works;
     * and otherwise, it's return all by userName and deleted == true),
     * with the userName and status as the input parameter.
     * @param userName
     * @param status
     * @return user id by userId
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public List<Address> findByUserIdAndDeleted(Integer status, String userName) {
        if (status == -1) {
            return addressRepository.findByUserNameAndDeleted(userName, null);
        } else if (status == 0) {
            return addressRepository.findByUserNameAndDeleted(userName, false);
        } else {
            return addressRepository.findByUserNameAndDeleted(userName, true);
        }
    }

    /**
     * Return address by id.
     * <p>
     * This function returns address by id, with the id as the input parameter.
     * @param id
     * @return address by id
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if the address cannot be found by categoriesId
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.ADDRESS_NOT_FOUND));
    }

    /**
     * Add new address with user.
     * <p>
     * This function is used to add a new address with user.
     * @param DTORequest
     * @param userName
     * @return information of address if the add process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if address does not exist in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Address addWithUser(String userName, AddressDTORequest DTORequest) {
        User foundUser = userService.findByUserName(userName);
        Address address = new Address();
        address.setStreet(DTORequest.getStreet());
        address.setCity(DTORequest.getCity());
        address.setCountry(DTORequest.getCountry());
        address.setDeleted(false);
        address.setUser(foundUser);
        return addressRepository.save(address);
    }
    /**
     * Update address by id and userName.
     * <p>
     * This function updates address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @param DTORequest
     * @return address by id and userName if the update process is successful
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if address already been deleted in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public Address updateByIdAndUserName(String userName, Long id, AddressDTORequest DTORequest) {
        Address foundAddress = findById(id);
        if (!foundAddress.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundAddress.setStreet(DTORequest.getStreet());
        foundAddress.setCity(DTORequest.getCity());
        foundAddress.setCountry(DTORequest.getCountry());
        return addressRepository.save(foundAddress);
    }

    /**
     * Delete address by id and userName.
     * <p>
     * This function delete address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @return address by id and userName if the delete process is successful
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if address already been deleted in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Address deleteByIdAndUserName(String userName, Long id) {
        Address foundAddress = findById(id);
        if (!foundAddress.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundAddress.setDeleted(true);
        return addressRepository.save(foundAddress);
    }

    /**
     * Reactivate address by id.
     * <p>
     * This function reactivate address by id, with the userName, id as the input parameter.
     * @param id
     * @return address by id if the reactivate process is successful
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_REACTIVATED) if address already been reactivated in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Address reactivateById(Long id) {
        Address foundAddress = findById(id);
        if (Boolean.FALSE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_REACTIVATED);
        }
        foundAddress.setDeleted(false);
        return addressRepository.save(foundAddress);
    }
}
