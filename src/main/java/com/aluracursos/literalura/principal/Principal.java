package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.service.AutorService;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LibroService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_BUSCAR = "?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroService libroService;
    private AutorService autorService;

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                    *                     MENU                            *
                    *  1 - Buscar libro por titulo                        *
                    *  2 - Listar libros registrados                      *
                    *  3 - Listar autores registrados                     *
                    *  4 - Listar autores vivos en un determinado año     *
                    *  5 - Listar libros por idioma                       *
                    *                                                     *
                    *  0 - Salir                                          *
                    * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                    """;
            System.out.println(menu);
            try{
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnUnDeterminadoAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Ingrese una opción válida del menu");
                }
            } catch (InputMismatchException e){
                System.out.println("Ingrese una opcion válida.");
                teclado.nextLine();
            }

        }

    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el titulo del libro que deseas buscar: ");
        var tituloLibro = teclado.nextLine().trim();
        var json = consumoAPI.obtenerDatos(URL_BASE + URL_BUSCAR + tituloLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        DatosLibro libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst()
                .orElse(null);

        return libroBuscado;
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        if (datos != null) {
            Libro libro = new Libro(datos);
            this.libroService.guardarLibro(libro);
            mostrarDatosLibro(libro);
        } else {
            System.out.println("No se pudo encontrar el libro.");
        }

    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = this.libroService.ObtenerTodosLosLibros();
        if (!libros.isEmpty()){
            libros.forEach(this::mostrarDatosLibro);
        }else{
            System.out.println("Aun no hay libros registrados que mostrar.");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = this.autorService.ObtenerTodosLosAutores();
        if(!autores.isEmpty()){
            autores.forEach(this::mostrarDatosAutor);
        }else{
            System.out.println("Aun no hay autores registrados que mostrar.");
        }

    }

    private void listarAutoresVivosEnUnDeterminadoAnio() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar: ");
        var anio = teclado.nextLine().trim();
        try{
            List<Autor> autores = this.autorService.obtenerAutoresVivosEnUnDeterminadoAnio(Integer.parseInt(anio));
            if(!autores.isEmpty()){
                autores.forEach(this::mostrarDatosAutor);
            }else{
                System.out.println("No se encontraron autores vivos en ese año.");
            }
        }catch(NumberFormatException e){
            System.out.println("Ingrese un año valido");
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
               Ingrese el idioma para buscar los libros: 
               es - español
               en - inglés
               fr - francés
               pt - portugués
                """);
        var idioma = teclado.nextLine().trim().toLowerCase();
        List<Libro> libros = this.libroService.obtenerLibrosPorIdioma(idioma);
        if(!libros.isEmpty()){
            libros.forEach(this::mostrarDatosLibro);
        } else{
            System.out.println("No se encontraron libros en ese idioma para mostrar.");
        }
    }


    public void mostrarDatosLibro(Libro libro) {
        System.out.println("\n* * * * * LIBRO * * * * *");
        System.out.println("Titulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor().getNombre());
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
    }

    public void mostrarDatosAutor(Autor autor) {
        String libros = autor.getLibros().stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", ", "[", "]"));

        System.out.println("\nAutor: " + autor.getNombre());
        System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
        System.out.println("Fecha de Fallecimiento: " + autor.getFechaDeFallecimiento());
        System.out.println("Libros: " + libros );

    }

}
