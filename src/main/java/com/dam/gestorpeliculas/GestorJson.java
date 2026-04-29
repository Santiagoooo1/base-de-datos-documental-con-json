package com.dam.gestorpeliculas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class GestorJson {
    private static final String RUTA_ARCHIVO = "peliculas.json";
    private Gson gson;

    public GestorJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void guardarPeliculas(ArrayList<Pelicula> peliculas) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(peliculas, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar las películas: " + e.getMessage());
        }
    }

    public ArrayList<Pelicula> cargarPeliculas() {
        ArrayList<Pelicula> peliculas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (archivo.exists()) {
            try (FileReader reader = new FileReader(archivo)) {
                Pelicula[] peliculasArray = gson.fromJson(reader, Pelicula[].class);
                if (peliculasArray != null) {
                    for (Pelicula pelicula : peliculasArray) {
                        peliculas.add(pelicula);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al cargar las películas: " + e.getMessage());
            }
        }
        return peliculas;
    }
    
}
