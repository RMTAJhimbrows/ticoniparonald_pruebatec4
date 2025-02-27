package com.hackaboss.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    /**
     * Configura y proporciona una instancia de ModelMapper como un bean de Spring.
     *
     * ModelMapper es una biblioteca utilizada para mapear objetos entre diferentes tipos de clases (por ejemplo, DTOs a entidades y viceversa).
     * Este bean se puede inyectar en cualquier clase de servicio o componente para facilitar el mapeo de objetos.
     *
     * @return Una instancia de ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
