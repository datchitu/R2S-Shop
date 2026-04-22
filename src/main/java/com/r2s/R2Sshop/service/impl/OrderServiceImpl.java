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
     * @throws AppException(ResponseCode.ACCESS_DENIED) if orderItem is not owned by the user
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Order findById(Long id, String userName) {
        Order foundOrder = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
        if (!foundOrder.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        return foundOrder;
    }
    /**
     * Return order by id.
     * <p>
     * This method returns order by id, with the id as the input parameter.
     * @param id
     * @return order by id
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if the order cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Order findByIdWithoutAuthor(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
    }
    /**
     * Return order by status and userName.
     * <p>
     * This method returns order by status and userName, with the status and userName as the input parameter.
     * @param status
     * @param userName
     * @return order by status and userName
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if the order cannot be found by id
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Order myOrder(Integer status, String userName) {
        if (status == 0) {
            return orderRepository.findByStatusAndUserUserName(false, userName)
                    .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
        } else {
            return orderRepository.findByStatusAndUserUserName(true, userName)
                    .orElseThrow(() -> new AppException(ResponseCode.ORDER_NOT_FOUND));
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
     * @throws AppException(ResponseCode.ACCESS_DENIED) if order is not owned by the user
     * @author HoangVu
     * @since 1.0
     */
    @Override
    @Transactional
    public Order updateById(Long id, String userName, OrderDTORequest dtoRequest) {
        Order foundOrder = findById(id, userName);
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
     * @throws AppException(ResponseCode.ACCESS_DENIED) if order is not owned by the user
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
     * @since 1.1
     */
    @Override
    public void setStatusDelivery(Long id) {
        Order foundOrder = findByIdWithoutAuthor(id);
        if (Boolean.TRUE.equals(foundOrder.getStatusDelivery())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
        }
        if (Boolean.TRUE.equals(foundOrder.getDeleted())) {

        }
        foundOrder.setStatusDelivery(true);
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
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public void cancel(Long id, String userName) {
        Order foundOrder = findById(id, userName);
        if (Boolean.TRUE.equals(foundOrder.getStatus())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_CANCELED);
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
     * @since 1.1
     */
    @Override
    public void deleteById(Long id) {
        Order foundOrder = findByIdWithoutAuthor(id);
        if (Boolean.TRUE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELETED);
        }
        if (Boolean.TRUE.equals(foundOrder.getStatusDelivery())) {
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
     * @since 1.1
     */
    @Override
    public void restoreById(Long id) {
        Order foundOrder = findByIdWithoutAuthor(id);
        if (Boolean.FALSE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_RESTORED);
        }
        foundOrder.setDeleted(false);
        orderRepository.save(foundOrder);
    }
}
