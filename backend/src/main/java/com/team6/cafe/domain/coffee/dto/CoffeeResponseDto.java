package com.team6.cafe.domain.coffee.dto;

import com.team6.cafe.domain.coffee.entity.Coffee;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeResponseDto {
	private Long id; // 아이디
	private String name; // 커피명
	private int price; // 가격

	public static CoffeeResponseDto from(Coffee coffee) {
		return new CoffeeResponseDto(coffee.getId(), coffee.getName(), coffee.getPrice());
	}
}
