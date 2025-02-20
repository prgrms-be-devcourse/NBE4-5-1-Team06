package com.team6.cafe.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.cafe.domain.coffee.dto.CoffeeResponseDto;
import com.team6.cafe.domain.coffee.entity.Coffee;
import com.team6.cafe.domain.coffee.repository.CoffeeRepository;
import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.entity.Order;
import com.team6.cafe.domain.order.repository.OrderRepository;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;
import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;
import com.team6.cafe.domain.orderCoffee.repository.OrderCoffeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

}
