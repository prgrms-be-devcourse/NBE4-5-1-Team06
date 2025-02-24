package com.team6.cafe.domain.coffee.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.cafe.domain.coffee.dto.CoffeeRequestDto;
import com.team6.cafe.domain.coffee.dto.CoffeeResponseDto;
import com.team6.cafe.domain.coffee.dto.CoffeeUpdateRequestDto;
import com.team6.cafe.domain.coffee.service.CoffeeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coffee")
@RequiredArgsConstructor
public class CoffeeController {

	private final CoffeeService coffeeService;

	@Operation(summary = "커피 생성")
	@PostMapping("/create")
	public ResponseEntity<CoffeeResponseDto> createCoffee(@RequestBody @Valid CoffeeRequestDto request) {
		CoffeeResponseDto response = coffeeService.create(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "커피 가격 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<CoffeeResponseDto> modifyCoffee(
		@PathVariable Long id,
		@RequestBody @Valid CoffeeUpdateRequestDto request) {

		CoffeeResponseDto updatedCoffee = coffeeService.updateCoffee(id, request);
		return ResponseEntity.ok(updatedCoffee);
	}


	@Operation(summary = "커피 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCoffee(@PathVariable Long id) {
		coffeeService.deleteCoffee(id);
		return ResponseEntity.ok("삭제 완료");
	}

	@Operation(summary = "커피 전체 조회")
	@GetMapping
	public ResponseEntity<List<CoffeeResponseDto>> getCoffees() {
		return ResponseEntity.ok(coffeeService.getAllCoffees());
	}
}
