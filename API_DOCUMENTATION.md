# API REST - Gestión de Productos iPhone

## Descripción
Servicio REST para gestionar productos de iPhone con operaciones CRUD completas.

## Configuración de Base de Datos
La aplicación está configurada para conectarse a MySQL Railway con la tabla `producto`.

## Endpoints de la API

### Base URL
```
http://localhost:8080/api/productos
```

### 1. Obtener todos los productos
- **URL**: `/api/productos`
- **Método**: `GET`
- **Respuesta exitosa**: 200 OK
```json
[
  {
    "id": 1,
    "modelo": "iPhone 15 Pro",
    "precio": 999.99,
    "almacenamiento": "256GB",
    "color": "Titanio Natural",
    "stock": 10,
    "fechaCreacion": "2025-10-04T12:00:00"
  }
]
```

### 2. Obtener producto por ID
- **URL**: `/api/productos/{id}`
- **Método**: `GET`
- **Parámetros**: `id` (Integer) - ID del producto
- **Respuesta exitosa**: 200 OK
- **Respuesta error**: 404 Not Found

### 3. Crear nuevo producto
- **URL**: `/api/productos`
- **Método**: `POST`
- **Body**:
```json
{
  "modelo": "iPhone 15 Pro Max",
  "precio": 1199.99,
  "almacenamiento": "512GB",
  "color": "Titanio Azul",
  "stock": 5
}
```
- **Respuesta exitosa**: 201 Created

### 4. Actualizar producto
- **URL**: `/api/productos/{id}`
- **Método**: `PUT`
- **Parámetros**: `id` (Integer) - ID del producto
- **Body**:
```json
{
  "modelo": "iPhone 15 Pro Max",
  "precio": 1099.99,
  "almacenamiento": "512GB",
  "color": "Titanio Azul",
  "stock": 8
}
```
- **Respuesta exitosa**: 200 OK
- **Respuesta error**: 404 Not Found

### 5. Eliminar producto
- **URL**: `/api/productos/{id}`
- **Método**: `DELETE`
- **Parámetros**: `id` (Integer) - ID del producto
- **Respuesta exitosa**: 204 No Content
- **Respuesta error**: 404 Not Found

### 6. Buscar por modelo
- **URL**: `/api/productos/buscar/modelo/{modelo}`
- **Método**: `GET`
- **Parámetros**: `modelo` (String) - Nombre o parte del modelo
- **Ejemplo**: `/api/productos/buscar/modelo/iPhone 15`

### 7. Buscar por color
- **URL**: `/api/productos/buscar/color/{color}`
- **Método**: `GET`
- **Parámetros**: `color` (String) - Color del producto
- **Ejemplo**: `/api/productos/buscar/color/Titanio`

### 8. Buscar por almacenamiento
- **URL**: `/api/productos/buscar/almacenamiento/{almacenamiento}`
- **Método**: `GET`
- **Parámetros**: `almacenamiento` (String) - Capacidad de almacenamiento
- **Ejemplo**: `/api/productos/buscar/almacenamiento/256GB`

### 9. Obtener productos disponibles (con stock)
- **URL**: `/api/productos/disponibles`
- **Método**: `GET`
- **Respuesta**: Lista de productos con stock > 0

## Ejecutar la aplicación

### Requisitos
- Java 17 o superior
- Maven 3.6+
- MySQL (Railway configurado)

### Comandos
```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## Probar la API con cURL

### Crear un producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "iPhone 15",
    "precio": 799.99,
    "almacenamiento": "128GB",
    "color": "Negro",
    "stock": 15
  }'
```

### Obtener todos los productos
```bash
curl http://localhost:8080/api/productos
```

### Obtener producto por ID
```bash
curl http://localhost:8080/api/productos/1
```

### Actualizar producto
```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "iPhone 15",
    "precio": 749.99,
    "almacenamiento": "128GB",
    "color": "Negro",
    "stock": 20
  }'
```

### Eliminar producto
```bash
curl -X DELETE http://localhost:8080/api/productos/1
```

## Tecnologías utilizadas
- Spring Boot 3.5.6
- Spring Data JPA
- MySQL Connector
- Lombok
- Maven
