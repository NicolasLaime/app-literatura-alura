package com.alura.appLiteratura.service;


import com.alura.appLiteratura.model.Libro;
import com.alura.appLiteratura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarLibro(){
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma){
        return libroRepository.findByIdioma(idioma);
    }


    public Libro crearLibro(Libro libro){
        return libroRepository.save(libro);
    }

    public Optional<Libro> obtenerLibroPorId(Long id){
        return libroRepository.findById(id);
    }

    public Optional<Libro> obtenerLibroPorTitulo(String titulo) {
        return libroRepository.findByTituloIgnoreCase(titulo);
    }

    public Libro actualizarLibro(Long id, Libro libroDetalles){
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado!"));
        libro.setTitulo(libroDetalles.getTitulo());
        libro.setIdioma(libroDetalles.getIdioma());
        libro.setDescargas(libroDetalles.getDescargas());
        libro.setAutor(libroDetalles.getAutor());
        return libroRepository.save(libro);
    }

    public void eliminarLibro(Long id){
        libroRepository.deleteById(id);
    }






}
