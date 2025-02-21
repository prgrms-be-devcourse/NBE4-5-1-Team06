package com.team6.cafe.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
	private Long id;
	private String email;
	private LocalDateTime orderTime;
	private LocalDateTime modifyTime;
	private boolean status;
	private String address;
	private int totalPrice;
	private List<OrderCoffeeResponseDto> orderCoffees;

	public static OrderResponseDto from(Order order, List<OrderCoffee> orderCoffees) {
		List<OrderCoffeeResponseDto> orderCoffeeDtos = orderCoffees.stream()
			.map(OrderCoffeeResponseDto::from)
			.collect(Collectors.toList());

		return new OrderResponseDto(
			order.getId(),
			order.getEmail(),
			order.getOrderTime(),
			order.getModifyTime(),
			order.isStatus(),
			order.getAddress(),
			order.getTotalPrice(),
			orderCoffeeDtos
		);
	}
}
