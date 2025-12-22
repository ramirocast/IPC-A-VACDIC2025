package controladores;

import datos.datosBiblio;
import estructura.libro;

public class controladorEstudiante {

    // ==============================
    // ===== OBTENER CATÁLOGO =======
    // ==============================
    public libro[] obtenerCatalogoLibros() {
        return datosBiblio.libros;
    }

    // ==============================
    // ===== CERRAR SESIÓN ==========
    // ==============================
    public void cerrarSesion() {
        new interfaz.login();
    }
}
