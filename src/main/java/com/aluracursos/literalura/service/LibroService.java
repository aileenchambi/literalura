package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aluracursos.literalura.model.Libro;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public Libro guardarLibro(Libro libro) {

        Optional<Libro> libroExistente = libroRepository.findByTitulo(libro.getTitulo());
        if(libroExistente.isPresent()){
            return libroExistente.get();
        }

        Optional<Autor> autorExistente = autorRepository.findByNombre(libro.getAutor().getNombre());
        if (autorExistente.isPresent()) {
            libro.setAutor(autorExistente.get());
        } else {
            Autor nuevoAutor = libro.getAutor();
            Autor autorGuardado = autorRepository.save(nuevoAutor);
            libro.setAutor(autorGuardado);
        }

        return libroRepository.save(libro);
    }

    public List<Libro> ObtenerTodosLosLibros(){
        return libroRepository.findAllByOrderByTituloAsc();
    }

    public List<Libro> obtenerLibrosPorIdioma(String idioma){
        return libroRepository.obtenerLibrosPorIdioma(idioma);
    }

}
