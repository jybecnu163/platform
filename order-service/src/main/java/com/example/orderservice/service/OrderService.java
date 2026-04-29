package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setId(null);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = findOrderById(id);
        existingOrder.setProductName(orderDetails.getProductName());
        existingOrder.setQuantity(orderDetails.getQuantity());
        existingOrder.setPrice(orderDetails.getPrice());
        return orderRepository.save(existingOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}