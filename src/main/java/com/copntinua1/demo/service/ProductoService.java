package com.copntinua1.demo.service;

import com.copntinua1.demo.model.Producto;
import com.copntinua1.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de Producto
 */
@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }
    
    // Obtener producto por ID
    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }
    
    // Crear nuevo producto
    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }
    
    // Actualizar producto
    public Producto actualizar(Integer id, Producto productoActualizado) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setModelo(productoActualizado.getModelo());
                producto.setPrecio(productoActualizado.getPrecio());
                producto.setAlmacenamiento(productoActualizado.getAlmacenamiento());
                producto.setColor(productoActualizado.getColor());
                producto.setStock(productoActualizado.getStock());
                return productoRepository.save(producto);
            })
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }
    
    // Eliminar producto
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
    
    // Buscar por modelo
    public List<Producto> buscarPorModelo(String modelo) {
        return productoRepository.findByModeloContainingIgnoreCase(modelo);
    }
    
    // Buscar por color
    public List<Producto> buscarPorColor(String color) {
        return productoRepository.findByColorIgnoreCase(color);
    }
    
    // Buscar por almacenamiento
    public List<Producto> buscarPorAlmacenamiento(String almacenamiento) {
        return productoRepository.findByAlmacenamiento(almacenamiento);
    }
    
    // Buscar productos con stock disponible
    public List<Producto> obtenerProductosConStock() {
        return productoRepository.findByStockGreaterThan(0);
    }
}
