# WSRestSBoot

API REST para gestión de productos iPhone desarrollada con Spring Boot.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.5.6
- Spring Data JPA
- MySQL
- Maven
- Lombok

## 📋 Descripción

Servicio REST completo para gestionar un inventario de productos iPhone con operaciones CRUD y búsquedas personalizadas.

## 🔧 Configuración

### Base de Datos
La aplicación está configurada para conectarse a MySQL Railway. La configuración se encuentra en `application.properties`.

### Requisitos
- Java 17 o superior
- Maven 3.6+
- MySQL

## 📦 Instalación

```bash
# Clonar el repositorio
git clone https://github.com/WilderSantamaria18/WSRestSBoot.git

# Navegar al directorio
cd WSRestSBoot

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

## 🌐 Endpoints de la API

### Base URL: `http://localhost:8080/api/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Obtener todos los productos |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| POST | `/api/productos` | Crear nuevo producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| GET | `/api/productos/buscar/modelo/{modelo}` | Buscar por modelo |
| GET | `/api/productos/buscar/color/{color}` | Buscar por color |
| GET | `/api/productos/buscar/almacenamiento/{almacenamiento}` | Buscar por almacenamiento |
| GET | `/api/productos/disponibles` | Productos con stock disponible |

## 📝 Ejemplo de uso

### Crear un producto
```json
POST /api/productos
{
  "modelo": "iPhone 15 Pro",
  "precio": 999.99,
  "almacenamiento": "256GB",
  "color": "Titanio Natural",
  "stock": 10
}
```

### Obtener todos los productos
```bash
GET /api/productos
```

## 🖥️ Interfaz de Prueba

La aplicación incluye una interfaz web para probar la API:
```
http://localhost:8080
```

## 📄 Documentación

Para más detalles, consulta `API_DOCUMENTATION.md`

## 👨‍💻 Autor

Wilder Santamaria

## 📅 Fecha

Octubre 2025
