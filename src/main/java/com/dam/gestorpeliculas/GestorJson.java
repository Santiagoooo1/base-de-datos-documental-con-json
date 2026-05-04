package com.dam.gestorpeliculas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GestorJson {

    private static final String RUTA_JSON = "peliculas.json";
    private final Gson gson;

    public GestorJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public ArrayList<Pelicula> cargarPeliculas() {
        File archivo = new File(RUTA_JSON);

        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {
            PeliculasEnvoltorio datos = gson.fromJson(reader, PeliculasEnvoltorio.class);

            if (datos == null || datos.getPeliculas() == null) {
                return new ArrayList<>();
            }

            return datos.getPeliculas();

        } catch (JsonSyntaxException e) {
            System.out.println("Error: el archivo JSON tiene un formato incorrecto.");
            return new ArrayList<>();

        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarPeliculas(ArrayList<Pelicula> peliculas) {
        PeliculasEnvoltorio datos = new PeliculasEnvoltorio(peliculas);

        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(datos, writer);

        } catch (IOException e) {
            System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
        }
    }
}