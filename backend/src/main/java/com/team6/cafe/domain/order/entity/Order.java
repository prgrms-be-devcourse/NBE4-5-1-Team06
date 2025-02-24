package com.team6.cafe.domain.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SoftDelete;

import com.team6.cafe.domain.orderCoffee.entity.OrderCoffee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private LocalDateTime orderTime;  // 주문 일시

	private LocalDateTime modifyTime; // 수정 일시

	private boolean status; // 출고 여부 (true: 출고 완료, false: 출고 전)

	private String address; // 주소

	private int totalPrice; // 총 가격

	@Column(insertable = false, updatable = false)
	private boolean deleted; // 삭제 유무

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderCoffee> orderCoffees; // 주문한 커피 목록

	@Builder
	public Order(String email, LocalDateTime orderTime, LocalDateTime modifyTime, boolean status, String address, int totalPrice, boolean deleted) {
		this.email = email;
		this.orderTime = orderTime;
		this.modifyTime = modifyTime;
		this.status = status;
		this.address = address;
		this.totalPrice = totalPrice;
		this.deleted = deleted;
		this.orderCoffees = new ArrayList<>();
	}

	public boolean isMyOrder(String email) {
		return this.email.equals(email);
	}

	public void updateTotalPrice() {
		totalPrice = orderCoffees.stream()
			.mapToInt(orderCoffee -> orderCoffee.getQuantity() * orderCoffee.getCoffee().getPrice())
			.sum();

		modifyTime = LocalDateTime.now();
	}

	public void deleteOrder() {
		this.deleted = true;

		if (orderCoffees != null) {
			orderCoffees.forEach(orderCoffee -> orderCoffee.setDeleted(true));
		}
	}
}
