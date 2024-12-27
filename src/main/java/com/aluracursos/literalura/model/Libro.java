package com.aluracursos.literalura.model;

import jakarta.persistence.*;


@Entity
@Table(name = "libros")

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Double numeroDeDescargas;
    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().get(0);
        try {
            this.idioma = datosLibro.idiomas().get(0);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            this.idioma = "Desconocido";
        }
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        try {
            this.autor = new Autor(datosLibro.autor().get(0));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            this.autor = null;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numeroDeDescargas=" + numeroDeDescargas +
                ", autor=" + autor;
    }
}
