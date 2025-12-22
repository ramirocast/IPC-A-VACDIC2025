package estructura;

public class bibliotecario {

    private int idEmpleado;
    private String nombreCompleto;
    private String usuario;
    private String contrasena;
    private String dpi;
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String turno;
    private double salario;
    private String fechaContratacion;
    private boolean activo;
    private int totalPrestamos;

    public bibliotecario(int idEmpleado, String nombreCompleto, String usuario,
                          String contrasena, String dpi, String fechaNacimiento,
                          String telefono, String email, String turno,
                          double salario, String fechaContratacion) {

        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.dpi = dpi;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.turno = turno;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = true;
        this.totalPrestamos = 0;
    }

    // ===== Getters y setters =====
    public int getIdEmpleado() { return idEmpleado; }
    public String getUsuario() { return usuario; }
    public boolean isActivo() { return activo; }
    public int getTotalPrestamos() { return totalPrestamos; }

    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setTurno(String turno) { this.turno = turno; }
    public void setSalario(double salario) { this.salario = salario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
