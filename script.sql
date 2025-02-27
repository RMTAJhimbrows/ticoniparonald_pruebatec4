CREATE DATABASE IF NOT EXISTS SkyLodgeDB;
USE SkyLodgeDB;

DROP TABLE IF EXISTS `hoteles`;
CREATE TABLE `hoteles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_hotel` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `disponible_desde` date DEFAULT NULL,
  `disponible_hasta` date DEFAULT NULL,
  `lugar` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `precio_noche` double DEFAULT NULL,
  `reservado` bit(1) DEFAULT NULL,
  `tipo_habitacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`codigo_hotel`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `reservas_hoteles`;
CREATE TABLE `reservas_hoteles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_personas` int DEFAULT NULL,
  `fecha_entrada` date DEFAULT NULL,
  `fecha_salida` date DEFAULT NULL,
  `monto_total` double DEFAULT NULL,
  `hotel_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`hotel_id`),
  CONSTRAINT `FK_reservas_hoteles_hoteles` FOREIGN KEY (`hotel_id`) REFERENCES `hoteles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `huespedes`;
CREATE TABLE `huespedes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `reserva_hotel_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`reserva_hotel_id`),
  CONSTRAINT `FK_huespedes_reservas_hoteles` FOREIGN KEY (`reserva_hotel_id`) REFERENCES `reservas_hoteles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `vuelos`;
CREATE TABLE `vuelos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_vuelo` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `destino` varchar(255) DEFAULT NULL,
  `fecha_ida` date DEFAULT NULL,
  `fecha_vuelta` date DEFAULT NULL,
  `origen` varchar(255) DEFAULT NULL,
  `precio_por_persona` double DEFAULT NULL,
  `tipo_asiento` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`codigo_vuelo`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `reservas_vuelos`;
CREATE TABLE `reservas_vuelos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_personas` int DEFAULT NULL,
  `destino` varchar(255) DEFAULT NULL,
  `fecha_vuelo` date DEFAULT NULL,
  `monto_total` double DEFAULT NULL,
  `origen` varchar(255) DEFAULT NULL,
  `vuelo_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`vuelo_id`),
  CONSTRAINT `FK_reservas_vuelos_vuelos` FOREIGN KEY (`vuelo_id`) REFERENCES `vuelos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `pasajeros`;
CREATE TABLE `pasajeros` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `reserva_vuelo_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`reserva_vuelo_id`),
  CONSTRAINT `FK_pasajeros_reservas_vuelos` FOREIGN KEY (`reserva_vuelo_id`) REFERENCES `reservas_vuelos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `SkyLodgeDB`.`vuelos`
  (`deleted`, `destino`, `fecha_ida`, `fecha_vuelta`, `codigo_vuelo`, `origen`, `precio_por_persona`, `tipo_asiento`)
VALUES
  (0, 'Bogota',      '2025-05-01', '2025-05-10', 'AVX-128', 'Medellin',    160.5, 'Economy'),
  (0, 'Medellin',    '2025-05-02', '2025-05-11', 'AVX-129', 'Bogota',      170.5, 'Economy'),
  (0, 'Cali',        '2025-05-03', '2025-05-12', 'AVX-130', 'Cartagena',   150.0, 'Economy'),
  (0, 'Cartagena',   '2025-05-04', '2025-05-13', 'AVX-131', 'Cali',        180.0, 'Business'),
  (0, 'Santa Marta', '2025-05-05', '2025-05-14', 'AVX-132', 'Barranquilla',200.0, 'Economy'),
  (0, 'Barranquilla','2025-05-06', '2025-05-15', 'AVX-133', 'Santa Marta', 190.0, 'Economy'),
  (0, 'Bucaramanga', '2025-05-07', '2025-05-16', 'AVX-134', 'Medellin',    165.0, 'Business'),
  (0, 'Pereira',     '2025-05-08', '2025-05-17', 'AVX-135', 'Bogota',      175.0, 'Economy'),
  (0, 'Manizales',   '2025-05-09', '2025-05-18', 'AVX-136', 'Medellin',    155.0, 'Business'),
  (0, 'Ibagué',      '2025-05-10', '2025-05-19', 'AVX-137', 'Cali',        160.0, 'Economy');
  
INSERT INTO `SkyLodgeDB`.`hoteles`
  (`codigo_hotel`, `deleted`, `disponible_desde`, `disponible_hasta`, `lugar`, `nombre`, `precio_noche`, `reservado`, `tipo_habitacion`)
VALUES
  ('HT001', 0, '2025-05-01', '2025-05-10', 'Bogota',      'Hotel Andino',   120.50, 0, 'Individual'),
  ('HT002', 0, '2025-05-02', '2025-05-11', 'Medellin',    'Hotel Paisa',    130.75, 0, 'Doble'),
  ('HT003', 0, '2025-05-03', '2025-05-12', 'Cali',        'Hotel Pacifico', 110.00, 1, 'Triple'),
  ('HT004', 0, '2025-05-04', '2025-05-13', 'Cartagena',   'Hotel Caribe',   150.00, 0, 'Individual'),
  ('HT005', 0, '2025-05-05', '2025-05-14', 'Santa Marta', 'Hotel del Mar',  140.00, 1, 'Doble'),
  ('HT006', 0, '2025-05-06', '2025-05-15', 'Barranquilla','Hotel Tropical', 125.50, 0, 'Triple'),
  ('HT007', 0, '2025-05-07', '2025-05-16', 'Bucaramanga', 'Hotel Montaña',  115.25, 0, 'Individual'),
  ('HT008', 0, '2025-05-08', '2025-05-17', 'Pereira',     'Hotel Central',  135.00, 1, 'Doble'),
  ('HT009', 0, '2025-05-09', '2025-05-18', 'Manizales',   'Hotel Vista',    145.75, 0, 'Triple'),
  ('HT010', 0, '2025-05-10', '2025-05-19', 'Ibagué',      'Hotel Royal',    155.00, 0, 'Individual');