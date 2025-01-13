package com.alura.appLiteratura.repository;

import com.alura.appLiteratura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE (a.anoFallecimiento IS NULL OR a.anoFallecimiento > :ano) AND a.anoNacimiento <= :ano ")
    List<Autor> findAutoresVivos(@Param("ano") int ano);

    @Query("SELECT a From Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllConLibros();




}





