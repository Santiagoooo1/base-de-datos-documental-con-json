package com.dam.gestorpeliculas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InterfazGrafica extends JFrame {

    private GestorJson gestorJson;
    private ArrayList<Pelicula> peliculas;
    private JTable tablaPeliculas;
    private DefaultTableModel modeloTabla;

    private JTextField campoId;
    private JTextField campoTitulo;
    private JTextField campoDirector;
    private JTextField campoGenero;
    private JTextField campoAnio;
    private JTextField campoDuracion;
    private JCheckBox checkVista;

    private JButton botonAgregar;
    private JButton botonBuscar;
    private JButton botonModificar;
    private JButton botonEliminar;
    private JButton botonLimpiar;

    public InterfazGrafica() {
        setTitle("Gestor de Películas");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gestorJson = new GestorJson();
        peliculas = gestorJson.cargarPeliculas();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gestor de Películas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{
                "ID", "Título", "Director", "Género", "Año", "Duración", "Vista"
        });

        tablaPeliculas = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPeliculas);
        add(scrollTabla, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 5, 5));

        campoId = new JTextField();
        campoTitulo = new JTextField();
        campoDirector = new JTextField();
        campoGenero = new JTextField();
        campoAnio = new JTextField();
        campoDuracion = new JTextField();
        checkVista = new JCheckBox("Vista");

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(campoId);

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(campoTitulo);

        panelFormulario.add(new JLabel("Director:"));
        panelFormulario.add(campoDirector);

        panelFormulario.add(new JLabel("Género:"));
        panelFormulario.add(campoGenero);

        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(campoAnio);

        panelFormulario.add(new JLabel("Duración minutos:"));
        panelFormulario.add(campoDuracion);

        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(checkVista);

        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 5, 5));

        botonAgregar = new JButton("Agregar");
        botonAgregar.addActionListener(e -> agregarPelicula());
        botonBuscar = new JButton("Buscar");
        botonModificar = new JButton("Modificar");
        botonEliminar = new JButton("Eliminar");
        botonLimpiar = new JButton("Limpiar");

        panelBotones.add(botonAgregar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonLimpiar);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelFormulario, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);
        cargarDatosEnTabla();
    }
    private void agregarPelicula() {
    try {
        int id = Integer.parseInt(campoId.getText().trim());
        String titulo = campoTitulo.getText().trim();
        String director = campoDirector.getText().trim();
        String genero = campoGenero.getText().trim();
        int anio = Integer.parseInt(campoAnio.getText().trim());
        int duracion = Integer.parseInt(campoDuracion.getText().trim());
        boolean vista = checkVista.isSelected();

        Pelicula nuevaPelicula = new Pelicula(
                id, titulo, director, genero, anio, duracion, vista
        );
        
        peliculas.add(nuevaPelicula);
        gestorJson.guardarPeliculas(peliculas);
        cargarDatosEnTabla();

        JOptionPane.showMessageDialog(
                this,
                "Película añadida correctamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
                this,
                "El ID, el año y la duración deben ser números.",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    private void cargarDatosEnTabla(){
        modeloTabla.setRowCount(0);
        for (Pelicula pelicula : peliculas) {
            modeloTabla.addRow(new Object[]{
                    pelicula.getId(),
                    pelicula.getTitulo(),
                    pelicula.getDirector(),
                    pelicula.getGenero(),
                    pelicula.getAnio(),
                    pelicula.getDuracionMinutos(),
                    pelicula.isVista() ? "Sí" : "No"
            });
        }
    }
}