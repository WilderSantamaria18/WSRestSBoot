package com.copntinua1.demo.repository;

import com.copntinua1.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad Producto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Buscar productos por modelo
    List<Producto> findByModeloContainingIgnoreCase(String modelo);
    
    // Buscar productos por color
    List<Producto> findByColorIgnoreCase(String color);
    
    // Buscar productos por almacenamiento
    List<Producto> findByAlmacenamiento(String almacenamiento);
    
    // Buscar productos con stock disponible
    List<Producto> findByStockGreaterThan(Integer stock);
}
