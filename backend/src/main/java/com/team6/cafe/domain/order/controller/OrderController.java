package com.team6.cafe.domain.order.controller;

import com.team6.cafe.domain.order.dto.OrderSearchResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto;
import com.team6.cafe.domain.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

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

	@Operation(summary = "주문 조회")
	@GetMapping(value = "/{email}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Map<String, List<OrderSearchResponseDto>>> getOrdersByEmail(@PathVariable String email) {
		List<OrderSearchResponseDto> response = orderService.getOrdersByEmail(email);
		Map<String, List<OrderSearchResponseDto>> responseBody = Map.of("orders", response);
		return ResponseEntity.ok()
				.header("Content-Type", "application/json; charset=UTF-8")
				.body(responseBody);
	}

	@Operation(summary = "주문 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<OrderResponseDto> update(
		@PathVariable Long id,
		@RequestBody @Valid OrderUpdateRequestDto request
	) {
		return ResponseEntity.ok(orderService.update(id, request));
	}

	@Operation(summary = "주문 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<OrderResponseDto> delete(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.delete(id));
	}
}
