package com.team6.cafe.domain.coffee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeRequestDto {
	@NotNull
	private String name;  // 커피명
	private int price;  // 가격
	private String image; // 이미지 링크

}
