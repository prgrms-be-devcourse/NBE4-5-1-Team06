package com.team6.cafe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import com.team6.cafe.domain.order.dto.OrderRequestDto;
import com.team6.cafe.domain.order.dto.OrderResponseDto;
import com.team6.cafe.domain.order.service.OrderService;
import com.team6.cafe.domain.orderCoffee.dto.OrderCoffeeRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.MockMvc;

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
}
