package com.team6.cafe.domain.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team6.cafe.domain.coffee.entity.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
