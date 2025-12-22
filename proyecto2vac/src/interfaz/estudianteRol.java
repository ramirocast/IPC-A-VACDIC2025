package interfaz;

import estructura.usuario;
import javax.swing.*;

public class estudianteRol extends JFrame {
    public estudianteRol(usuario u) {
        setTitle("Admin - " + u.getNombre());
        setSize(400,300);
        add(new JLabel("Panel Estudiante"));
        setVisible(true);
    }
}
