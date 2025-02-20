package com.team6.cafe.domain.orderCoffee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeRequestDto;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;
import com.team6.cafe.domain.orderCoffee.repository.OrderCoffeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderCoffeeService {
	private final OrderCoffeeRepository orderCoffeeRepository;
}
