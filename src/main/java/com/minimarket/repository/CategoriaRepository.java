package com.minimarket.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.minimarket.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
