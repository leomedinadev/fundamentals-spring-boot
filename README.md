# Fundamentos de Spring Boot

API REST de usuarios hecha para practicar los fundamentos de **Spring Boot** y **Spring Data JPA**: inyección de dependencias, repositorios con consultas derivadas y `@Query`, paginación, transacciones y documentación con Swagger.

## Stack

- Java 8 y Spring Boot 2.7 (Maven)
- Spring Data JPA con H2 en memoria
- Swagger / OpenAPI (`springdoc`)
- Lombok
- JUnit 5 y MockMvc

## Cómo ejecutar

Requiere Java 8 o superior. No hay que instalar ninguna base de datos:

```bash
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080/fundamentalsServices` y Swagger en `http://localhost:8080/fundamentalsServices/api/public/swagger-ui.html`.

Al arrancar se cargan 12 usuarios de ejemplo. Los datos se pierden al detener la aplicación.

## Endpoints

| Ruta | Método | Descripción |
|---|---|---|
| `/users/all` | `GET` | Lista todos los usuarios |
| `/users/all/{page}/{size}` | `GET` | Lista paginada |
| `/users/find/{id}` | `GET` | Usuario por id |
| `/users/byEmail/{email}` | `GET` | Usuario por correo |
| `/users/byName/{name}` | `GET` | Usuarios cuyo nombre contiene el texto |
| `/users/create` | `POST` | Crea un usuario |
| `/users/update/{id}` | `PUT` | Actualiza el usuario de ese id |
| `/users/delete/{id}` | `DELETE` | Elimina un usuario |

Si el usuario no existe, la API responde `404`.

## Qué se practica

- **Consultas** en [`IUserRepository`](src/main/java/ec/com/leodev/fundamentals/repository/IUserRepository.java): métodos derivados del nombre (`findByNameContaining`, `findByBirthDateBetween`…), JPQL, SQL nativo, parámetros con nombre y ordenamiento.
- **Relaciones**: un usuario tiene muchos posts (`@OneToMany` / `@ManyToOne`).
- **Transacciones**: guardado de varios usuarios con `@Transactional` y rollback si uno falla.

## Tests

```bash
./mvnw test
```
