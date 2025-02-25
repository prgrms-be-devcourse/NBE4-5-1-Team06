package com.team6.cafe.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.cafe.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByStatusFalse();
	List<Order> findByEmailAndStatusFalse(String email);
}
