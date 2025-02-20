package com.team6.cafe.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;

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

	public static OrderResponseDto from(Order order) {
		return new OrderResponseDto(); // 미구현
	}
}
