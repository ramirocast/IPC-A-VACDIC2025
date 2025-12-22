package datos;

import estructura.usuario;
import estructura.administrador;
import estructura.libro;



public class datosBiblio {

    // ===== USUARIOS DEL SISTEMA =====
    public static usuario[] usuarios = new usuario[50];
    public static int totalUsuarios = 0;

    // ===== EMPLEADOS (BIBLIOTECARIOS) =====
    public static estructura.bibliotecario[] empleados = new estructura.bibliotecario[50];
    public static int totalEmpleados = 0;
    private static int contadorId = 1;
    
    public static libro[] libros = new libro[100];
public static int totalLibros = 0;

    static {
        // ADMIN POR DEFECTO
        usuarios[totalUsuarios++] = new administrador("admin", "123", "Administrador");
    }

    // ===== ID AUTOGENERADO =====
    public static int generarIdEmpleado() {
        return contadorId++;
    }

    public static boolean usuarioExiste(String usuario) {
        for (int i = 0; i < totalEmpleados; i++) {
            if (empleados[i].getUsuario().equals(usuario)) {
                return true;
            }
        }
        return false;
    }
}

