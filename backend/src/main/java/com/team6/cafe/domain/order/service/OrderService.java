package com.team6.cafe.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponseDto create(OrderRequestDto request) {
		Order order = orderRepository.save(
			Order.builder()
				.email(request.getEmail())
				.address(request.getAddress())
				.build()
		);

		return OrderResponseDto.from(order); // 미구현
	}
}
