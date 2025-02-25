package com.team6.cafe.global.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.team6.cafe.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
	private final OrderService orderService;

	// 매일 오후 2시에 실행 (Cron 표현식: "초 분 시 일 월 요일")
	@Scheduled(cron = "0 0 14 * * *")
	public void updateOrdersAtTwoPM() {
		orderService.updatePendingOrdersToShipped();
		System.out.println("오후 2시: 출고되지 않은 주문을 출고 완료 상태로 변경했습니다.");
	}
}
