package practica1;

public class puntuacion {
    private int score;
    private int level;
    private int totalLines;
    private int tetrisCount;
    
    public puntuacion() {
        score = 0;
        level = 1;
        totalLines = 0;
        tetrisCount = 0;
    }
    
    // Método para sumar puntos por líneas eliminadas
    public void addLinesCleared(int lines) {
        totalLines += lines;
        
        // Calcular puntos según reglas originales
        int linePoints = 0;
        switch (lines) {
            case 1:
                linePoints = 100 * level;
                break;
            case 2:
                linePoints = 300 * level;
                break;
            case 3:
                linePoints = 500 * level;
                break;
            case 4:
                linePoints = 800 * level;
                tetrisCount++;  // Contar Tetris
                break;
        }
        
        score += linePoints;
        
        // Subir nivel cada 10 líneas
        if (totalLines >= level * 10) {
            level++;
            System.out.println("\n  ¡NUEVO NIVEL " + level + "!");
        }
    }
    
    // Método para sumar puntos por Soft Drop (tecla S)
    public void addSoftDropPoints(int cells) {
        score += cells;  // 1 punto por celda
    }
    
    // Método para sumar puntos por Hard Drop (tecla ESPACIO)
    public void addHardDropPoints(int cells) {
        score += cells * 2;  // 2 puntos por celda
    }
    
    // Método para sumar puntos directamente (por si acaso)
    public void addScore(int points) {
        score += points;
    }
    
    // GETTERS - TODOS LOS MÉTODOS QUE NECESITAS
    
    public int getScore() {
        return score;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getTotalLines() {
        return totalLines;
    }
    
    public int getTetrisCount() {
        return tetrisCount;
    }
    
    // Método para velocidad de caída (aunque no la uses ahora)
    public int getDropSpeed() {
        // Base 1000ms, disminuye 100ms por nivel (mínimo 100ms)
        int speed = 1000 - ((level - 1) * 100);
        return Math.max(speed, 100);
    }
}