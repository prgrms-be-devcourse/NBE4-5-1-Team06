package com.team6.cafe.domain.order.dto;

import java.util.List;

import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeRequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
	@Email
	@NotNull
	private String email;

	@NotNull
	private String address;

	@Min(0)
	private int totalPrice; // 총 가격

	private List<OrderCoffeeRequestDto> orderCoffees; // 주문 커피 목록
}