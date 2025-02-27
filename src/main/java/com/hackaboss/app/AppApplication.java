package com.hackaboss.app;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI().info(new Info()
				.title("API de Agencia de vuelos y Hoteles")
				.version("0.0.1")
				.description("La app tiene la funcionalidades de reserva un Hotel en un fecha determinada con un destino, ademas de poder autenticarse a ciertas peticiones como las  cuatro operaciones CRUD." +
						" También tiene la funcionalidad de reservar un vuelo al igual que hotel también podemos realizar las operaciones CRUD."));

	}

}
