package estructura;

public class libro {
    public String isbn;
    public String titulo;
    public boolean disponible;

    public libro(String isbn, String titulo) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.disponible = true;
    }
}
