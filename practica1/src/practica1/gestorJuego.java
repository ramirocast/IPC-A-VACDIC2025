package practica1;

public class gestorJuego {
    private tablero board;
    private pieza currentPiece;
    private pieza nextPiece;
    private puntuacion scoreManager;
    private boolean isPaused;
    private boolean isGameOver;
    private boolean terminadoPorUsuario;
    private String carne;
    
    // CONSTRUCTOR
    public gestorJuego(String carne) {
        this.carne = carne;
        this.board = new tablero();
        this.scoreManager = new puntuacion();
        this.nextPiece = new pieza(tetromino.getRandomPieceIndex());
        this.spawnNewPiece();
        this.isPaused = false;
        this.isGameOver = false;
        this.terminadoPorUsuario = false;
    }
    
    // MÉTODO: Generar nueva pieza
    private void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = new pieza(tetromino.getRandomPieceIndex());
        
        if (board.isGameOver(currentPiece)) {
            isGameOver = true;
        }
    }
    
    // MÉTODO: Actualizar estado del juego
    public void update() {
        if (isGameOver || isPaused || terminadoPorUsuario) {
            return;
        }
    }
    
    // MÉTODO: Manejar entrada del usuario
    public void handleInput(String input) {
        if (isGameOver || terminadoPorUsuario) {
            return;
        }
        
        switch (input.toUpperCase()) {
            case "A":
            case "LEFT":
                if (board.isValidPosition(currentPiece, -1, 0)) {
                    currentPiece.moveLeft();
                }
                break;
                
            case "D":
            case "RIGHT":
                if (board.isValidPosition(currentPiece, 1, 0)) {
                    currentPiece.moveRight();
                }
                break;
                
            case "S":
            case "DOWN":
                if (board.isValidPosition(currentPiece, 0, 1)) {
                    currentPiece.moveDown();
                    scoreManager.addSoftDropPoints(1);
                } else {
                    placeCurrentPiece();
                }
                break;
                
            case "W":
            case "UP":
                pieza rotatedPiece = currentPiece.getRotatedCopy();
                if (board.isValidPosition(rotatedPiece, 0, 0)) {
                    currentPiece.rotate();
                }
                break;
                
           case "": 
        case " ": 
            handleHardDrop();
            break;
                
            case "P":
                isPaused = !isPaused;
                break;
                
            case "ESC":
                terminadoPorUsuario = true;
                break;
                
            case "X":
                isGameOver = true;
                break;
        }
    }
    
    // MÉTODO: Manejar caída rápida (hard drop)
    private void handleHardDrop() {
        if (currentPiece == null) return;
        
        int dropDistance = 0;
        pieza tempPiece = new pieza(copyMatrix(currentPiece.getShape()), currentPiece.getType());
        tempPiece.setPosition(currentPiece.getX(), currentPiece.getY());
        
        // Calcular distancia de caída
        while (board.isValidPosition(tempPiece, 0, 1)) {
            tempPiece.moveDown();
            dropDistance++;
        }
        
        if (dropDistance > 0) {
            // Mover pieza real
            for (int i = 0; i < dropDistance; i++) {
                if (board.isValidPosition(currentPiece, 0, 1)) {
                    currentPiece.moveDown();
                }
            }
            
            // Sumar puntos
            scoreManager.addHardDropPoints(dropDistance);
            
            // Colocar pieza
            placeCurrentPiece();
        }
    }
    
    // MÉTODO: Colocar pieza actual en el tablero
    private void placeCurrentPiece() {
        board.placePiece(currentPiece);
        
        // Verificar líneas completas
        int linesCleared = board.clearLines();
        
        if (linesCleared > 0) {
            scoreManager.addLinesCleared(linesCleared);
            
            switch (linesCleared) {
                case 1:
                    System.out.printf("\n  ¡1 línea! +%d puntos\n", 100 * scoreManager.getLevel());
                    break;
                case 2:
                    System.out.printf("\n  ¡2 líneas! +%d puntos\n", 300 * scoreManager.getLevel());
                    break;
                case 3:
                    System.out.printf("\n  ¡3 líneas! +%d puntos\n", 500 * scoreManager.getLevel());
                    break;
                case 4:
                    System.out.printf("\n  ¡TETRIS! +%d puntos\n", 800 * scoreManager.getLevel());
                    break;
            }
        }
        
        spawnNewPiece();
    }
    
    // MÉTODO: Copiar matriz (auxiliar)
    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 4);
        }
        return copy;
    }
    
    // MÉTODO: Dibujar interfaz del juego
    public void draw() {
        utilidadesConsola.clearScreen();
        
        System.out.println("=========================================");
        System.out.println("     TETRIS - CARNÉ: " + carne);
        System.out.println("=========================================\n");
        
        if (isPaused) {
            System.out.println("\n\n           JUEGO EN PAUSA");
            System.out.println("     Presiona P para continuar...\n\n");
            return;
        }
        
        if (terminadoPorUsuario) {
            System.out.println("\n\n           PARTIDA CANCELADA");
            System.out.println("     Volviendo al menú principal...\n\n");
            return;
        }
        
        if (isGameOver) {
            System.out.println("\n\n           GAME OVER!");
            System.out.printf("     Puntaje Final: %d\n", scoreManager.getScore());
            System.out.println("     Nivel alcanzado: " + scoreManager.getLevel());
            System.out.println("     Líneas completadas: " + scoreManager.getTotalLines());
            System.out.println("     Presiona Enter para guardar puntaje...\n\n");
            return;
        }
        
        board.draw(currentPiece, nextPiece, scoreManager.getScore(), 
                  scoreManager.getLevel(), scoreManager.getTotalLines());
        
        System.out.println("\n  CONTROLES:");
        System.out.println(" A: Izquierda    D: Derecha");
        System.out.println(" S: Bajar (+1pt) W/↑: Rotar");
        System.out.println(" ESPACIO: Caída rápida (+2pts/celda)");
        System.out.println(" P: Pausa          X: Terminar partida");
        System.out.println("  ESC: Salir sin guardar");
        System.out.print("\n  Comando: ");
    }
    
    // --- MÉTODOS GETTER QUE ESTABAN FALTANDO ---
    
    // MÉTODO: Verificar si el juego ha terminado
    public boolean isGameOver() {
        return isGameOver || terminadoPorUsuario;
    }
    
    // MÉTODO: Verificar si el juego está en pausa
    public boolean isPaused() {
        return isPaused;
    }
    
    // MÉTODO: Verificar si fue cancelado por usuario
    public boolean fueCancelado() {
        return terminadoPorUsuario;
    }
    
    // MÉTODO: Obtener puntaje actual
    public int getScore() {
        return scoreManager.getScore();
    }
    
    // MÉTODO: Obtener total de líneas
    public int getTotalLines() {
        return scoreManager.getTotalLines();
    }
    
    // MÉTODO: Obtener nivel actual
    public int getLevel() {
        return scoreManager.getLevel();
    }
    
    // MÉTODO: Obtener conteo de Tetris
    public int getTetrisCount() {
        return scoreManager.getTetrisCount();
    }
}