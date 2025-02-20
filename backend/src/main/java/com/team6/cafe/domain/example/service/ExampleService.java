package com.team6.cafe.domain.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.example.dto.CreateExampleRequest;
import com.team6.cafe.domain.example.dto.CreateExampleResponse;
import com.team6.cafe.domain.example.entity.Example;
import com.team6.cafe.domain.example.repository.ExampleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExampleService {

	private final ExampleRepository exampleRepository;

	@Transactional
	public CreateExampleResponse create(CreateExampleRequest request) {
		Example example = exampleRepository.save(
			Example.builder()
				.field(request.field())
				.type(request.type())
				.build()
		);

		return CreateExampleResponse.from(example);
	}
}
