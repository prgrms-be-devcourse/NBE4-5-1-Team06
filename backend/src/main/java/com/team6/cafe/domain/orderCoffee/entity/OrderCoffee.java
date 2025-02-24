package com.team6.cafe.domain.orderCoffee.entity;

import org.hibernate.annotations.SoftDelete;

import com.team6.cafe.domain.coffee.entity.Coffee;
import com.team6.cafe.domain.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "orderCoffee")
public class OrderCoffee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "coffee_id", nullable = false)
	private Coffee coffee;

	private int quantity; // 커피 수량

	@Column(insertable = false, updatable = false)
	private boolean deleted; // 삭제 유무

	@Builder
	public OrderCoffee(Order order, Coffee coffee, int quantity, boolean deleted) {
		this.order = order;
		this.coffee = coffee;
		this.quantity = quantity;
		this.deleted = deleted;
	}

	public void updateQuantity(Integer quantity) {
		if (quantity != null) {
			this.quantity = quantity;
		}
	}
}

