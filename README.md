Resumen de la Solución

Esta aplicación simula la gestión de reservas para una agencia de turismo, abarcando tanto hoteles como vuelos y sus respectivas reservas. Está desarrollada con Java 17, Spring Boot, Spring JPA y Lombok. Se implementaron capas separadas para:

Entidades y DTOs:
Las entidades usan nombres en español y los DTOs en inglés. Esto facilita la distinción de los atributos en cada capa y mejora la mantenibilidad del código.

Servicios y Repositorios:
Cada servicio inyecta solo un repositorio, siguiendo buenas prácticas para mantener una arquitectura limpia y modular.

Controladores:
Se han definido endpoints para listar, buscar y realizar reservas, así como para operaciones CRUD de administración, cumpliendo las historias de usuario definidas.

Manejo de Excepciones:
Se implementó un controlador global de excepciones que utiliza excepciones personalizadas y un ExceptionDTO para entregar mensajes de error estandarizados.

Esta solución es ideal para pruebas y demostraciones, ya que combina una estructura modular con funcionalidades completas y una buena separación de responsabilidades.



Relaciones entre las Tablas (Hoteles y reservas)

La base de datos SkyLodgeDB está hecha para manejar hoteles, vuelos, reservas y las personas que están en ellas (huéspedes y pasajeros). Aquí te cuento cómo se conectan las tablas y por qué están organizadas así:

Relación: Un hotel puede tener muchas reservas, y esas reservas se guardan en la tabla reservas_hoteles.

Relación: Cada reserva está ligada a un solo hotel (por eso usamos hotel_id).

Relación: Cada huésped está asociado a una reserva de hotel. Una reserva puede tener varios huéspedes (por ejemplo, una familia), pero cada huésped pertenece a una sola reserva.

Relaciones entre las Tablas(Vuelos y reservas de vuelos)

Relación: Un vuelo puede tener muchas reservas, y esas reservas se guardan en la tabla reservas_vuelos.

Relación: Cada reserva está ligada a un solo vuelo (por eso usamos vuelo_id).

Relación: Cada pasajero está asociado a una reserva de vuelo. Una reserva puede tener varios pasajeros (por ejemplo, un grupo de amigos), pero cada pasajero pertenece a una sola reserva.

Ejemplo de cómo funciona

Alguien busca un hotel en "Medellín" para ciertas fechas.

El sistema busca en la tabla hoteles y muestra los disponibles.

El usuario selecciona un hotel y su reserva se guarda en la tabla reservas_hoteles.


El usuario ingresa los datos de los huéspedes (como su nombre y apellido), que se guardan en huespedes y se vinculan a la reserva.

Si el usuario también reserva un vuelo, el proceso es similar: se busca en vuelos, se crea una reserva en reservas_vuelos, y se registran los pasajeros en pasajeros.

Lista de Supuestos

Supuesto sobre la estructura de la base de datos:

Se asume que las tablas están normalizadas para evitar duplicación de datos y mantener la integridad referencial. Por ejemplo, en lugar de repetir la información del hotel en cada reserva, se usa una clave foránea (hotel_id) para referenciar el hotel correspondiente.

Supuesto sobre la validación de datos:

Se asume que las validaciones de datos (como fechas correctas, precios positivos, etc.) se manejan en la capa de aplicación (backend) y no directamente en la base de datos, para mantener la lógica de negocio centralizada.

Supuesto sobre el manejo de eliminaciones:

En lugar de eliminar registros físicamente (por ejemplo, un hotel o un vuelo), se usa un campo deleted (de tipo bit) para marcarlos como eliminados. Esto permite mantener un historial y evitar problemas de integridad referencial.

Supuesto sobre el uso de ModelMapper:

"Se utilizó ModelMapper para transformar entidades en DTOs de manera eficiente, reduciendo código repetitivo y asegurando una conversión precisa." Además, ModelMapper es fácil de configurar y extender, lo que lo hace ideal para proyectos que pueden crecer en el futuro.

Ejemplo: Cuando se consulta un hotel, la entidad Hotel tiene muchos campos que no son relevantes para el frontend (como deleted o reservado). Con ModelMapper, se puede mapear automáticamente la entidad Hotel a un HotelDTO que solo incluye los campos necesarios (como nombre, lugar, precio_noche, etc.).

Beneficios:

Menos código: No hay que escribir manualmente los getters y setters para cada transformación.

Mantenibilidad: Si la estructura de las entidades cambia, solo hay que ajustar la configuración de ModelMapper, no todo el código de mapeo.

Consistencia: ModelMapper asegura que los datos se transfieran de manera consistente, evitando errores humanos.

Nota.-
Para la base de datos se ha generado un script.sql, se encuentra en la raíz del proyecto en conjunto con el json de Postman para las demostraciones de diferentes peticiones.

La API maneja para la autenticación pedirá usuario y contraseña así que por lo tanto es
"Usuario: admin | Contraseña: 1234" en la base de datos está configurada con usuario: root y contraseña: Root123!

Para la documentación el path es /doc.
