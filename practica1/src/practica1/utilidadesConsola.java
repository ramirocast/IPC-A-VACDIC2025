package practica1;

public class utilidadesConsola {
    // Códigos ANSI para colores
    public static final String RESET = "\033[0m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String RED = "\033[31m";
    public static final String CYAN = "\033[36m";
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void hideCursor() {
        System.out.print("\033[?25l");
    }
    
    public static void showCursor() {
        System.out.print("\033[?25h");
    }
    
    public static void printColor(String text, String color) {
        System.out.print(color + text + RESET);
    }
}