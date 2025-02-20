package com.team6.cafe.domain.example.dto;

import com.team6.cafe.domain.example.entity.Example;
import com.team6.cafe.domain.example.entity.Type;

public record CreateExampleResponse(
	Long id,
	Type type
) {
	public static CreateExampleResponse from(Example e) {
		return new CreateExampleResponse(e.getId(), e.getType());
	}
}
