/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica1;

/**
 *
 * @author ramirito
 */
public class tetromino {

    // Matrices 4x4 para cada pieza
    public static final int[][][] PIECES = {
        // I (Cyan)
        {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        },
        // O (Amarillo)
        {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // T (Morado)
        {
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // S (Verde)
        {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {1, 1, 0, 0},
            {0, 0, 0, 0}
        },
        // Z (Rojo)
        {
            {0, 0, 0, 0},
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // J (Azul)
        {
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // L (Naranja)
        {
            {0, 0, 0, 0},
            {0, 0, 1, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        }
    };
    
    public static final char[] PIECE_TYPES = {'I', 'O', 'T', 'S', 'Z', 'J', 'L'};
    
    public static int[][] getPiece(int index) {
        int[][] piece = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(PIECES[index][i], 0, piece[i], 0, 4);
        }
        return piece;
    }
    
    public static char getPieceType(int index) {
        return PIECE_TYPES[index];
    }
    
    public static int getRandomPieceIndex() {
        return (int) (Math.random() * 7);
    }
}   

