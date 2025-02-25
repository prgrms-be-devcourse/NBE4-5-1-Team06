package com.team6.cafe.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderSearchResponseDto {
    private Long orderId;
    private String customerEmail;
    private LocalDateTime orderTime;
    private LocalDateTime modifyTime;
    private boolean status;
    private String deliveryAddress;
    private int totalPrice;
    private List<OrderCoffeeInfoDto> coffeeItems;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderCoffeeInfoDto {
        private Long orderCoffeeId;
        private Long coffeeId;
        private String coffeeName;
        private int coffeePrice;
        private int quantity;
    }
}