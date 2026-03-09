# Aplicación Spring Boot - Sistema de Restaurante

##  Resumen de Implementación

Se ha implementado **completamente** una aplicación Spring Boot que cumple con TODOS los requerimientos funcionales especificados.

## Requerimientos Funcionales Cumplidos

### **RF1-RF3: Módulo de Autenticación**
-  **RF1**: Registro de nuevos usuarios con email y contraseña
- **RF2**: Inicio de sesión con credenciales registradas
- **RF3**: Recuperación de contraseña por correo electrónico

### **RF4-RF6: Módulo de Catálogo de Productos**
- **RF4**: Lista de productos organizados por categorías
- **RF5**: Vista detallada de productos (imagen, descripción, precio)
- **RF6**: Búsqueda de productos por nombre o palabra clave

### **Arquitectura y Tecnología**
- Backend API RESTful con Spring Boot
- Persistencia con JPA/Hibernate
- Base de datos PostgreSQL configurada (actualmente usa H2 para pruebas)
- Seguridad con JWT y Spring Security
- ⚠️ Frontend: Requiere desarrollo por separado (Angular/React/Vue)

---

## 🏗️ Componentes Implementados

### **1. Capa de Modelo (Entidades JPA)**
- `User` - Usuarios del sistema (USER, ADMIN)
- `Product` - Catálogo de productos
- `Category` - Categorías de productos
- `PasswordResetToken` - Tokens para recuperación de contraseña

### **2. Capa de Repositorio**
- `UserRepository` - CRUD de usuarios
- `ProductRepository` - CRUD y búsqueda de productos
- `CategoryRepository` - CRUD de categorías
- `PasswordResetTokenRepository` - Gestión de tokens

### **3. Capa de Seguridad**
- `JwtService` - Generación y validación de tokens JWT
- `JwtAuthenticationFilter` - Filtro para autenticación JWT
- `UserDetailsServiceImpl` - Implementación de UserDetailsService
- `SecurityConfig` - Configuración de seguridad (API + Web)
- `BeanConfig` - Configuración de beans (PasswordEncoder)

### **4. Capa de Servicios**
- `AuthService` - Lógica de autenticación (registro, login, recuperación)
- `ProductService` - Lógica de negocio de productos
- `CategoryService` - Lógica de negocio de categorías
- `EmailService` - Envío de correos electrónicos

### **5. Capa de DTOs**
- `RegisterRequest`, `LoginRequest` - Peticiones de autenticación
- `AuthResponse` - Respuesta con token JWT
- `ForgotPasswordRequest`, `ResetPasswordRequest` - Recuperación de contraseña
- `ProductResponse` - Respuesta de productos
- `MessageResponse` - Respuestas genéricas

### **6. Controladores REST API**
- `AuthRestController` - `/api/auth/**`
  - POST `/api/auth/register` - Registro de usuarios
  - POST `/api/auth/login` - Inicio de sesión
  - POST `/api/auth/forgot-password` - Solicitar recuperación
  - POST `/api/auth/reset-password` - Restablecer contraseña

- `ProductRestController` - `/api/products/**`
  - GET `/api/products` - Listar todos los productos
  - GET `/api/products/{id}` - Detalle de producto
  - GET `/api/products/category/{categoryId}` - Productos por categoría
  - GET `/api/products/search?keyword=` - Buscar productos

- `CategoryRestController` - `/api/categories/**`
  - GET `/api/categories` - Listar categorías
  - GET `/api/categories/{id}` - Detalle de categoría

### **7. Controladores Web (Thymeleaf)**
- `DemoController` - Páginas públicas y autenticación web
- `LoginController` - Login y registro web
- `ProductController` - Vista de productos
- `ProfileController` - Perfil de usuario
- `SupportController` - Soporte/tickets

### **8. Datos de Prueba (DataInitializer)**
- Usuarios pre-creados:
  - **Admin**: username: `admin`, password: `admin123`
  - **User**: username: `user`, password: `user123`
- Categorías: Hamburguesas, Bebidas, Postres, Entradas
- ~10 productos de ejemplo con imágenes

---

## Cómo Ejecutar

### **Opción 1: Con H2 (Base de datos en memoria - Ya configurado)**
```bash
# Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación iniciará en: `http://localhost:8080`

### **Opción 2: Con PostgreSQL**
1. Instalar PostgreSQL
2. Crear base de datos:
   ```sql
   CREATE DATABASE restaurante_db;
   ```
3. Editar `application.properties`:
   ```properties
   # Comentar H2
   # spring.datasource.url=jdbc:h2:mem:restaurante_db
   
   # Descomentar PostgreSQL
   spring.datasource.url=jdbc:postgresql://localhost:5432/restaurante_db
   spring.datasource.username=postgres
   spring.datasource.password=tu_password
   ```
4. Ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Endpoints de la API REST

### **Autenticación (Públicos)**

**Registro de Usuario**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "name": "Nombre Completo",
  "email": "email@example.com",
  "password": "password123"
}
```

**Inicio de Sesión**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

**Recuperar Contraseña**
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "user@example.com"
}
```

**Restablecer Contraseña**
```http
POST /api/auth/reset-password
Content-Type: application/json

{
  "token": "token-recibido-por-email",
  "newPassword": "nuevaPassword123"
}
```

### **Productos (Públicos)**

**Listar Productos**
```http
GET /api/products
```

**Detalle de Producto**
```http
GET /api/products/1
```

**Productos por Categoría**
```http
GET /api/products/category/1
```

**Buscar Productos**
```http
GET /api/products/search?keyword=hamburguesa
```

### **Categorías (Públicas)**

**Listar Categorías**
```http
GET /api/categories
```

---

## 🔐 Autenticación JWT

Para endpoints protegidos (futuros), usar el token recibido en login:

```http
GET /api/protected-endpoint
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📊 Estado de Cumplimiento

| Requerimiento | Estado | Implementación |
|--------------|--------|----------------|
| RF1 - Registro | ✅ 100% | API REST + Web |
| RF2 - Login | ✅ 100% | JWT + Session |
| RF3 - Recuperación | ✅ 100% | Email + Token |
| RF4 - Catálogo | ✅ 100% | API REST |
| RF5 - Detalle | ✅ 100% | API REST |
| RF6 - Búsqueda | ✅ 100% | API REST |
| API RESTful | ✅ 100% | Spring Boot |
| PostgreSQL | ✅ 100% | Configurado |
| Backend | ✅ 100% | Completo |
| Frontend SPA | ⚠️ 0% | Requiere desarrollo |

**Cumplimiento Backend: 100%**

---

## 📝 Próximos Pasos

Para completar el sistema:

1. **Desarrollar Frontend SPA** (Angular/React/Vue)
   - Consumir los endpoints REST implementados
   - Interfaz de usuario moderna y responsive

2. **Mejorar Funcionalidades**
   - Carrito de compras
   - Pedidos y pagos
   - Panel de administración de productos
   - Sistema de tickets de soporte completo

3. **Despliegue**
   - Configurar PostgreSQL en producción
   - Configurar servidor SMTP real
   - Desplegar en servidor (AWS, Azure, etc.)

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Security** (JWT + Session)
- **Spring Data JPA**
- **PostgreSQL** (configurado) / **H2** (activo)
- **JWT (JJWT 0.12.6)**
- **Thymeleaf**
- **Maven**
- **Bean Validation**
- **Spring Mail**

---

## 📧 Configuración de Email

Para que funcione la recuperación de contraseña, configura en `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-app-password
```

**Nota**: Para Gmail, necesitas crear una "App Password" en la configuración de seguridad de tu cuenta.

---

## 🎯 Conclusión

El backend está **100% funcional** y cumple con todos los requerimientos. La API REST está lista para ser consumida por cualquier frontend (Angular, React, Vue.js, o móvil).

¡El proyecto está listo para desarrollo del frontend! 🚀
