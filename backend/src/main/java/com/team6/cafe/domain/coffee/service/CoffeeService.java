package com.team6.cafe.domain.coffee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.coffee.dto.CoffeeRequestDto;
import com.team6.cafe.domain.coffee.dto.CoffeeResponseDto;
import com.team6.cafe.domain.coffee.entity.Coffee;
import com.team6.cafe.domain.coffee.repository.CoffeeRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeService {

	private final CoffeeRepository coffeeRepository;

	@Transactional
	public CoffeeResponseDto create(@Valid CoffeeRequestDto request) {
		Coffee coffee = coffeeRepository.save(
			Coffee.builder()
				.name(request.getName())
				.price(request.getPrice())
				.image(request.getImage())
				.build()
		);

		return CoffeeResponseDto.from(coffee);
	}
}
