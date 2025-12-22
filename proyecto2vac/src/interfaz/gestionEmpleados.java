package interfaz;

import datos.datosBiblio;
import estructura.bibliotecario;

import javax.swing.*;
import java.awt.*;

public class gestionEmpleados extends JPanel {

    private JTable tablaEmpleados;
    private JTextField campoNombre, campoUsuario, campoDpi, campoNacimiento;
    private JTextField campoTelefono, campoEmail, campoSalario, campoContratacion;
    private JPasswordField campoContrasena;
    private JComboBox<String> comboTurno;

    public gestionEmpleados() {

        setLayout(new BorderLayout());

        // ===== FORMULARIO =====
        JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 5, 5));

        campoNombre = new JTextField();
        campoUsuario = new JTextField();
        campoContrasena = new JPasswordField();
        campoDpi = new JTextField();
        campoNacimiento = new JTextField();
        campoTelefono = new JTextField();
        campoEmail = new JTextField();
        campoSalario = new JTextField();
        campoContratacion = new JTextField();

        comboTurno = new JComboBox<>(new String[]{"Matutino", "Vespertino", "Nocturno"});

        panelFormulario.add(new JLabel("Nombre Completo"));
        panelFormulario.add(campoNombre);

        panelFormulario.add(new JLabel("Usuario"));
        panelFormulario.add(campoUsuario);

        panelFormulario.add(new JLabel("Contraseña"));
        panelFormulario.add(campoContrasena);

        panelFormulario.add(new JLabel("DPI"));
        panelFormulario.add(campoDpi);

        panelFormulario.add(new JLabel("Fecha Nacimiento"));
        panelFormulario.add(campoNacimiento);

        panelFormulario.add(new JLabel("Teléfono"));
        panelFormulario.add(campoTelefono);

        panelFormulario.add(new JLabel("Email"));
        panelFormulario.add(campoEmail);

        panelFormulario.add(new JLabel("Turno"));
        panelFormulario.add(comboTurno);

        panelFormulario.add(new JLabel("Salario"));
        panelFormulario.add(campoSalario);

        panelFormulario.add(new JLabel("Fecha Contratación"));
        panelFormulario.add(campoContratacion);

        JButton botonCrear = new JButton("Crear Empleado");
        panelFormulario.add(botonCrear);

        add(panelFormulario, BorderLayout.WEST);

        // ===== TABLA =====
        tablaEmpleados = new JTable();
        actualizarTabla();
        add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        // ===== EVENTO CREAR =====
        botonCrear.addActionListener(e -> crearEmpleado());
    }

    private void crearEmpleado() {

        if (datosBiblio.usuarioExiste(campoUsuario.getText())) {
            JOptionPane.showMessageDialog(this, "Usuario ya existe");
            return;
        }

        bibliotecario nuevo = new bibliotecario(
                datosBiblio.generarIdEmpleado(),
                campoNombre.getText(),
                campoUsuario.getText(),
                new String(campoContrasena.getPassword()),
                campoDpi.getText(),
                campoNacimiento.getText(),
                campoTelefono.getText(),
                campoEmail.getText(),
                comboTurno.getSelectedItem().toString(),
                Double.parseDouble(campoSalario.getText()),
                campoContratacion.getText()
        );

        datosBiblio.empleados[datosBiblio.totalEmpleados++] = nuevo;
        actualizarTabla();
        limpiarCampos();
    }

    private void actualizarTabla() {
        String[] columnas = {"ID", "Nombre", "Usuario", "Turno", "Estado"};
        Object[][] datos = new Object[datosBiblio.totalEmpleados][5];

        for (int i = 0; i < datosBiblio.totalEmpleados; i++) {
            bibliotecario b = datosBiblio.empleados[i];
            datos[i][0] = b.getIdEmpleado();
            datos[i][1] = b.getUsuario();
            datos[i][2] = b.getUsuario();
            datos[i][3] = b.isActivo() ? "Activo" : "Inactivo";
        }

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
    }

    private void limpiarCampos() {
        campoNombre.setText("");
        campoUsuario.setText("");
        campoContrasena.setText("");
        campoDpi.setText("");
        campoNacimiento.setText("");
        campoTelefono.setText("");
        campoEmail.setText("");
        campoSalario.setText("");
        campoContratacion.setText("");
    }
}
