package com.team6.cafe.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderSearchResponseDto {
    private Long id;
    private String email;
    private LocalDateTime orderTime;
    private LocalDateTime modifyTime;
    private boolean status;
    private String address;
    private int totalPrice;
    private List<OrderCoffeeInfoDto> orderCoffees;

    @Getter
    @Builder
    public static class OrderCoffeeInfoDto {
        private Long id;
        private CoffeeInfoDto coffee;
        private int quantity;

        @Getter
        @Builder
        public static class CoffeeInfoDto {
            private Long id;
            private String name;
            private int price;
        }
    }
}