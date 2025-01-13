package com.alura.appLiteratura.principal;

import com.alura.appLiteratura.dto.*;
import com.alura.appLiteratura.model.*;
import com.alura.appLiteratura.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

      @Autowired
      private LibroService libroService;

      @Autowired
      private AutorService autorService;

      @Autowired
      private ConsumoApi consumoApi;

      @Autowired
      private ConvierteDatos convierteDatos;

      private static final String BASE_URL = "https://gutendex.com/books/";

      public void mostrarMenu() {
            Scanner scanner = new Scanner(System.in);
            int opcion;

            do {
                  System.out.println("\n--- LITERALURA ---");
                  System.out.println("1 - Buscar libro por título");
                  System.out.println("2 - Listar libros registrados");
                  System.out.println("3 - Listar autores registrados");
                  System.out.println("4 - Listar autores vivos en un año");
                  System.out.println("5 - Listar libros por idioma");
                  System.out.println("0 - Salir");
                  System.out.print("Seleccione una opción: ");
                  opcion = scanner.nextInt();
                  scanner.nextLine(); // Consumir el salto de línea

                  switch (opcion) {
                        case 1 -> buscarLibro(scanner);
                        case 2 -> listarLibros();
                        case 3 -> listarAutores();
                        case 4 -> listarAutoresVivosEnAno(scanner);
                        case 5 -> listarLibrosPorIdioma(scanner);
                        case 0 -> System.out.println("Saliendo...");
                        default -> System.out.println("Opción no válida. Intente nuevamente.");
                  }
            } while (opcion != 0);

            scanner.close();
      }

      private void buscarLibro(Scanner scanner) {
            System.out.print("Ingrese el título del libro: ");
            String titulo = scanner.nextLine();

            try {
                  String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                  String json = consumoApi.obtenerDatos(BASE_URL + "?search=" + encodedTitulo);
                  RespuestaLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, RespuestaLibrosDTO.class);
                  List<LibroDTO> librosDTO = respuestaLibrosDTO.getLibros();

                  if (librosDTO.isEmpty()) {
                        System.out.println("Libro no encontrado en la API");
                        return;
                  }

                  for (LibroDTO libroDTO : librosDTO) {
                        if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                              // Comprobar si el libro ya existe en tu base de datos
                              Optional<Libro> libroExistente = libroService.obtenerLibroPorTitulo(titulo);
                              if (libroExistente.isPresent()) {
                                    System.out.println("Detalle: Clave (titulo)=(" + titulo + ") ya existe");
                                    System.out.println("No se puede registrar el mismo libro más de una vez");
                              } else {
                                    // Convertir LibroDTO a Libro para guardar en la base de datos
                                    Libro libro = convertirDTOALibro(libroDTO);
                                    libroService.crearLibro(libro);
                                    System.out.println("Libro registrado: " + libro.getTitulo());
                                    mostrarDetallesLibro(libroDTO);
                              }
                              return; // Solo mostramos detalles del primer libro que coincida exactamente
                        }
                  }
                  System.out.println("No se encontró un libro exactamente con el título '" + titulo + "' en la API");
            } catch (Exception e) {
                  System.out.println("Error al obtener datos de la API: " + e.getMessage());
            }
      }

      private void listarLibros() {
            libroService.listarLibro().forEach(libro -> {
                  System.out.println("------ LIBRO ------");
                  System.out.println("Título: " + libro.getTitulo());
                  System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                  System.out.println("Idioma: " + libro.getIdioma());
                  System.out.println("Número de descargas: " + libro.getDescargas());
            });
      }

      private void listarAutores() {
            autorService.listaAutores().forEach(autor -> {
                  System.out.println("------ AUTOR ------");
                  System.out.println("Nombre: " + autor.getNombre());
                  System.out.println("Fecha de nacimiento: " + autor.getAnoNacimiento());
                  System.out.println("Fecha de fallecimiento: " + (autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "Desconocido"));
                  String libros = autor.getLibros().stream()
                          .map(Libro::getTitulo)
                          .collect(Collectors.joining(", "));
                  System.out.println("Libros: [ " + libros + " ]");
            });
      }

      private void listarAutoresVivosEnAno(Scanner scanner) {
            System.out.print("Ingrese el año para buscar autores vivos: ");
            int ano = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            List<Autor> autoresVivos = autorService.listarAutoresVivos(ano);
            if (autoresVivos.isEmpty()) {
                  System.out.println("No se encontraron autores vivos en el año " + ano);
            } else {
                  autoresVivos.forEach(autor -> {
                        System.out.println("------ AUTOR ------");
                        System.out.println("Nombre: " + autor.getNombre());
                        System.out.println("Fecha de nacimiento: " + autor.getAnoNacimiento());
                        System.out.println("Fecha de fallecimiento: " + (autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "Desconocido"));
                        System.out.println("Libros: " + autor.getLibros().size());
                  });
            }
      }

      private void listarLibrosPorIdioma(Scanner scanner) {
            System.out.println("Ingrese el idioma (es/en/fr/pt):");
            String idioma = scanner.nextLine();
            if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma) || "pt".equalsIgnoreCase(idioma)) {
                  List<Libro> libros = libroService.listarLibrosPorIdioma(idioma);
                  if (libros.isEmpty()) {
                        System.out.println("No se encontraron libros en el idioma " + idioma);
                  } else {
                        libros.forEach(libro -> {
                              System.out.println("------ LIBRO ------");
                              System.out.println("Título: " + libro.getTitulo());
                              // Puedes mostrar más información si lo deseas:
                              System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                              System.out.println("Idioma: " + libro.getIdioma());
                              System.out.println("Número de descargas: " + libro.getDescargas());
                        });
                  }
            } else {
                  System.out.println("Idioma no válido. Intente de nuevo.");
            }
      }

      private void mostrarDetallesLibro(LibroDTO libroDTO) {
            System.out.println("------ LIBRO ------");
            System.out.println("Título: " + libroDTO.getTitulo());
            System.out.println("Autor: " + (libroDTO.getAutores().isEmpty() ? "Desconocido" : libroDTO.getAutores().get(0).getNombre()));
            System.out.println("Idioma: " + libroDTO.getIdiomas().get(0));
            System.out.println("Número de descargas: " + libroDTO.getNumeroDescargas());
      }

      private Libro convertirDTOALibro(LibroDTO libroDTO) {
            Libro libro = new Libro();
            libro.setTitulo(libroDTO.getTitulo());
            libro.setIdioma(libroDTO.getIdiomas().get(0)); // Asumimos que solo hay un idioma
            libro.setDescargas(libroDTO.getNumeroDescargas());

            // Aquí asumimos que solo guardamos el primer autor. Si necesitas todos los autores, sería más complejo.
            if (!libroDTO.getAutores().isEmpty()) {
                  AutorDTO autorDTO = libroDTO.getAutores().get(0);
                  Optional<Autor> autorExistente = autorService.obtenerAutorPorNombre(autorDTO.getNombre());
                  Autor autor = autorExistente.orElseGet(() -> {
                        Autor nuevoAutor = new Autor();
                        nuevoAutor.setNombre(autorDTO.getNombre());
                        nuevoAutor.setAnoNacimiento(autorDTO.getAnoNacimiento());
                        nuevoAutor.setAnoFallecimiento(autorDTO.getAnoFallecimiento() == 0 ? null : autorDTO.getAnoFallecimiento()); // 0 puede significar no fallecido en algunos APIs
                        return autorService.crearAutor(nuevoAutor);
                  });
                  libro.setAutor(autor);
            }

            return libro;
      }
}