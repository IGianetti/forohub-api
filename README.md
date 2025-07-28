# ForoHub API REST

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen)
![Java](https://img.shields.io/badge/Java-24-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![Flyway](https://img.shields.io/badge/Database-Flyway-green)

---

## 📄 Descripción del Proyecto

ForoHub es una API REST diseñada para gestionar un foro de discusión, permitiendo a los usuarios crear, consultar, actualizar y eliminar tópicos. La aplicación está construida con **Spring Boot**, siguiendo las mejores prácticas para el desarrollo de APIs RESTful. Incluye un robusto sistema de autenticación y autorización basado en **JSON Web Tokens (JWT)** para asegurar los endpoints.

Este proyecto fue desarrollado como parte de un desafío de programación, aplicando conceptos clave de Spring Security, JPA con Hibernate, validación de datos y gestión de bases de datos con Flyway.

---

## ✨ Características Principales

* **API RESTful:** Endpoints bien definidos para la gestión de tópicos.
* **Autenticación de Usuarios:** Registro y autenticación de usuarios a través de `correoElectronico` y `contrasena`.
* **Seguridad JWT:**
    * Generación de JWTs al iniciar sesión (`/login`).
    * Filtro de seguridad para validar tokens JWT en cada solicitud a endpoints protegidos.
    * Protección de rutas sensibles (creación, edición, eliminación de tópicos).
* **Base de Datos Relacional:** Persistencia de datos con MySQL.
* **Migraciones de Base de Datos:** Gestión de esquemas de base de datos con Flyway para un desarrollo ágil y consistente.
* **Validación de Datos:** Uso de `jakarta.validation` para asegurar la integridad de los datos de entrada.
* **Manejo de Excepciones:** Respuestas de error claras y significativas.

---

## 🛠️ Tecnologías Utilizadas

* **Java 24:** Lenguaje de programación.
* **Spring Boot 3.5.4:** Framework principal para el desarrollo de la API.
    * Spring Web
    * Spring Data JPA
    * Spring Security
    * Spring Boot DevTools (para desarrollo)
* **MySQL 8.0:** Base de datos relacional.
* **Hibernate:** Implementación de JPA para la persistencia de objetos.
* **Flyway:** Herramienta de migración de bases de datos.
* **Auth0 Java-JWT:** Biblioteca para la implementación y gestión de JSON Web Tokens.
* **Maven:** Herramienta de gestión de dependencias y construcción de proyectos.
* **Insomnia / Postman:** Herramientas para probar la API REST.

---

## 🚀 Cómo Ejecutar el Proyecto

Sigue estos pasos para configurar y ejecutar la API de ForoHub en tu entorno local.

### **Requisitos Previos**

* **Java Development Kit (JDK) 24** o superior.
* **Maven** instalado.
* **MySQL 8.0** o superior (y un cliente como MySQL Workbench).
* **IDE** (IntelliJ IDEA, Eclipse, VS Code).

### **1. Clonar el Repositorio**

```bash
git clone [https://github.com/IGianetti/forohub.git](https://github.com/IGianetti/forohub.git)
cd forohub
```

### **2. Configuración de la Base de Datos**

Asegúrate de tener un servidor MySQL en ejecución.

* Crea una base de datos llamada `forohub`. Puedes hacerlo desde MySQL Workbench o la línea de comandos:

    ```sql
    CREATE DATABASE forohub;
    ```

* Configura las credenciales de tu base de datos en `src/main/resources/application.properties`:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/forohub?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=tu_contrasena_mysql
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # Configuración de JPA/Hibernate
    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

    # Configuración de Flyway (asegura que las migraciones se apliquen)
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:db/migration

    # JWT Configuration
    jwt.secret=mi-clave-secreta-muy-larga-y-segura-para-firmar-jwt-tokens-del-forohub-por-favor-cambiar-en-produccion
    jwt.expiration=10800000
    ```
    **¡Importante!** Cambia `tu_contrasena_mysql` por tu contraseña real de MySQL. Asegúrate de que `jwt.secret` sea una cadena larga y segura.
* ### **3. Ejecutar Migraciones de Base de Datos (Flyway)**

Flyway ejecutará automáticamente las migraciones al iniciar la aplicación. Sin embargo, para asegurarte de que la estructura inicial de la tabla `usuarios` y `topicos` esté creada, puedes verificar los archivos en `src/main/resources/db/migration`.

* **Crear el usuario inicial:**
    * Si no tienes un usuario inicial en la tabla `usuarios`, puedes insertarlo manualmente para pruebas.
    * Primero, genera la contraseña encriptada (BCrypt) para "12345":
        * Crea temporalmente un `main` en una clase de utilidades (ej. `PasswordGenerator.java` en `com.forohub.util`):
            ```java
            package com.forohub.util; // Puedes colocarlo en este paquete o similar

            import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

            public class PasswordGenerator {
                public static void main(String[] args) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String rawPassword = "12345"; // La contraseña que usarás
                    String encodedPassword = encoder.encode(rawPassword);
                    System.out.println("Contraseña codificada (BCrypt): " + encodedPassword);
                }
            }
            ```
        * Ejecuta este `main` y copia el hash generado (empieza con `$2a$`).
        * **Elimina la clase `PasswordGenerator.java` después de obtener el hash.**
    * Luego, inserta el usuario en MySQL Workbench (o similar):
        ```sql
        INSERT INTO forohub.usuarios (nombre, correo_electronico, contrasena, rol)
        VALUES ('Admin', 'admin@forohub.com', 'TU_HASH_BCRYPT_GENERADO_AQUI', 'ADMIN');
        ```
      Reemplaza `TU_HASH_BCRYPT_GENERADO_AQUI` con el hash que obtuviste.
### **4. Ejecutar la Aplicación**

* Desde tu IDE (IntelliJ, Eclipse): Abre la clase `ForohubApplication` y ejecútala como una aplicación Spring Boot.
* Desde la línea de comandos (en la raíz del proyecto):
    ```bash
    mvn spring-boot:run
    ```

La aplicación se iniciará en `http://localhost:8080`.

## 🧪 **Uso y Pruebas de la API (con Insomnia/Postman)**

### **1. Autenticación (Obtener Token JWT)**

* **URL:** `POST http://localhost:8080/login`
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**
    ```json
    {
        "correoElectronico": "admin@forohub.com",
        "contrasena": "12345"
    }
    ```
* **Respuesta Exitosa (200 OK):**
    ```json
    {
        "jwtToken": "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0"(Por ejemplo)
    }
    ```
  **Guarda este `jwtToken`**, lo usarás para las próximas solicitudes.

### **2. Acceder a Endpoints Protegidos**

Para acceder a cualquier otro endpoint (ej. `/topicos`), debes incluir el JWT en el encabezado `Authorization`.

* **Método de envío del Token en Insomnia/Postman:**
    * Ve a la pestaña **Auth**.
    * Selecciona el tipo **"Bearer Token"**.
    * Pega el `jwtToken` que obtuviste en el campo `Token`.

* **Ejemplo: Listar Tópicos (Protegido)**
    * **URL:** `GET http://localhost:8080/topicos`
    * **Headers:** (Automáticamente añadido por la configuración de Auth en Insomnia/Postman) `Authorization: Bearer <tu_jwt_token>`
    * **Respuesta Exitosa (200 OK):** (Si la DB está vacía, verás un objeto `Page` vacío)
        ```json
        {
            "content": [],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10,
                "sort": {
                    "empty": false,
                    "sorted": true,
                    "unsorted": false
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "totalPages": 0,
            "totalElements": 0,
            "last": true,
            "size": 10,
            "number": 0,
            "numberOfElements": 0,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "first": true,
            "empty": true
        }
        ```
    * **Respuesta sin Token o Token Inválido (403 Forbidden / 401 Unauthorized):**
        ```json
        {
            "timestamp": "YYYY-MM-DDTHH:MM:SS.ZZZ+00:00",
            "status": 403,
            "error": "Forbidden",
            "path": "/topicos"
        }
        ```
---

## 🧑‍💻 **Autor**

* **Iván Gianetti**
* **GitHub:** [IGianetti](https://github.com/IGianetti)
* **Email:** ivangianetti@gmail.com

---

## 📜 **Licencia**

Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.      
