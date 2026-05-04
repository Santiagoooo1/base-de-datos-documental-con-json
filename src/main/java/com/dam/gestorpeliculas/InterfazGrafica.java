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
    private boolean limpiandoCampos = false;

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
        tablaPeliculas.getSelectionModel().addListSelectionListener(e -> cargarPeliculaSeleccionada());
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
        botonBuscar.addActionListener(e -> buscarPelicula());
        botonModificar = new JButton("Modificar");
        botonModificar.addActionListener(e -> modificarPelicula());
        botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(e -> eliminarPelicula());
        botonLimpiar = new JButton("Limpiar");
        botonLimpiar.addActionListener(e -> limpiarCampos());

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
        limpiandoCampos = true;

        campoId.setText("");
        campoTitulo.setText("");
        campoDirector.setText("");
        campoGenero.setText("");
        campoAnio.setText("");
        campoDuracion.setText("");
        checkVista.setSelected(false);
        tablaPeliculas.clearSelection();

        limpiandoCampos = false;
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

    private void buscarPelicula() {
        String textoBusqueda = JOptionPane.showInputDialog(
                this,
                "Introduce ID, título, director, género o año:",
                "Buscar película",
                JOptionPane.QUESTION_MESSAGE);

        if (textoBusqueda == null) {
            return;
        }

        textoBusqueda = textoBusqueda.trim().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes introducir un texto de búsqueda.",
                    "Error de búsqueda",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);

        for (Pelicula pelicula : peliculas) {
            boolean coincide = String.valueOf(pelicula.getId()).equals(textoBusqueda)
                    || textoSeguro(pelicula.getTitulo()).contains(textoBusqueda)
                    || textoSeguro(pelicula.getDirector()).contains(textoBusqueda)
                    || textoSeguro(pelicula.getGenero()).contains(textoBusqueda)
                    || String.valueOf(pelicula.getAnio()).equals(textoBusqueda);

            if (coincide) {
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

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron películas con ese criterio.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarDatosEnTabla();
        }
    }

    private String textoSeguro(String texto) {
        return texto == null ? "" : texto.toLowerCase();
    }

    private void cargarPeliculaSeleccionada() {
        if (limpiandoCampos) {
            return;
        }

        int fila = tablaPeliculas.getSelectedRow();

        if (fila >= 0 && !tablaPeliculas.getSelectionModel().getValueIsAdjusting()) {
            campoId.setText(valorTablaSeguro(fila, 0));
            campoTitulo.setText(valorTablaSeguro(fila, 1));
            campoDirector.setText(valorTablaSeguro(fila, 2));
            campoGenero.setText(valorTablaSeguro(fila, 3));
            campoAnio.setText(valorTablaSeguro(fila, 4));
            campoDuracion.setText(valorTablaSeguro(fila, 5));

            String vista = valorTablaSeguro(fila, 6);
            checkVista.setSelected(vista.equals("Sí"));
        }
    }

    private void modificarPelicula() {
        try {
            int filaSeleccionada = tablaPeliculas.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debes seleccionar una película de la tabla.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
            boolean vista = checkVista.isSelected();

            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "El ID debe ser mayor que 0.");
                return;
            }

            if (anio < 1888 || anio > 2100) {
                JOptionPane.showMessageDialog(this, "El año debe estar entre 1888 y 2100.");
                return;
            }

            if (duracion <= 0) {
                JOptionPane.showMessageDialog(this, "La duración debe ser mayor que 0.");
                return;
            }

            int idOriginal = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());

            for (Pelicula pelicula : peliculas) {
                if (pelicula.getId() == id && id != idOriginal) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ya existe otra película con ese ID.",
                            "Error de validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            for (Pelicula pelicula : peliculas) {
                if (pelicula.getId() == idOriginal) {
                    pelicula.setId(id);
                    pelicula.setTitulo(titulo);
                    pelicula.setDirector(director);
                    pelicula.setGenero(genero);
                    pelicula.setAnio(anio);
                    pelicula.setDuracionMinutos(duracion);
                    pelicula.setVista(vista);
                    break;
                }
            }

            gestorJson.guardarPeliculas(peliculas);
            cargarDatosEnTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Película modificada correctamente.",
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

    private void eliminarPelicula() {
        int filaSeleccionada = tablaPeliculas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una película de la tabla.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar esta película?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());

        peliculas.removeIf(pelicula -> pelicula.getId() == id);

        gestorJson.guardarPeliculas(peliculas);
        cargarDatosEnTabla();
        limpiarCampos();

        JOptionPane.showMessageDialog(
                this,
                "Película eliminada correctamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String valorTablaSeguro(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }
}