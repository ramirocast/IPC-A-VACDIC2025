package Practica1;

import java.io.*;
import java.util.Random;

public class Practica1 {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

/* ------------------------------------------------------------
   Clase Game: controla flujo general, menú, carga/guardado de puntajes
   ------------------------------------------------------------ */
class Game {
    private Board board;
    private InputHandler input;
    private Piece current;
    private Piece next;
    private Random rand = new Random();
    private boolean running = true;
    private boolean paused = false;
    private int score = 0;
    private int level = 1;
    private int totalLines = 0;
    private final String carnet = "202012345"; // Reemplaza con tu número de carné
    private final String HIGHSCORE_FILE = "mejores_puntajes.txt";

    public void start() {
        showMenu();
    }

    private void showMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            clearScreen();
            System.out.println("Número de carné: " + carnet);
            System.out.println("TETRIS - Menú Principal");
            System.out.println("1. Jugar Partida Nueva");
            System.out.println("2. Ver Mejores Puntajes");
            System.out.println("3. Ver Estadísticas (totales)");
            System.out.println("4. Salir");
            System.out.print("Seleccione opción: ");
            try {
                String line = br.readLine();
                if (line == null) return;
                switch (line.trim()) {
                    case "1":
                        newGame();
                        break;
                    case "2":
                        showHighScores();
                        pauseForKey();
                        break;
                    case "3":
                        showStats();
                        pauseForKey();
                        break;
                    case "4":
                        System.exit(0);
                    default:
                        System.out.println("Opción inválida.");
                        Thread.sleep(800);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void newGame() {
        board = new Board(20, 10);
        input = new InputHandler();
        input.start();
        score = 0;
        level = 1;
        totalLines = 0;
        paused = false;
        running = true;
        current = Piece.randomPiece(rand);
        next = Piece.randomPiece(rand);
        current.setPosition(0, 3); // fila 0, columnas 3-6 inicial
        gameLoop();
        input.stopRunning();
    }

    private void gameLoop() {
        long lastDrop = System.currentTimeMillis();
        int baseDelay = 1000; // nivel 1 = 1000ms
        while (running) {
            if (input.isExitRequested()) { running = false; break; }
            if (paused) {
                render();
                sleep(100);
                if (input.consumeTogglePause()) paused = !paused;
                continue;
            }
            if (input.consumeTogglePause()) { paused = true; render(); continue; }

            // process input fast
            char cmd;
            while ((cmd = input.poll()) != 0) {
                switch (cmd) {
                    case 'a': case 'A': moveLeft(); break;
                    case 'd': case 'D': moveRight(); break;
                    case 's': case 'S': softDrop(); break;
                    case 'w': case 'W': rotate(); break;
                    case ' ': hardDrop(); break;
                    case 'q': case 'Q': running=false; break;
                    case 'p': case 'P': paused = !paused; break;
                }
            }

            long now = System.currentTimeMillis();
            int delay = Math.max(100, baseDelay - (level - 1) * 100);
            if (now - lastDrop >= delay) {
                if (!tryMove(current, current.row + 1, current.col)) {
                    // place piece
                    placeCurrent();
                    int cleared = board.clearFullLines();
                    if (cleared > 0) {
                        totalLines += cleared;
                        score += getScoreForLines(cleared) * level;
                        level = 1 + totalLines / 10;
                    }
                    // generate next
                    current = next;
                    next = Piece.randomPiece(rand);
                    current.setPosition(0, 3);
                    // check game over: cannot place
                    if (!board.canPlace(current, current.row, current.col)) {
                        renderGameOver();
                        saveScorePrompt();
                        running = false;
                        break;
                    }
                } else {
                    current.row++;
                }
                lastDrop = now;
            }

            render();
            sleep(30);
        }
    }

    private void moveLeft() {
        if (tryMove(current, current.row, current.col - 1)) current.col--;
    }

    private void moveRight() {
        if (tryMove(current, current.row, current.col + 1)) current.col++;
    }

    private void softDrop() {
        if (tryMove(current, current.row + 1, current.col)) {
            current.row++;
            score += 1; // soft drop points
        }
    }

    private void hardDrop() {
        int drop = 0;
        while (tryMove(current, current.row + 1, current.col)) {
            current.row++;
            drop++;
        }
        score += 2 * drop; // hard drop points
        // place immediately
        placeCurrent();
        int cleared = board.clearFullLines();
        if (cleared > 0) {
            totalLines += cleared;
            score += getScoreForLines(cleared) * level;
            level = 1 + totalLines / 10;
        }
        current = next;
        next = Piece.randomPiece(rand);
        current.setPosition(0, 3);
        if (!board.canPlace(current, current.row, current.col)) {
            renderGameOver();
            saveScorePrompt();
            running = false;
        }
    }

    private void rotate() {
        int[][] oldShape = copyMatrix(current.shape);
        current.rotateClockwise();
        if (!board.canPlace(current, current.row, current.col)) {
            // try simple wall kick: try shift left or right
            if (board.canPlace(current, current.row, current.col - 1)) {
                current.col -= 1;
            } else if (board.canPlace(current, current.row, current.col + 1)) {
                current.col += 1;
            } else {
                // revert
                current.shape = oldShape;
            }
        }
    }

    private void placeCurrent() {
        board.placePiece(current);
    }

    private boolean tryMove(Piece p, int r, int c) {
        return board.canPlace(p, r, c);
    }

    private int getScoreForLines(int lines) {
        switch (lines) {
            case 1: return 100;
            case 2: return 300;
            case 3: return 500;
            case 4: return 800;
        }
        return 0;
    }

    private void render() {
        clearScreen();
        System.out.println("Carné: " + carnet + "    Puntaje: " + score + "    Nivel: " + level + "    Líneas: " + totalLines);
        // print board with current piece rendered as overlay
        char[][] view = board.getBoardView();
        // overlay piece
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (current.shape[i][j] == 1) {
                    int br = current.row + i;
                    int bc = current.col + j;
                    if (br >= 0 && br < board.rows && bc >= 0 && bc < board.cols) {
                        view[br][bc] = '▓';
                    }
                }
            }
        }
        // print with borders and next box
        for (int r = 0; r < board.rows; r++) {
            System.out.print("|");
            for (int c = 0; c < board.cols; c++) {
                System.out.print(view[r][c]);
            }
            System.out.print("|");
            // show next piece preview on the right
            if (r < 4) {
                System.out.print("   ");
                if (r == 0) System.out.print("Next:");
                else System.out.print("     ");
                System.out.print(" ");
                for (int c = 0; c < 4; c++) {
                    char ch = (next.shape[r][c] == 1) ? '█' : ' ';
                    System.out.print(ch);
                }
            }
            System.out.println();
        }
        // bottom border
        System.out.print("+");
        for (int c = 0; c < board.cols; c++) System.out.print("-");
        System.out.println("+");
        System.out.println("Controles: A-izq D-der S-soft W-rotar [espacio]-hard P-pausa Q-salir");
    }

    private void renderGameOver() {
        clearScreen();
        System.out.println("GAME OVER");
        System.out.println("Puntaje: " + score);
    }

    private void saveScorePrompt() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Ingrese su nombre (max 15 chars): ");
            String name = br.readLine();
            if (name == null) name = "Anon";
            if (name.length() > 15) name = name.substring(0,15);
            saveHighScore(name.trim(), score);
            System.out.println("Puntaje guardado.");
            System.out.println("Presione Enter para volver al menú...");
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Persistencia: guarda en archivo top 10 simples (nombre|puntaje) */
    private void saveHighScore(String name, int score) {
        try {
            // read existing
            File f = new File(HIGHSCORE_FILE);
            String[] names = new String[20];
            int[] scores = new int[20];
            int count = 0;
            if (f.exists()) {
                BufferedReader fr = new BufferedReader(new FileReader(f));
                String ln;
                while ((ln = fr.readLine()) != null && count < 20) {
                    String[] parts = ln.split("\\|");
                    if (parts.length == 2) {
                        names[count] = parts[0];
                        scores[count] = Integer.parseInt(parts[1]);
                        count++;
                    }
                }
                fr.close();
            }
            // add new
            names[count] = name;
            scores[count] = score;
            count++;
            // sort desc
            for (int i = 0; i < count-1; i++) {
                for (int j = i+1; j < count; j++) {
                    if (scores[j] > scores[i]) {
                        int t = scores[i]; scores[i] = scores[j]; scores[j] = t;
                        String tn = names[i]; names[i] = names[j]; names[j] = tn;
                    }
                }
            }
            // write top 10
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
            int top = Math.min(10, count);
            for (int i = 0; i < top; i++) {
                bw.write(names[i] + "|" + scores[i]);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHighScores() {
        clearScreen();
        System.out.println("Mejores Puntajes:");
        File f = new File(HIGHSCORE_FILE);
        if (!f.exists()) {
            System.out.println("No hay puntajes guardados.");
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String ln;
            int i = 1;
            while ((ln = br.readLine()) != null && i <= 10) {
                String[] parts = ln.split("\\|");
                if (parts.length == 2) {
                    System.out.printf("%d. %s - %s\n", i, parts[0], parts[1]);
                }
                i++;
            }
            br.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showStats() {
        clearScreen();
        System.out.println("Estadísticas Generales (simples):");
        System.out.println("Líneas totales en la sesión actual: " + totalLines);
        System.out.println("Nota: para estadísticas agregadas se pueden implementar archivos extra (estadisticas.txt).");
    }

    private void pauseForKey() {
        try {
            System.out.println("Presione Enter para continuar...");
            System.in.read();
        } catch (IOException e) { }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    private void clearScreen() {
        // ANSI clear (funciona en la mayoría de terminales)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private int[][] copyMatrix(int[][] src) {
        if (src == null) return null;
        int[][] dst = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++)
            for (int j = 0; j < src[i].length; j++)
                dst[i][j] = src[i][j];
        return dst;
    }
}

/* ------------------------------------------------------------
   Clase Board: tablero de juego y lógica de colocación / borrado
   ------------------------------------------------------------ */
class Board {
    public final int rows;
    public final int cols;
    private int[][] board; // 0 empty, 1 occupied

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new int[rows][cols];
        // initialize zeros
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                board[r][c] = 0;
    }

    public boolean canPlace(Piece p, int row, int col) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (p.shape[i][j] == 1) {
                    int br = row + i;
                    int bc = col + j;
                    if (bc < 0 || bc >= cols) return false;
                    if (br >= rows) return false;
                    if (br >= 0 && board[br][bc] == 1) return false;
                }
            }
        }
        return true;
    }

    public void placePiece(Piece p) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (p.shape[i][j] == 1) {
                    int br = p.row + i;
                    int bc = p.col + j;
                    if (br >= 0 && br < rows && bc >= 0 && bc < cols) {
                        board[br][bc] = 1;
                    }
                }
            }
        }
    }

    // elimina filas completas y desplaza hacia abajo. retorna número de filas eliminadas
    public int clearFullLines() {
        int cleared = 0;
        for (int r = rows - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 0) { full = false; break; }
            }
            if (full) {
                // shift all above down
                for (int rr = r; rr > 0; rr--) {
                    for (int cc = 0; cc < cols; cc++) {
                        board[rr][cc] = board[rr - 1][cc];
                    }
                }
                // clear top row
                for (int cc = 0; cc < cols; cc++) board[0][cc] = 0;
                cleared++;
                r++; // recheck same row index since rows shifted down
            }
        }
        return cleared;
    }

    public char[][] getBoardView() {
        char[][] view = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                view[r][c] = (board[r][c] == 1) ? '█' : '·';
            }
        }
        return view;
    }
}

/* ------------------------------------------------------------
   Clase Piece: definición de piezas 4x4, rotación, factory
   ------------------------------------------------------------ */
class Piece {
    public int[][] shape = new int[4][4];
    public int row = 0;
    public int col = 3; // initial column
    private Piece() {}

    public void setPosition(int r, int c) { row = r; col = c; }

    public void rotateClockwise() {
        // transpose
        int n = 4;
        int[][] t = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                t[j][i] = shape[i][j];
        // reverse columns for clockwise
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n/2; j++) {
                int tmp = t[i][j];
                t[i][j] = t[i][n-1-j];
                t[i][n-1-j] = tmp;
            }
        }
        shape = t;
    }

    public static Piece randomPiece(Random rand) {
        int r = rand.nextInt(7);
        return createFromIndex(r);
    }

    private static Piece createFromIndex(int idx) {
        Piece p = new Piece();
        int[][] s = new int[4][4];
        // define 7 tetrominos (I,O,T,S,Z,J,L)
        switch (idx) {
            case 0: // I
                s = new int[][] {
                    {0,1,0,0},
                    {0,1,0,0},
                    {0,1,0,0},
                    {0,1,0,0}
                }; break;
            case 1: // O
                s = new int[][] {
                    {1,1,0,0},
                    {1,1,0,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            case 2: // T
                s = new int[][] {
                    {0,1,0,0},
                    {1,1,1,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            case 3: // S
                s = new int[][] {
                    {0,1,1,0},
                    {1,1,0,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            case 4: // Z
                s = new int[][] {
                    {1,1,0,0},
                    {0,1,1,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            case 5: // J
                s = new int[][] {
                    {1,0,0,0},
                    {1,1,1,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            case 6: // L
                s = new int[][] {
                    {0,0,1,0},
                    {1,1,1,0},
                    {0,0,0,0},
                    {0,0,0,0}
                }; break;
            default:
                s = new int[4][4];
        }
        p.shape = s;
        p.row = 0;
        p.col = 3;
        return p;
    }
}

/* ------------------------------------------------------------
   InputHandler: hilo para leer teclas sin bloquear el loop principal
   ------------------------------------------------------------ */
class InputHandler extends Thread {
    private volatile boolean running = true;
    private volatile char last = 0;
    private volatile boolean togglePause = false;
    private volatile boolean exitRequested = false;

    public void run() {
        try {
            while (running) {
                if (System.in.available() > 0) {
                    int ch = System.in.read();
                    if (ch == -1) continue;
                    if (ch == '\r') continue;
                    // space is 32, newline 10
                    if (ch == 10) continue;
                    if (ch == 27) { // ESC
                        // consume possible '[' and next
                        if (System.in.available() > 0) System.in.read();
                        continue;
                    }
                    char c = (char) ch;
                    if (c == 'p' || c == 'P') togglePause = true;
                    if (c == 'q' || c == 'Q') exitRequested = true;
                    last = c;
                } else {
                    Thread.sleep(20);
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    // poll single char (consumes it)
    public char poll() {
        char c = last;
        last = 0;
        return c;
    }

    public boolean consumeTogglePause() {
        if (togglePause) {
            togglePause = false;
            return true;
        }
        return false;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }

    public void stopRunning() {
        running = false;
    }
}

