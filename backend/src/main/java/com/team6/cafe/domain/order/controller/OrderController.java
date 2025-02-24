package com.team6.cafe.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto;
import com.team6.cafe.domain.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@Operation(summary = "주문 생성")
	@PostMapping("/create")
	public ResponseEntity<OrderResponseDto> create(@RequestBody @Valid OrderRequestDto request) {
		OrderResponseDto response = orderService.create(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "주문 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<OrderResponseDto> update(
		@PathVariable Long id,
		@RequestBody @Valid OrderUpdateRequestDto request
	) {
		return ResponseEntity.ok(orderService.update(id, request));
	}
}
