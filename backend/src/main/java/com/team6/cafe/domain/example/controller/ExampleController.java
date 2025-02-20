package com.team6.cafe.domain.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.cafe.domain.example.dto.CreateExampleRequest;
import com.team6.cafe.domain.example.dto.CreateExampleResponse;
import com.team6.cafe.domain.example.service.ExampleService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleController {

	private final ExampleService exampleService;

	@Operation(summary = "Example 생성")
	@PostMapping("/create")
	public ResponseEntity<CreateExampleResponse> create(@RequestBody @Valid CreateExampleRequest request) {
		CreateExampleResponse response = exampleService.create(request);

		return ResponseEntity.ok(response);
	}
}
