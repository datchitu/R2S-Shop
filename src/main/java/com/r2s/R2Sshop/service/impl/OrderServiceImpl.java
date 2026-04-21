package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Order;
import com.r2s.R2Sshop.repository.OrderRepository;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

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
    public Order findById(Long id) {
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

    @Override
    public Order add(OrderDTORequest dtoRequest) {
        return null;
    }

    @Override
    public Order updateById(Long id, OrderDTORequest dtoRequest) {
        return null;
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
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void setStatusDelivery(Long id) {
        Order foundOrder = findById(id);
        if (Boolean.TRUE.equals(foundOrder.getStatusDelivery())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELIVERED);
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
     * @since 1.0
     */
    @Override
    public void cancel(Long id) {
        Order foundOrder = findById(id);
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
     * @since 1.0
     */
    @Override
    public void reactivateById(Long id) {
        Order foundOrder = findById(id);
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
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void deleteById(Long id) {
        Order foundOrder = findById(id);
        if (Boolean.TRUE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_DELETED);
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
     * @since 1.0
     */
    @Override
    public void restoreById(Long id) {
        Order foundOrder = findById(id);
        if (Boolean.FALSE.equals(foundOrder.getDeleted())) {
            throw new AppException(ResponseCode.ORDER_ALREADY_RESTORED);
        }
        foundOrder.setDeleted(false);
        orderRepository.save(foundOrder);
    }
}
