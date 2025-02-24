package com.team6.cafe.domain.coffee.service;

import java.util.List;
import java.util.stream.Collectors;

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

	// 커피 전체 조회
	@Transactional(readOnly = true)
	public List<CoffeeResponseDto> getAllCoffees() {
		List<Coffee> coffeeList = coffeeRepository.findAll();
		return coffeeList.stream()
			.map(CoffeeResponseDto::from)
			.collect(Collectors.toList());
	}

	// 특정 커피 조회
	@Transactional(readOnly = true)
	public CoffeeResponseDto getCoffeeById(Long id) {
		Coffee coffee = coffeeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 커피가 존재하지 않습니다. ID: " + id));
		return CoffeeResponseDto.from(coffee);
	}

	// 커피 생성
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

	// 커피 수정
	@Transactional
	public CoffeeResponseDto updateCoffee(Long id, CoffeeRequestDto requestDto) {
		Coffee coffee = coffeeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 커피가 존재하지 않습니다. ID: " + id));

		coffee.update(requestDto.getName(), requestDto.getPrice(), requestDto.getImage());
		return CoffeeResponseDto.from(coffee);
	}

	// 커피 삭제
	@Transactional
	public void deleteCoffee(Long id) {
		Coffee coffee = coffeeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 커피가 존재하지 않습니다. ID: " + id));

		coffeeRepository.delete(coffee);
	}

	public long count() {
		return coffeeRepository.count();
	}
}
