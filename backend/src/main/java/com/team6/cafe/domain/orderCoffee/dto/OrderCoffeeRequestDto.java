package com.team6.cafe.domain.orderCoffee.dto;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCoffeeRequestDto {
	@NotNull
	private Long coffeeId; // 커피 id

	@NotNull
	private Long orderId; // 주문 id

	@Min(1)
	private int quantity; // 최소수량 1개
}
