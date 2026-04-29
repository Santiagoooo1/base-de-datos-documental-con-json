package com.dam.gestorpeliculas;
import java.util.ArrayList;

public class PeliculasEnvoltorio {
    private ArrayList<Pelicula> peliculas;

    public PeliculasEnvoltorio() {
        this.peliculas = new ArrayList<>();
    }

    public PeliculasEnvoltorio(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }
}