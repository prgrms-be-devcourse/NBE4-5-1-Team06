package com.team6.cafe.domain.example.dto;

import com.team6.cafe.domain.example.entity.Example;
import com.team6.cafe.domain.example.entity.Type;

import jakarta.validation.constraints.NotNull;

public record CreateExampleRequest(
	@NotNull
	String field,
	Type type
) {
}
