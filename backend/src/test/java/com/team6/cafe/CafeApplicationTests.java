package com.team6.cafe;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto;
import com.team6.cafe.domain.order.dto.OrderUpdateRequestDto.CoffeeDto;
import com.team6.cafe.domain.order.service.OrderService;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeRequestDto;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
class CafeApplicationTests {
	@Autowired
	private OrderService orderService;

	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
	}

	private void initData() {
		String email = "test@gmail.com";
		String address = "주소";

		OrderRequestDto request = new OrderRequestDto(
			email,
			address,
			1000,
			List.of(
				new OrderCoffeeRequestDto(1L, 1L, 1),  // coffeeId=1, quantity=1
				new OrderCoffeeRequestDto(2L, 1L, 2),  // coffeeId=2, quantity=2
				new OrderCoffeeRequestDto(3L, 1L, 3)   // coffeeId=3, quantity=3
			)
		);

		orderService.create(request);
	}

	@Test
	@DisplayName("주문 생성 테스트")
	void createTest() throws Exception {
		OrderRequestDto requestDto = new OrderRequestDto("example@team6.com", "Seoul", 20000, List.of(
			new OrderCoffeeRequestDto(1L, 2),
			new OrderCoffeeRequestDto(2L, 1)
		));

		OrderResponseDto responseDto = new OrderResponseDto();
		responseDto.setId(1L);
		responseDto.setEmail("example@team6.com");
		responseDto.setAddress("Seoul");
		responseDto.setTotalPrice(20000);

		mvc.perform(post("/api/order/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
                        {
                            "email": "example@team6.com",
                            "address": "Seoul",
                            "totalPrice": 20000,
                            "orderCoffees": [ {"coffeeId": 1, "quantity": 2}, { "coffeeId": 2, "quantity": 1 } ]
                        }
                    """))
			.andExpect(status().isOk())  // HTTP 상태 코드 201 확인
			.andExpect(jsonPath("$.email").value("example@team6.com"))  // 이메일 값 검증
			.andExpect(jsonPath("$.totalPrice").value(20000))  // 총 가격 검증
			.andExpect(jsonPath("$.orderCoffees[0].coffee.id").value(1))  // 커피 ID 검증
			.andExpect(jsonPath("$.orderCoffees[0].coffee.name").value("Columbia Nariño"))  // 커피 이름 검증
			.andDo(print());
	}

	@Test
	@DisplayName("주문 수정 테스트")
	void updateTest() {
		initData();

		/* Given */
		String email = "test@gmail.com";
		Long orderId = 1L;
		Long coffeeId1 = 1L;
		Long coffeeId2 = 2L;

		OrderUpdateRequestDto request = new OrderUpdateRequestDto(
			email,
			List.of(
				new CoffeeDto(coffeeId1, 5),
				new CoffeeDto(coffeeId2, 6)
			)
		);

		/* When */
		OrderResponseDto response = orderService.update(orderId, request);

		/* Then */
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(orderId);
		assertThat(response.getEmail()).isEqualTo(email);

		// 총 가격 검증
		Integer expectedTotalPrice = 82000;
		Integer actualTotalPrice = response.getTotalPrice();
		assertThat(actualTotalPrice).isEqualTo(expectedTotalPrice);

		// 수량 검증
		List<Integer> quantities = response.getOrderCoffees()
			.stream()
			.map(OrderCoffeeResponseDto::getQuantity)
			.collect(Collectors.toList());
		assertThat(quantities).containsExactlyInAnyOrder(5, 6, 3);
	}
}
