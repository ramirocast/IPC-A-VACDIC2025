package utilidades;

import estructura.libro;

public class recursividad {

    public static libro buscarISBN(libro[] libros, int i, String isbn) {
        if (i < 0) return null;
        if (libros[i].isbn.equals(isbn)) return libros[i];
        return buscarISBN(libros, i - 1, isbn);
    }
}
