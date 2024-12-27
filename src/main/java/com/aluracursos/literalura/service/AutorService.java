package com.aluracursos.literalura.service;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> ObtenerTodosLosAutores(){
        return autorRepository.findAllByOrderByNombreAsc();
    }

    public List<Autor> obtenerAutoresVivosEnUnDeterminadoAnio(int anio){
        return autorRepository.obtenerAutoresVivosEnUnDeterminadoAnio(anio);
    }
}
