package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Order;
import com.r2s.R2Sshop.model.OrderItem;
import com.r2s.R2Sshop.repository.OrderItemRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.OrderItemService;
import com.r2s.R2Sshop.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderService orderService;

    /**
     * Return order item by id.
     * <p>
     * This method returns order item by id, with the id as the input parameter.
     * @param id
     * @return order item by id
     * @throws AppException(ResponseCode.ORDER_ITEM_NOT_FOUND) if the order item cannot be found by id
     * @throws AppException(ResponseCode.ACCESS_DENIED) if orderItem is not owned by the user
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public OrderItem findById(Long id, String userName) {
        OrderItem foundOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.ORDER_ITEM_NOT_FOUND));
        if (!foundOrderItem.getOrder().getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        return foundOrderItem;
    }
    /**
     * Return order item list by orderId.
     * <p>
     * This method returns order item by orderId and pagination is applied,
     * with the orderId and pageable as the input parameter.
     * @param orderId
     * @param pageable
     * @return order item list by orderId
     * @throws AppException(ResponseCode.ORDER_NOT_FOUND) if the order cannot be found by id
     * @throws AppException(ResponseCode.ACCESS_DENIED) if orderItem is not owned by the user
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable, String userName) {
        Order foundOrder = orderService.findById(orderId, userName);
        if (!foundOrder.getUser().getUserName().equals(userName)) {
            throw new AppException(ResponseCode.ACCESS_DENIED);
        }
        return orderItemRepository.findAllByOrderId(orderId, pageable);
    }
}
