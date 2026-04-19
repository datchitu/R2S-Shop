package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.AddressDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.AddressRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.AddressService;
import com.r2s.R2Sshop.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

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
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if the address cannot be found by id
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
     * @param dtoRequest
     * @param userName
     * @return information of address if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS)
     * if street, city, userName and deleted exist in the database
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if address does not exist in the database
     * @author HoangVu
     * @since 1.5
     */
    @Transactional
    @Override
    public Address addWithUser(String userName, AddressDTORequest dtoRequest) {
        if (addressRepository.existsByStreetAndCityAndUser_UserNameAndDeleted(
                dtoRequest.getStreet(), dtoRequest.getCity(), userName, false)) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        User foundUser = userService.findByUserName(userName);
        Address address = addressRepository.findByStreetAndCityAndUser_UserNameAndDeleted(
                dtoRequest.getStreet(), dtoRequest.getCity(), userName, true)
                .orElseGet(() -> {
                    Address newAddress = modelMapper.map(dtoRequest, Address.class);
                    newAddress.setDefaulted(
                            !addressRepository.existsByDefaultedAndUser_UserName(true, userName));
                    newAddress.setUser(foundUser);
                    return newAddress;
                });
        address.setReceiverName(dtoRequest.getReceiverName());
        address.setPhoneNumber(dtoRequest.getPhoneNumber());
        address.setDeleted(false);
        return addressRepository.save(address);
    }
    /**
     * Update address by id and userName.
     * <p>
     * This function updates address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @param dtoRequest
     * @return address by id and userName if the update process is successful
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if address already been deleted in the database
     * @throws AppException(ResponseCode.IMMUTABLE) if street, city and country remains unchanged
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS)
     * if street, city, userName and deleted exist in the database
     * @author HoangVu
     * @since 1.6
     */
    @Transactional
    @Override
    public Address updateByIdAndUserName(String userName, Long id, AddressDTORequest dtoRequest) {
        Address foundAddress = findById(id);
        if (!foundAddress.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        if (Objects.equals(foundAddress.getStreet(), dtoRequest.getStreet()) &&
                Objects.equals(foundAddress.getCity(), dtoRequest.getCity()) &&
                Objects.equals(foundAddress.getCountry(), dtoRequest.getCountry()) &&
                Objects.equals(foundAddress.getReceiverName(), dtoRequest.getReceiverName()) &&
                Objects.equals(foundAddress.getPhoneNumber(), dtoRequest.getPhoneNumber())) {
            throw new AppException(ResponseCode.IMMUTABLE);
        }
        if (!Objects.equals(foundAddress.getStreet(), dtoRequest.getStreet()) ||
                !Objects.equals(foundAddress.getCity(), dtoRequest.getCity())) {
            if (addressRepository.existsByStreetAndCityAndUser_UserNameAndDeleted(
                    dtoRequest.getStreet(), dtoRequest.getCity(), userName, false)) {
                throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
            }
        }
        modelMapper.map(dtoRequest, foundAddress);
        return addressRepository.save(foundAddress);
    }
    /**
     * Set default address by id and userName.
     * <p>
     * This function Sets default address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if address already been deleted in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DEFAULTED) if address has been set to default
     * @author HoangVu
     * @since 1.0
     */
    @Transactional
    @Override
    public void setDefault(String userName, Long id) {
        Address foundAddress = findById(id);
        if (!foundAddress.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDefaulted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DEFAULTED);
        }
        addressRepository.findByDefaultedAndUser_UserName(true, userName)
                .ifPresent(oldDefault -> {
                    oldDefault.setDefaulted(false);
                addressRepository.save(oldDefault);
        });
        foundAddress.setDefaulted(true);
        addressRepository.save(foundAddress);
    }

    /**
     * Delete address by id and userName.
     * <p>
     * This function deletes address by id and userName, with the userName, id as the input parameter.
     * @param userName
     * @param id
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED) if address already been deleted in the database
     * @author HoangVu
     * @since 1.4
     */
    @Override
    public void deleteByIdAndUserName(String userName, Long id) {
        Address foundAddress = findById(id);
        if (!foundAddress.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        if (Boolean.TRUE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        Boolean wasDefault = foundAddress.getDefaulted();
        foundAddress.setDeleted(true);
        foundAddress.setDefaulted(false);
        addressRepository.save(foundAddress);
        if (wasDefault) {
            addressRepository.findFirstByUser_UserNameAndDeletedFalseOrderByCreatedAtDesc(userName)
                    .ifPresent(newDefault -> {
                        newDefault.setDefaulted(true);
                        addressRepository.save(newDefault);
                    });
        }
    }

    /**
     * Reactivate address by id.
     * <p>
     * This function reactivates address by id, with the userName, id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ADDRESS_NOT_FOUND) if address does not exist in the database
     * @throws AppException(ResponseCode.ACCESS_DENIED) if the username does not match
     * the username retrieved from the address's user
     * @throws AppException(ResponseCode.DATA_ALREADY_REACTIVATED) if address already been reactivated in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public void reactivateById(Long id) {
        Address foundAddress = findById(id);
        if (Boolean.FALSE.equals(foundAddress.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_REACTIVATED);
        }
        foundAddress.setDeleted(false);
        addressRepository.save(foundAddress);
    }
}
