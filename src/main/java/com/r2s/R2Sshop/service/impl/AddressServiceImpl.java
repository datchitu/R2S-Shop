package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.AddressRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.AddressService;
import com.r2s.R2Sshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {
    private Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());

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

    /**
     * Add new address with user.
     * <p>
     * This function is used to add a new address with user.
     * @param newAddress
     * @param userName
     * @return information of address if the add process is successful
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Address addWithUser(String userName, Map<String, Object> newAddress) {
        User foundUser = userService.findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        Address address = new Address();
        address.setStreet(newAddress.get("street").toString());
        address.setCity(newAddress.get("city").toString());
        address.setCountry(newAddress.get("country").toString());
        address.setDeleted(false);
        address.setUser(foundUser);
        return this.addressRepository.save(address);
    }
    /**
     * Update address by id and userName.
     * <p>
     * This function updates address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @param address
     * @return Address by id and userName if the update process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if address does not exist in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Address updateByIdAndUserName(String userName, Long id, Map<String, Object> address) {
        Address foundAddress = addressRepository.findByIdAndUser_UserName(id, userName)
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        foundAddress.setStreet(address.get("street").toString());
        foundAddress.setCity(address.get("city").toString());
        foundAddress.setCountry(address.get("country").toString());
        foundAddress.setUpdatedAt(ts);
        return this.addressRepository.save(foundAddress);
    }

    /**
     * Delete address by id and userName.
     * <p>
     * This function delete address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @return Address by id and userName if the delete process is successful
     * @throws AppException(ResponseCode.NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if address already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Address deleteByIdAndUserName(String userName, Long id) {
        Address foundAddress = addressRepository.findByIdAndUser_UserName(id, userName)
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        if (foundAddress.getDeleted().equals(true)) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundAddress.setDeleted(true);
        foundAddress.setUpdatedAt(ts);
        return this.addressRepository.save(foundAddress);
    }

    /**
     * Reactivate address by id.
     * <p>
     * This function reactivate address by id, with the userName, id as the input parameter.
     * @param id
     * @return Address by id if the reactivate process is successful
     * @throws AppException(ResponseCode.NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_REACTIVATED) if address already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Address reactivateById(Long id) {
        Address foundAddress = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        if (foundAddress.getDeleted().equals(false)) {
            throw new AppException(ResponseCode.DATA_ALREADY_REACTIVATED);
        }
        foundAddress.setDeleted(false);
        foundAddress.setUpdatedAt(ts);
        return this.addressRepository.save(foundAddress);
    }
}
