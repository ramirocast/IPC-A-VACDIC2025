package practica1;

import java.util.Scanner;

public class juegoPrincipal {
    private gestorJuego gameManager;
    private long startTime;
    private String carne;
    
    public juegoPrincipal(String carne) {
        this.carne = carne;
    }
    
    public void start() {
        gameManager = new gestorJuego(carne);
        startTime = System.currentTimeMillis();
        
        Scanner scanner = new Scanner(System.in);
        utilidadesConsola.hideCursor();
        
        while (!gameManager.isGameOver()) {
            gameManager.draw();
            
            System.out.flush();
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("ESC")) {
                System.out.println("\n  Cancelando partida...");
                utilidadesConsola.showCursor();
                return;
            }
            
            // IMPORTANTE: La entrada debe capturar ESPACIO correctamente
            gameManager.handleInput(input);
            gameManager.update();
        }
        
        utilidadesConsola.showCursor();
        
        if (!gameManager.fueCancelado()) {
            saveScore(scanner);
        }
    }
    
    private void saveScore(Scanner scanner) {
        System.out.print("\n  ¿Deseas guardar tu puntaje? (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        
        if (respuesta.equals("S") || respuesta.equals("SI")) {
            System.out.print("  Ingresa tu nombre (máx. 15 caracteres): ");
            String playerName = scanner.nextLine().trim();
            
            if (playerName.length() > 15) {
                playerName = playerName.substring(0, 15);
            }
            
            if (playerName.isEmpty()) {
                playerName = "Jugador";
            }
            
            long gameTime = (System.currentTimeMillis() - startTime) / 1000;
            
            gestorArchivos.saveScore(
                playerName,
                gameManager.getScore(),
                gameManager.getLevel(),
                gameManager.getTotalLines(),
                gameTime
            );
            
            System.out.println("\n  ¡Puntaje guardado exitosamente!");
        } else {
            System.out.println("\n  Puntaje no guardado.");
        }
        
        System.out.println("  Presiona Enter para volver al menú...");
        scanner.nextLine();
    }
}