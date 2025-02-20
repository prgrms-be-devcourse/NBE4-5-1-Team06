package com.team6.cafe.domain.coffee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.cafe.domain.coffee.dto.CoffeeRequestDto;
import com.team6.cafe.domain.coffee.dto.CoffeeResponseDto;
import com.team6.cafe.domain.coffee.service.CoffeeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coffee")
@RequiredArgsConstructor
public class CoffeeController {

	private final CoffeeService coffeeService;

	@Operation(summary = "Example 생성")
	@PostMapping("/create")
	public ResponseEntity<CoffeeResponseDto> create(@RequestBody @Valid CoffeeRequestDto request) {
		CoffeeResponseDto response = coffeeService.create(request);
		return ResponseEntity.ok(response);
	}
}
