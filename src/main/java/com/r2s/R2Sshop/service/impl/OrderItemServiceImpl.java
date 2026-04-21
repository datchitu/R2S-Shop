package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.OrderItemDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.OrderItem;
import com.r2s.R2Sshop.repository.OrderItemRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * Return order item by id.
     * <p>
     * This method returns order item by id, with the id as the input parameter.
     * @param id
     * @return order item by id
     * @throws AppException(ResponseCode.ORDER_ITEM_NOT_FOUND) if the order item cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.ORDER_ITEM_NOT_FOUND));
    }
    /**
     * Return order item list by orderId.
     * <p>
     * This method returns order item by orderId and pagination is applied,
     * with the orderId and pageable as the input parameter.
     * @param orderId
     * @param pageable
     * @return order item list by orderId
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(orderId, pageable);
    }

    @Override
    public OrderItem addByOrderId(Long orderId, OrderItemDTORequest dtoRequest) {
        return null;
    }
}
