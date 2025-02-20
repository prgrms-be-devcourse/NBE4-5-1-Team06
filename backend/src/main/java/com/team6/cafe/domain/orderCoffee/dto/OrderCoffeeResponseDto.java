package com.team6.cafe.domain.orderCoffee.dto;

import com.team6.cafe.domain.coffee.dto.CoffeeResponseDto;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCoffeeResponseDto {
	private Long id;
	private CoffeeResponseDto coffee; // 커피 정보 포함
	private int quantity;
}
