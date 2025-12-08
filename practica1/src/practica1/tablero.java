package practica1;

public class tablero {
    // CONSTANTES
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final char EMPTY_CELL = '·';
    private static final char FILLED_CELL = '█';
    
    private char[][] grid;
    
    // CONSTRUCTOR
    public tablero() {
        grid = new char[ROWS][COLS];
        clear();
    }
    
    // Limpiar el tablero
    public void clear() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = EMPTY_CELL;
            }
        }
    }
    
    // Verificar si una posición es válida para una pieza
    public boolean isValidPosition(pieza piece, int offsetX, int offsetY) {
        int[][] shape = piece.getShape();
        int pieceX = piece.getX() + offsetX;
        int pieceY = piece.getY() + offsetY;
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j] == 1) {
                    int boardX = pieceX + j;
                    int boardY = pieceY + i;
                    
                    // Verificar bordes
                    if (boardX < 0 || boardX >= COLS || boardY >= ROWS) {
                        return false;
                    }
                    
                    // Verificar colisión con piezas existentes
                    if (boardY >= 0 && grid[boardY][boardX] != EMPTY_CELL) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    // Colocar una pieza en el tablero
    public void placePiece(pieza piece) {
        int[][] shape = piece.getShape();
        int pieceX = piece.getX();
        int pieceY = piece.getY();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j] == 1) {
                    int boardX = pieceX + j;
                    int boardY = pieceY + i;
                    
                    if (boardY >= 0 && boardY < ROWS && boardX >= 0 && boardX < COLS) {
                        grid[boardY][boardX] = FILLED_CELL;
                    }
                }
            }
        }
    }
    
    // Eliminar líneas completas
    public int clearLines() {
        int linesCleared = 0;
        
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean fullLine = true;
            
            // Verificar si la fila está completa
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == EMPTY_CELL) {
                    fullLine = false;
                    break;
                }
            }
            
            // Si la fila está completa, eliminarla
            if (fullLine) {
                linesCleared++;
                
                // Mover todas las filas superiores hacia abajo
                for (int r = row; r > 0; r--) {
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, COLS);
                }
                
                // Limpiar la fila superior
                for (int col = 0; col < COLS; col++) {
                    grid[0][col] = EMPTY_CELL;
                }
                
                // Revisar la misma fila nuevamente
                row++;
            }
        }
        
        return linesCleared;
    }
    
    // Dibujar el tablero - MÉTODO draw COMPLETO
    public void draw(pieza currentPiece, pieza nextPiece, int score, int level, int lines) {
        System.out.println("\n  TABLERO DE JUEGO");
        System.out.println("  " + "═".repeat(COLS * 2 + 2));
        
        // Crear display temporal
        char[][] display = new char[ROWS][COLS];
        
        // Copiar tablero actual
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(grid[i], 0, display[i], 0, COLS);
        }
        
        // Añadir pieza actual (solo si la celda está vacía)
        if (isValidPosition(currentPiece, 0, 0)) {
            int[][] shape = currentPiece.getShape();
            int pieceX = currentPiece.getX();
            int pieceY = currentPiece.getY();
            
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (shape[i][j] == 1) {
                        int boardX = pieceX + j;
                        int boardY = pieceY + i;
                        
                        if (boardY >= 0 && boardY < ROWS && boardX >= 0 && boardX < COLS) {
                            // Solo poner si está vacío
                            if (grid[boardY][boardX] == EMPTY_CELL) {
                                display[boardY][boardX] = FILLED_CELL;
                            }
                        }
                    }
                }
            }
        }
        
        // Dibujar tablero
        for (int i = 0; i < ROWS; i++) {
            System.out.print("  ║");
            for (int j = 0; j < COLS; j++) {
                System.out.print(display[i][j] + "" + display[i][j]);
            }
            System.out.println("║");
        }
        System.out.println("  " + "═".repeat(COLS * 2 + 2));
        
        // Panel de información
        System.out.println("\n  INFORMACIÓN");
        System.out.println("  " + "─".repeat(20));
        System.out.printf("  Puntaje: %d\n", score);
        System.out.printf("  Nivel: %d\n", level);
        System.out.printf("  Líneas: %d\n", lines);
        
        // Siguiente pieza
        System.out.println("\n  SIGUIENTE PIEZA:");
        int[][] nextShape = nextPiece.getShape();
        System.out.println("  ┌──────┐");
        for (int i = 0; i < 4; i++) {
            System.out.print("  │");
            for (int j = 0; j < 4; j++) {
                System.out.print(nextShape[i][j] == 1 ? "██" : "  ");
            }
            System.out.println("│");
        }
        System.out.println("  └──────┘");
    }
    
    // Método isGameOver - Verificar si el juego ha terminado
    public boolean isGameOver(pieza piece) {
        // El juego termina si la pieza no puede colocarse en su posición inicial
        return !isValidPosition(piece, 0, 0);
    }
    
    // Métodos adicionales útiles
    public int getRows() {
        return ROWS;
    }
    
    public int getCols() {
        return COLS;
    }
    
    // Método para depuración: mostrar el tablero en texto
    public void printDebug() {
        System.out.println("\n[DEBUG] Estado del tablero:");
        for (int i = 0; i < ROWS; i++) {
            System.out.print("Fila " + i + ": ");
            for (int j = 0; j < COLS; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}