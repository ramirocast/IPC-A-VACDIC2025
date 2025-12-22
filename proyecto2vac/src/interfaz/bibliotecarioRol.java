package interfaz;

import estructura.usuario;
import javax.swing.*;

public class bibliotecarioRol extends JFrame {
    public bibliotecarioRol(usuario u) {
        setTitle("Admin - " + u.getNombre());
        setSize(400,300);
        add(new JLabel("Panel Bibliotecario"));
        setVisible(true);
    }
}
