package com.team6.cafe.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.coffee.entity.Coffee;
import com.team6.cafe.domain.coffee.repository.CoffeeRepository;
import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto.CoffeeDto;
import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.order.repository.OrderRepository;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;
import com.team6.cafe.domain.orderCoffee.repository.OrderCoffeeRepository;
import com.team6.cafe.global.common.exception.BusinessException;
import com.team6.cafe.global.common.response.ErrorCode;

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

	@Transactional
	public OrderResponseDto update(Long orderId, OrderUpdateRequestDto request) {
		String email = request.email();

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.isMyOrder(email)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}

		Map<Pair<Long, Long>, OrderCoffee> orderCoffeeMap = getOrderCoffeeMap(order);

		updateQuantities(orderCoffeeMap, orderId, request.coffees());
		updatePrices(order);

		return OrderResponseDto.from(order, order.getOrderCoffees());
	}

	private void updatePrices(Order order) {
		order.updateTotalPrice();
	}

	private void updateQuantities(Map<Pair<Long, Long>, OrderCoffee> orderCoffeeMap, Long orderId,
		List<CoffeeDto> coffees) {
		coffees.forEach(
			coffee -> {
				Long coffeeId = coffee.coffeeId();
				Integer newQuantity = coffee.quantity();

				OrderCoffee orderCoffee = orderCoffeeMap.get(Pair.of(orderId, coffeeId));

				if (orderCoffee != null) {
					orderCoffee.updateQuantity(newQuantity);
				}
			}
		);
	}

	@Transactional(readOnly = true)
	Map<Pair<Long, Long>, OrderCoffee> getOrderCoffeeMap(Order order) {
		return Stream.of(order)
			.flatMap(o -> o.getOrderCoffees().stream()
				.map(oc -> Pair.of(
					Pair.of(o.getId(), oc.getCoffee().getId()),
					oc)
				)
			)
			.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}

	@Transactional
	public OrderResponseDto delete(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

		orderRepository.delete(order);

		OrderResponseDto responseDto = OrderResponseDto.from(order, order.getOrderCoffees());
		responseDto.setDeleted(true);

		return responseDto;
  }
  
	public void updatePendingOrdersToShipped() {
		List<Order> pendingOrders = orderRepository.findByStatusFalse();

		for (Order order : pendingOrders) {
			order.updateStatusToShipped();  // 상태 변경
		}
	}

	public Long count() {
		return orderCoffeeRepository.count();
	}
}
