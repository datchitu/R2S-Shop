package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.*;
import com.r2s.R2Sshop.repository.*;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartLineItemRepository cartLineItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Return order by id.
     * <p>
     * This method returns order by id, with the id as the input parameter.
     * @param id
     * @return order by id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if the order cannot be found by id
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Order findById(Long id, String userName) {
        if (userName == null) {
            return orderRepository.findById(id)
                    .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
        }
        return orderRepository.findByIdAndUserUserName(id, userName)
                .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
    }
    /**
     * Return order list by deliveryStatus.
     * <p>
     * This method returns all order by deliveryStatus (With the passed-in status -1, return all works;
     * with 0, return all by deliveryStatus == false works;
     * and otherwise, it's return all by deliveryStatus == true),
     * with the deliveryStatus and pageable as the input parameter.
     * @param status
     * @param pageable
     * @return order list by deliveryStatus
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<Order> findAllByDeliveryStatus(Integer status, Pageable pageable) {
        if (status == -1) {
            return orderRepository.findAllByDeliveryStatus(null, pageable);
        } else if (status == 0){
            return orderRepository.findAllByDeliveryStatus(false, pageable);
        } else {
            return orderRepository.findAllByDeliveryStatus(true, pageable);
        }
    }
    /**
     * Return order list by deliveryStatus And userName.
     * <p>
     * This method returns all order by deliveryStatus And userName
     * (With the passed-in status -1, return all works;
     * with 0, return all by deliveryStatus == false works;
     * and otherwise, it's return all by deliveryStatus == true),
     * with the deliveryStatus and pageable as the input parameter.
     * @param status
     * @param pageable
     * @return order list by deliveryStatus
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<Order> findAllByDeliveryStatusAndUserName(Integer status, String userName,
                                                          Pageable pageable) {
        if (status == -1) {
            return orderRepository
                    .findByDeliveryStatusAndUserName(null, userName,pageable);
        } else if (status == 0){
            return orderRepository
                    .findByDeliveryStatusAndUserName(false, userName, pageable);
        } else {
            return orderRepository
                    .findByDeliveryStatusAndUserName(true, userName, pageable);
        }
    }

    /**
     * Return order by status and userName.
     * <p>
     * This method returns order by status and userName (With the passed-in status -1, return all works;
     * with 0, return all by status == false works;
     * and otherwise, it's return all by status == true),
     * with the status and pageable as the input parameter.
     * @param status
     * @param userName
     * @return order by status and userName
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if the order cannot be found by id
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Page<Order> myOrder(Integer status, String userName, Pageable pageable) {
        if (status == -1) {
            return orderRepository
                    .findByStatusAndUserUserName(null, userName, pageable);
        } else if (status == 0){
            return orderRepository
                    .findByStatusAndUserUserName(false, userName, pageable);
        } else {
            return orderRepository
                    .findByStatusAndUserUserName(true, userName, pageable);
        }
    }
    /**
     * Add new order and add new orderItem list and delete all cartLineItem.
     * <p>
     * This method is used to add a new order
     * and add new orderItem list
     * and delete all cartLineItem by user and cart.
     * @param userName
     * @param cartId
     * @param dtoRequest
     * @param addressId
     * @return information of order if the add process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not found by userName
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if cart does not found by id
     * @throws AppException(ResponseCode.ACCESS_DENIED) if cart is not owned by the user
     * @throws AppException(ResponseCode.CART_ALREADY_ORDERED) if cart already been ordered
     * @author HoangVu
     * @since 1.0
     */
    @Override
    @Transactional
    public Order create(String userName, Long cartId, OrderDTORequest dtoRequest, Long addressId) {
        User foundUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));

        Cart foundCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ResponseCode.CART_NOT_FOUND));
        if (!foundCart.getUser().getUserName().equals(userName)) {
            throw new RuntimeException(ResponseCode.ACCESS_DENIED.getMessage());
        }

        if (orderRepository.findByCartId(cartId)) {
            throw new AppException(ResponseCode.CART_ALREADY_ORDERED);
        }

        Address foundAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ResponseCode.ADDRESS_NOT_FOUND));
        Order newOrder = modelMapper.map(dtoRequest, Order.class);
        newOrder.setCartId(cartId);
        newOrder.setTotalPrice(foundCart.getTotalPrice());
        newOrder.setUser(foundUser);
        newOrder.setAddress(foundAddress);
        Order savedOrder = orderRepository.save(newOrder);

        List<CartLineItem> cartLineItems = foundCart.getCartLineItems();
        List<OrderItem> orderItems = cartLineItems.stream()
                .filter(cartLineItem -> !cartLineItem.getDeleted())
                .map(cartLineItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setVariantProductId(cartLineItem.getVariantProduct().getId());
                    orderItem.setName(cartLineItem.getVariantProduct().getName());
                    orderItem.setPrice(cartLineItem.getVariantProduct().getPrice());
                    orderItem.setColor(cartLineItem.getVariantProduct().getColor());
                    orderItem.setModelYear(cartLineItem.getVariantProduct().getModelYear());
                    orderItem.setQuantity(cartLineItem.getQuantity());
                    return orderItem;
                }).toList();
        orderItemRepository.saveAll(orderItems);

        cartLineItemRepository.deleteAllActiveItemsByCartId(cartId);

        return savedOrder;
    }
    /**
     * Update order.
     * <p>
     * This method is used to update order by id.
     * @param id
     * @param userName
     * @param dtoRequest
     * @return order information by id if it is updated successfully.
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if order does not found
     * @throws AppException(ResponseCode.IMMUTABLE) if expireDate remains unchanged
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELIVERED)
     * if order already been delivered in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    @Transactional
    public Order updateById(Long id, String userName, OrderDTORequest dtoRequest) {
        Order foundOrder = findById(id, userName);
        if (foundOrder.getDeliveryTime().isEqual(dtoRequest.getDeliveryTime()) &&
                foundOrder.getNote().equals(dtoRequest.getNote())) {
            throw new AppException(ResponseCode.IMMUTABLE);
        }

        if (Boolean.TRUE.equals(foundOrder.getDeliveryStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
        }

        modelMapper.map(dtoRequest, foundOrder);
        return orderRepository.save(foundOrder);
    }
    /**
     * Charge address.
     * <p>
     * This method is used to charge address by id.
     * @param id
     * @param userName
     * @param addressId
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if order does not found
     * @throws AppException(ResponseCode.IMMUTABLE) if address is remains unchanged
     * @author HoangVu
     * @since 1.0
     */
    @Override
    @Transactional
    public void chargeAddress(Long id, String userName, Long addressId) {
        Order foundOrder = findById(id, userName);
        if (foundOrder.getAddress().getId().equals(addressId)) {
            throw new AppException(ResponseCode.IMMUTABLE);
        }
        Address foundAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ResponseCode.ADDRESS_NOT_FOUND));
        foundOrder.setAddress(foundAddress);
        orderRepository.save(foundOrder);
    }
    /**
     * Cancel order by id.
     * <p>
     * This method sets the delivery for order by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND)
     * if order does not exist in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELIVERED)
     * if order already been delivered in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELETED)
     * if order already been deleted in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public void setDeliveryStatus(Long id) {
        Order foundOrder = findById(id, null);
        if (Boolean.TRUE.equals(foundOrder.getDeliveryStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
        }
        if (Boolean.TRUE.equals(foundOrder.getDeleted())) {
            throw  new AppException(ResponseCode.ORDER_ALREADY_DELETED);
        }
        foundOrder.setDeliveryStatus(true);
        orderRepository.save(foundOrder);
    }

    /**
     * Cancel order by id.
     * <p>
     * This method cancels order by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND)
     * if order does not exist in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_CANCELED)
     * if order already been canceled in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELIVERED)
     * if order already been delivered in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public void cancelById(Long id, String userName) {
        Order foundOrder = findById(id, userName);
        if (Boolean.TRUE.equals(foundOrder.getStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_CANCELED);
        }
        if (Boolean.TRUE.equals(foundOrder.getDeliveryStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
        }
        foundOrder.setStatus(true);
        orderRepository.save(foundOrder);
    }
    /**
     * Reactivate order by id.
     * <p>
     * This method reactivates order by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND)
     * if order does not exist in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_REACTIVATED)
     * if order already been reactivated in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public void reactivateById(Long id, String userName) {
        Order foundOrder = findById(id, userName);
        if (Boolean.FALSE.equals(foundOrder.getStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_REACTIVATED);
        }
        foundOrder.setStatus(true);
        orderRepository.save(foundOrder);
    }
    /**
     * Delete order by id.
     * <p>
     * This method deletes order by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND)
     * if order does not exist in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELETED)
     * if order already been deleted in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_DELIVERED)
     * if order already been delivered in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public void deleteById(Long id) {
        Order foundOrder = findById(id, null);
        if (Boolean.TRUE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELETED);
        }
        if (Boolean.TRUE.equals(foundOrder.getDeliveryStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
        }
        foundOrder.setDeleted(true);
        orderRepository.save(foundOrder);
    }
    /**
     * Restore order by id.
     * <p>
     * This method restores order by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND)
     * if order does not exist in the database
     * @throws AppException(ResponseCode.ORDER_ALREADY_RESTORED)
     * if order already been restored in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public void restoreById(Long id) {
        Order foundOrder = findById(id, null);
        if (Boolean.FALSE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_RESTORED);
        }
        foundOrder.setDeleted(false);
        orderRepository.save(foundOrder);
    }
}
