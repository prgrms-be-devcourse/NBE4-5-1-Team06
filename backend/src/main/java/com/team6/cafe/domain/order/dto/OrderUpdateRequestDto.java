package com.team6.cafe.domain.order.dto;

import java.util.List;

public record OrderUpdateRequestDto(
	String email,
	List<CoffeeDto> coffees
) {
	public record CoffeeDto(Long coffeeId, Integer quantity) {
	}
}
