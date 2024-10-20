package com.minimarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.entidades.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Puedes agregar métodos personalizados si es necesario

	List<Producto> findByCategoriaId(Long categoriaId);

}
