package interfaz;

import estructura.usuario;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class administradorRol extends JFrame {

    // ===== Variables de instancia =====
    private JPanel panelCentral;
    private JLabel etiquetaEstado;
    private JLabel etiquetaFechaHora;

    public administradorRol(usuario usuario) {

        // ===== Configuración de la ventana =====
        setTitle("Sistema de Gestión Bibliotecaria - Administrador");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== 1. Barra de menú superior =====
        JMenuBar barraMenu = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuGestion = new JMenu("Gestión");
        JMenu menuReportes = new JMenu("Reportes");
        JMenu menuAyuda = new JMenu("Ayuda");

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
        JMenuItem itemSalir = new JMenuItem("Salir");

        menuArchivo.add(itemCerrarSesion);
        menuArchivo.add(itemSalir);

        menuGestion.add(new JMenuItem("Empleados"));
        menuGestion.add(new JMenuItem("Libros"));
        menuGestion.add(new JMenuItem("Estudiantes"));

        menuReportes.add(new JMenuItem("Reporte General"));
        menuAyuda.add(new JMenuItem("Acerca de"));

        barraMenu.add(menuArchivo);
        barraMenu.add(menuGestion);
        barraMenu.add(menuReportes);
        barraMenu.add(menuAyuda);

        setJMenuBar(barraMenu);

        // ===== 2. Panel lateral izquierdo =====
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new GridLayout(6, 1, 5, 5));
        panelLateral.setPreferredSize(new Dimension(200, 0));
        panelLateral.setBackground(new Color(230, 230, 230));

        JButton botonEmpleados = new JButton("Empleados");
        JButton botonLibros = new JButton("Libros");
        JButton botonEstudiantes = new JButton("Estudiantes");
        JButton botonCargaMasiva = new JButton("Carga CSV");
        JButton botonReportes = new JButton("Reportes");

        panelLateral.add(botonEmpleados);
        panelLateral.add(botonLibros);
        panelLateral.add(botonEstudiantes);
        panelLateral.add(botonCargaMasiva);
        panelLateral.add(botonReportes);

        // ===== 3. Área central =====
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(
                new JLabel("Bienvenido al Panel del Administrador", SwingConstants.CENTER),
                BorderLayout.CENTER
        );

        // ===== 4. Barra inferior =====
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEtchedBorder());

        JLabel etiquetaUsuario = new JLabel("Usuario: " + usuario.getNombre());
        etiquetaFechaHora = new JLabel();
        etiquetaEstado = new JLabel("Estado del sistema: Activo");

        panelInferior.add(etiquetaUsuario, BorderLayout.WEST);
        panelInferior.add(etiquetaFechaHora, BorderLayout.CENTER);
        panelInferior.add(etiquetaEstado, BorderLayout.EAST);

        actualizarFechaHora();

        // ===== Eventos =====
        botonEmpleados.addActionListener(e -> cargarModulo("Gestión de Empleados"));
        botonLibros.addActionListener(e -> cargarModulo("Gestión de Libros"));
        botonEstudiantes.addActionListener(e -> cargarModulo("Gestión de Estudiantes"));
        botonCargaMasiva.addActionListener(e -> cargarModulo("Carga Masiva de Datos CSV"));
        botonReportes.addActionListener(e -> cargarModulo("Reportes y Estadísticas"));

        // ===== Agregar componentes a la ventana =====
        add(panelLateral, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

private void cargarModulo(String nombreModulo) {
    panelCentral.removeAll();

    switch (nombreModulo) {
        case "Gestión de Empleados":
            panelCentral.add(new gestionEmpleados(), BorderLayout.CENTER);
            break;

        default:
            panelCentral.add(
                new JLabel(nombreModulo, SwingConstants.CENTER),
                BorderLayout.CENTER
            );
    }

    panelCentral.revalidate();
    panelCentral.repaint();
}


    // ===== Método para actualizar fecha y hora =====
    private void actualizarFechaHora() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        etiquetaFechaHora.setText("Fecha/Hora: " + formatoFecha.format(new Date()));
    }
}
