package estructura;

public class administrador extends usuario {
    public administrador(String u, String p, String n) {
        super(u, p, n);
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }
}
