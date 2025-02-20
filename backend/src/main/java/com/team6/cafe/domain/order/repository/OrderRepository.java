package com.team6.cafe.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.cafe.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
