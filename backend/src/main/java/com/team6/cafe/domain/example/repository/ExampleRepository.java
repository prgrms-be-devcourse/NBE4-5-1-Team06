package com.team6.cafe.domain.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.cafe.domain.example.entity.Example;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}
