/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica1;

/**
 *
 * @author ramirito
 */
public class pieza {
   
    private int[][] shape;
    private int x, y;
    private char type;
    
    public pieza(int pieceIndex) {
        this.shape = tetromino.getPiece(pieceIndex);
        this.type = tetromino.getPieceType(pieceIndex);
        this.x = 3; // Columna inicial (centro del tablero 10 columnas)
        this.y = 0; // Fila inicial
    }
    
    public pieza(int[][] shape, char type) {
        this.shape = shape;
        this.type = type;
        this.x = 3;
        this.y = 0;
    }
    
    public int[][] getShape() {
        return shape;
    }
    
    public char getType() {
        return type;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void moveLeft() {
        x--;
    }
    
    public void moveRight() {
        x++;
    }
    
    public void moveDown() {
        y++;
    }
    
    public void rotate() {
        int[][] rotated = new int[4][4];
        
        // Transponer matriz
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rotated[j][i] = shape[i][j];
            }
        }
        
        // Invertir columnas para rotación horaria
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                int temp = rotated[i][j];
                rotated[i][j] = rotated[i][3 - j];
                rotated[i][3 - j] = temp;
            }
        }
        
        shape = rotated;
    }
    
    public pieza getRotatedCopy() {
        pieza copy = new pieza(copyMatrix(shape), type);
        copy.setPosition(x, y);
        copy.rotate();
        return copy;
    }
    
    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 4);
        }
        return copy;
    }
} 

