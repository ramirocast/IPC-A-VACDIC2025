package estructura;

public abstract class usuario {
    protected String usuario;
    protected String contrasena;
    protected String nombre;
    protected boolean activo;

    public usuario(String usuario, String contrasena, String nombre) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.activo = true;
    }

    public boolean validarLogin(String u, String p) {
        return usuario.equals(u) && contrasena.equals(p) && activo;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract String getRol();
}
