package com.example.demo.repository;

import com.example.demo.model.CustomerOrder;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserOrderByCreatedAtDesc(User user);
    List<CustomerOrder> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    List<CustomerOrder> findAllByOrderByCreatedAtDesc();
}
