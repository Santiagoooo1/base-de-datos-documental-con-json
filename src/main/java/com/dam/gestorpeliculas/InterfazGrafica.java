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
        modeloTabla.setColumnIdentifiers(new String[] {
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

    /*
     * IA: Método generado y revisado con asistencia de IA para añadir una película
     * validando campos obligatorios, números correctos e identificadores
     * duplicados.
     */
    private void agregarPelicula() {
        try {
            String textoId = campoId.getText().trim();
            String titulo = campoTitulo.getText().trim();
            String director = campoDirector.getText().trim();
            String genero = campoGenero.getText().trim();
            String textoAnio = campoAnio.getText().trim();
            String textoDuracion = campoDuracion.getText().trim();

            if (textoId.isEmpty() || titulo.isEmpty() || director.isEmpty()
                    || genero.isEmpty() || textoAnio.isEmpty() || textoDuracion.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Todos los campos son obligatorios.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(textoId);
            int anio = Integer.parseInt(textoAnio);
            int duracion = Integer.parseInt(textoDuracion);

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser mayor que 0.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existeId(id)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ya existe una película con ese ID.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (anio < 1888 || anio > 2100) {
                JOptionPane.showMessageDialog(
                        this,
                        "El año debe estar entre 1888 y 2100.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (duracion <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "La duración debe ser mayor que 0 minutos.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean vista = checkVista.isSelected();

            Pelicula nuevaPelicula = new Pelicula(
                    id,
                    titulo,
                    director,
                    genero,
                    anio,
                    duracion,
                    vista);

            peliculas.add(nuevaPelicula);
            gestorJson.guardarPeliculas(peliculas);
            cargarDatosEnTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Película añadida correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El ID, el año y la duración deben ser números enteros.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean existeId(int id) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoTitulo.setText("");
        campoDirector.setText("");
        campoGenero.setText("");
        campoAnio.setText("");
        campoDuracion.setText("");
        checkVista.setSelected(false);
    }

    private void cargarDatosEnTabla() {
        modeloTabla.setRowCount(0);
        for (Pelicula pelicula : peliculas) {
            modeloTabla.addRow(new Object[] {
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