package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Autor> findAllByOrderByNombreAsc();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio and fechaDeFallecimiento > :anio")
    List<Autor> obtenerAutoresVivosEnUnDeterminadoAnio(int anio);


}
