package com.alura.appLiteratura.service;


import com.alura.appLiteratura.model.Autor;
import com.alura.appLiteratura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.Optional;

@Service
public class AutorService {


    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listaAutores(){
        return autorRepository.findAllConLibros();
    }

    public List<Autor> listarAutoresVivos(int ano){
        return autorRepository.findAutoresVivos(ano);
    }

    public  Autor crearAutor(Autor autor){
        return autorRepository.save(autor);
    }

    public Optional<Autor> obtenerAutorPorId(Long id){
        return autorRepository.findById(id);
    }

    public Optional<Autor> obtenerAutorPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }

    public Autor actualizarAutor(Long id, Autor autorDetalles) {
        // Buscar el autor existente por ID
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id));

        // Actualizar los campos del autor con los detalles proporcionados
        autor.setNombre(autorDetalles.getNombre());
        autor.setAnoNacimiento(autorDetalles.getAnoNacimiento());
        autor.setAnoFallecimiento(autorDetalles.getAnoFallecimiento());

        // Guardar y devolver el autor actualizado
        return autorRepository.save(autor);
    }


    public void elminarAutor(Long id){
        autorRepository.deleteById(id);
    }

}
