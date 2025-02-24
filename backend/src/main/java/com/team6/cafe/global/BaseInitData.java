package com.team6.cafe.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import com.team6.cafe.domain.coffee.dto.CoffeeRequestDto;
import com.team6.cafe.domain.coffee.service.CoffeeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("build")
@Configuration
@RequiredArgsConstructor
public class BaseInitData {
	private final CoffeeService coffeeService;

	@Autowired
	@Lazy
	private BaseInitData self;

	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			self.coffeeInit();
		};
	}

	@Transactional
	public void coffeeInit() {
		if(coffeeService.count() > 0){
			return;
		}

		coffeeService.create(new CoffeeRequestDto("Columbia Nariño", 5000, "https://cdn.pixabay.com/photo/2017/09/04/18/39/coffee-2714970_960_720.jpg"));
		coffeeService.create(new CoffeeRequestDto("Brazil Serra do Caparaó", 6000, "https://cdn.pixabay.com/photo/2016/01/02/04/59/coffee-1117933_1280.jpg"));
		coffeeService.create(new CoffeeRequestDto("Columbia Quindio(White Wine Extended Fermentation)", 7000, "https://cdn.pixabay.com/photo/2022/11/01/05/18/coffee-7561288_1280.jpg"));
		coffeeService.create(new CoffeeRequestDto("Ethiopia Sidamo", 8000, "https://cdn.pixabay.com/photo/2015/05/31/10/54/coffee-791045_1280.jpg"));
	}
}
