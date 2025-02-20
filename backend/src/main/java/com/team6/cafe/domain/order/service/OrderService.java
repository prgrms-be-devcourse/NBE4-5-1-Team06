package com.team6.cafe.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.coffee.entity.Coffee;
import com.team6.cafe.domain.coffee.repository.CoffeeRepository;
import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.order.repository.OrderRepository;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;
import com.team6.cafe.domain.orderCoffee.repository.OrderCoffeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final CoffeeRepository coffeeRepository;
	private final OrderCoffeeRepository orderCoffeeRepository;

	@Transactional
	public OrderResponseDto create(OrderRequestDto orderRequestDto) {
		Order order = Order.builder()
			.email(orderRequestDto.getEmail())
			.address(orderRequestDto.getAddress())
			.totalPrice(orderRequestDto.getTotalPrice())
			.orderTime(LocalDateTime.now())
			.modifyTime(LocalDateTime.now())
			.status(false) // 기본값: 배송 전
			.build();

		Order savedOrder = orderRepository.save(order);

		List<OrderCoffee> orderCoffees = orderRequestDto.getOrderCoffees().stream()
			.map(dto -> {
				Coffee coffee = coffeeRepository.findById(dto.getCoffeeId())
					.orElseThrow(() -> new IllegalArgumentException("해당 커피를 찾을 수 없습니다: " + dto.getCoffeeId()));

				return OrderCoffee.builder()
					.order(savedOrder)
					.coffee(coffee)
					.quantity(dto.getQuantity())
					.build();
			})
			.collect(Collectors.toList());

		orderCoffeeRepository.saveAll(orderCoffees);

		return OrderResponseDto.from(savedOrder, orderCoffees);
	}
}
