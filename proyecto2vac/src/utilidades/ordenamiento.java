package utilidades;

import estructura.libro;

public class ordenamiento {

    public static void ordenarPorTitulo(libro[] libros, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (libros[j].titulo.compareTo(libros[j + 1].titulo) > 0) {
                    libro temp = libros[j];
                    libros[j] = libros[j + 1];
                    libros[j + 1] = temp;
                }
            }
        }
    }
}
