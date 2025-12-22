package interfaz;

import datos.datosBiblio;
import estructura.usuario;

import javax.swing.*;

public class login extends JFrame {

    public login() {
        setTitle("Login Biblioteca");
        setSize(300,200);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Ingresar");

        user.setBounds(80,20,120,25);
        pass.setBounds(80,60,120,25);
        login.setBounds(90,110,100,25);

        add(new JLabel("Usuario")).setBounds(20,20,60,25);
        add(new JLabel("Clave")).setBounds(20,60,60,25);
        add(user);
        add(pass);
        add(login);

        login.addActionListener(e -> {
            for (int i = 0; i < datosBiblio.totalUsuarios; i++) {
                usuario u = datosBiblio.usuarios[i];
                if (u.validarLogin(user.getText(), new String(pass.getPassword()))) {
                    abrirPanel(u);
                    dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
        });

        setVisible(true);
    }

    private void abrirPanel(usuario u) {
        switch (u.getRol()) {
            case "ADMIN" -> new administradorRol(u);
            case "BIBLIOTECARIO" -> new bibliotecarioRol(u);
            case "ESTUDIANTE" -> new estudianteRol(u);
        }
    }
}
