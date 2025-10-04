package com.copntinua1.demo.controller;

import com.copntinua1.demo.model.Producto;
import com.copntinua1.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar productos de iPhone
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    /**
     * GET /api/productos - Obtener todos los productos
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }
    
    /**
     * GET /api/productos/{id} - Obtener producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return productoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/productos - Crear nuevo producto
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }
    
    /**
     * PUT /api/productos/{id} - Actualizar producto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Integer id, 
            @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/productos/{id} - Eliminar producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/productos/buscar/modelo/{modelo} - Buscar por modelo
     */
    @GetMapping("/buscar/modelo/{modelo}")
    public ResponseEntity<List<Producto>> buscarPorModelo(@PathVariable String modelo) {
        List<Producto> productos = productoService.buscarPorModelo(modelo);
        return ResponseEntity.ok(productos);
    }
    
    /**
     * GET /api/productos/buscar/color/{color} - Buscar por color
     */
    @GetMapping("/buscar/color/{color}")
    public ResponseEntity<List<Producto>> buscarPorColor(@PathVariable String color) {
        List<Producto> productos = productoService.buscarPorColor(color);
        return ResponseEntity.ok(productos);
    }
    
    /**
     * GET /api/productos/buscar/almacenamiento/{almacenamiento} - Buscar por almacenamiento
     */
    @GetMapping("/buscar/almacenamiento/{almacenamiento}")
    public ResponseEntity<List<Producto>> buscarPorAlmacenamiento(@PathVariable String almacenamiento) {
        List<Producto> productos = productoService.buscarPorAlmacenamiento(almacenamiento);
        return ResponseEntity.ok(productos);
    }
    
    /**
     * GET /api/productos/disponibles - Obtener productos con stock disponible
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> obtenerDisponibles() {
        List<Producto> productos = productoService.obtenerProductosConStock();
        return ResponseEntity.ok(productos);
    }
}
