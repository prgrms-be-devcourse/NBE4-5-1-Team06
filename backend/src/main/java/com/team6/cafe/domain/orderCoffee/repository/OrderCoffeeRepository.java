package com.team6.cafe.domain.orderCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;

public interface OrderCoffeeRepository extends JpaRepository<OrderCoffee, Long> {
}
