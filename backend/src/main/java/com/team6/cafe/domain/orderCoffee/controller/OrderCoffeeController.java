package com.team6.cafe.domain.orderCoffee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeRequestDto;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;
import com.team6.cafe.domain.orderCoffee.service.OrderCoffeeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class OrderCoffeeController {
	private final OrderCoffeeService orderCoffeeService;
}
