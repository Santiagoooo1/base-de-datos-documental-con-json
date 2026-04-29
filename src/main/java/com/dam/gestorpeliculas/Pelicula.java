package com.dam.gestorpeliculas;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private String genero;
    private int anio;
    private int duracionMinutos;
    private boolean vista;

    public Pelicula (int id, String titulo, String director, String genero, int anio, int duracionMinutos, boolean vista) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.genero = genero;
        this.anio = anio;
        this.duracionMinutos = duracionMinutos;
        this.vista = vista;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public String getGenero() {
        return genero;
    }

    public int getAnio() {
        return anio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public boolean isVista() {
        return vista;
    }

    @Override
    public String toString() {
        return id + " - " + titulo + " (" + anio + ") - " + genero;
    }
}
