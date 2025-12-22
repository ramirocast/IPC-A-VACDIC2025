package estructura;

public class estudiante extends usuario {
    private String carne;

    public estudiante(String u, String p, String n, String carne) {
        super(u, p, n);
        this.carne = carne;
    }

    public String getCarne() {
        return carne;
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }
}
