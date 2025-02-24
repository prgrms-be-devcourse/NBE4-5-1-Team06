package com.team6.cafe.domain.coffee.entity;

import org.hibernate.annotations.SoftDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@Table(name = "coffee")
public class Coffee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;  // 커피명

	private int price;  // 가격

	private String image; // 이미지 링크

	@Builder
	public Coffee(Long id, String name, int price, String image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
	}

	public void update(int price) {
		this.price = price;
	}
}
