package practica1;

import java.util.Scanner;

public class main {
    private static final String CARNE = "202000127"; // CAMBIA POR TU CARNÉ
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            mostrarMenuPrincipal(scanner);
        }
    }
    
    private static void mostrarMenuPrincipal(Scanner scanner) {
        utilidadesConsola.clearScreen();
        System.out.println("=========================================");
        System.out.println("     TETRIS - CARNÉ: " + CARNE);
        System.out.println("=========================================\n");
        
        System.out.println("         MENÚ PRINCIPAL");
        System.out.println("        ────────────────");
        System.out.println("        1. Jugar Partida Nueva");
        System.out.println("        2. Ver Mejores Puntajes");
        System.out.println("        3. Ver Estadísticas Generales");
        System.out.println("        4. Sistema de Puntuación");
        System.out.println("        5. Salir del Juego");
        System.out.print("\n        Seleccione una opción (1-5): ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                iniciarJuego(scanner);
                break;
                
            case "2":
                mostrarPuntajes(scanner);
                break;
                
            case "3":
                mostrarEstadisticas(scanner);
                break;
                
            case "4":
                mostrarSistemaPuntuacion(scanner);
                break;
                
            case "5":
                System.out.println("\n¡Gracias por jugar Tetris!");
                scanner.close();
                System.exit(0);
                break;
                
            default:
                System.out.println("\nOpción inválida. Presiona Enter...");
                scanner.nextLine();
        }
    }
    
    // MÉTODO 1: iniciarJuego - DEFINIDO
    private static void iniciarJuego(Scanner scanner) {
        utilidadesConsola.clearScreen();
        System.out.println("        INICIANDO NUEVA PARTIDA");
        System.out.println("        ───────────────────────");
        System.out.println("        CONTROLES:");
        System.out.println("        • A : Mover izquierda");
        System.out.println("        • D : Mover derecha");
        System.out.println("        • S : Bajar (+1 punto por celda)");
        System.out.println("        • W : Rotar pieza");
        System.out.println("        • ESPACIO : Caída rápida (+2 puntos por celda)");
        System.out.println("        • P : Pausa");
        System.out.println("        • X : Terminar partida y guardar");
        System.out.println("        • ESC : Salir sin guardar");
        System.out.println("");
        System.out.println("        Presiona Enter para comenzar...");
        scanner.nextLine();
        
        juegoPrincipal juego = new juegoPrincipal(CARNE);
        juego.start();
    }
    
    // MÉTODO 2: mostrarPuntajes - DEFINIDO
    private static void mostrarPuntajes(Scanner scanner) {
        utilidadesConsola.clearScreen();
        System.out.println("=========================================");
        System.out.println("        MEJORES PUNTAJES");
        System.out.println("=========================================\n");
        
        // Leer puntajes del archivo
        String[] puntajes = gestorArchivos.readHighScores();
        
        if (puntajes == null || puntajes.length == 0 || 
            (puntajes.length == 1 && puntajes[0].contains("No hay"))) {
            System.out.println("        No hay puntajes registrados aún.");
            System.out.println("        ¡Juega para ser el primero!");
        } else {
            System.out.println("        Posición  Jugador          Puntos  Nivel  Líneas");
            System.out.println("        ────────────────────────────────────────────────");
            
            for (int i = 0; i < puntajes.length; i++) {
                if (puntajes[i] != null && !puntajes[i].trim().isEmpty()) {
                    System.out.println("        " + puntajes[i]);
                }
            }
        }
        
        System.out.println("\n        Presiona Enter para volver al menú...");
        scanner.nextLine();
    }
    
    // MÉTODO 3: mostrarEstadisticas - DEFINIDO
    private static void mostrarEstadisticas(Scanner scanner) {
        utilidadesConsola.clearScreen();
        System.out.println("=========================================");
        System.out.println("     ESTADÍSTICAS GENERALES");
        System.out.println("=========================================\n");
        
        // Leer estadísticas del archivo
        String[] stats = gestorArchivos.readStatistics();
        
        if (stats == null || stats.length == 0) {
            System.out.println("    No hay estadísticas disponibles.");
            System.out.println("    Juega algunas partidas primero.");
        } else {
            for (String estadistica : stats) {
                if (estadistica != null && !estadistica.trim().isEmpty()) {
                    System.out.println("    " + estadistica);
                }
            }
        }
        
        System.out.println("\n    Presiona Enter para volver al menú...");
        scanner.nextLine();
    }
    
    // MÉTODO 4: mostrarSistemaPuntuacion - DEFINIDO
    private static void mostrarSistemaPuntuacion(Scanner scanner) {
        utilidadesConsola.clearScreen();
        System.out.println("        SISTEMA DE PUNTUACIÓN");
        System.out.println("        ──────────────────────");
        System.out.println("        1. LÍNEAS ELIMINADAS:");
        System.out.println("           • 1 línea:   100 puntos × nivel");
        System.out.println("           • 2 líneas:  300 puntos × nivel");
        System.out.println("           • 3 líneas:  500 puntos × nivel");
        System.out.println("           • 4 líneas:  800 puntos × nivel (TETRIS)");
        System.out.println("");
        System.out.println("        2. BAJAR PIEZAS:");
        System.out.println("           • Tecla S:     +1 punto por cada celda");
        System.out.println("           • ESPACIO:     +2 puntos por cada celda");
        System.out.println("");
        System.out.println("        3. SISTEMA DE NIVELES:");
        System.out.println("           • Nivel inicial: 1");
        System.out.println("           • Sube de nivel cada 10 líneas");
        System.out.println("           • Los puntos se multiplican por el nivel");
        System.out.println("");
        System.out.println("        4. FIN DEL JUEGO:");
        System.out.println("           • Cuando una nueva pieza no puede colocarse");
        System.out.println("           • Se guarda el puntaje con tu nombre");
        System.out.println("");
        System.out.println("        Presiona Enter para volver al menú...");
        scanner.nextLine();
    }
}