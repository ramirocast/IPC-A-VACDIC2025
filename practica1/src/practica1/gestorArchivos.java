package practica1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class gestorArchivos {
    private static final String HIGH_SCORES_FILE = "mejores_puntajes.txt";
    private static final String STATS_FILE = "estadisticas.txt";
    
    public static void saveScore(String playerName, int score, int level, int lines, long gameTime) {
        try {
            // Leer estadísticas actuales primero
            int[] stats = readStatsForUpdate();
            
            // Actualizar estadísticas
            stats[0]++; // PARTIDAS_JUGADAS
            stats[1] += lines; // TOTAL_LINEAS
            stats[2] += score; // TOTAL_PUNTOS
            stats[3] = Math.max(stats[3], score); // MEJOR_PUNTAJE
            
            // Calcular promedios
            double avgScore = (double) stats[2] / stats[0];
            double avgLevel = stats[0] > 0 ? (double) (stats[1] / 10 + 1) : 0; // Nivel promedio estimado
            
            // Guardar estadísticas actualizadas
            saveUpdatedStats(stats, avgScore, avgLevel);
            
            // Guardar en mejores puntajes
            updateHighScores(playerName, score, level, lines, gameTime);
            
        } catch (IOException e) {
            System.err.println("Error al guardar puntaje: " + e.getMessage());
        }
    }
    
    private static int[] readStatsForUpdate() throws IOException {
        int[] stats = new int[4]; // [PARTIDAS_JUGADAS, TOTAL_LINEAS, TOTAL_PUNTOS, MEJOR_PUNTAJE]
        
        File file = new File(STATS_FILE);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PARTIDAS_JUGADAS:")) {
                    stats[0] = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("TOTAL_LINEAS:")) {
                    stats[1] = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("TOTAL_PUNTOS:")) {
                    stats[2] = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("MEJOR_PUNTAJE:")) {
                    stats[3] = Integer.parseInt(line.split(":")[1].trim());
                }
            }
            reader.close();
        }
        
        return stats;
    }
    
    private static void saveUpdatedStats(int[] stats, double avgScore, double avgLevel) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE));
        writer.write("PARTIDAS_JUGADAS: " + stats[0]);
        writer.newLine();
        writer.write("TOTAL_LINEAS: " + stats[1]);
        writer.newLine();
        writer.write("TOTAL_PUNTOS: " + stats[2]);
        writer.newLine();
        writer.write("PROMEDIO_PUNTOS: " + String.format("%.2f", avgScore));
        writer.newLine();
        writer.write("PROMEDIO_NIVEL: " + String.format("%.2f", avgLevel));
        writer.newLine();
        writer.write("MEJOR_PUNTAJE: " + stats[3]);
        writer.newLine();
        writer.write("TETRIS_REALIZADOS: " + stats[0]); // Por simplicidad, cada juego cuenta como 1
        writer.newLine();
        writer.write("FECHA_ULTIMA_ACTUALIZACION: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        writer.close();
    }
    
    private static void updateHighScores(String playerName, int score, int level, int lines, long gameTime) throws IOException {
        // Leer puntajes existentes
        String[] scores = readAllHighScores();
        
        // Crear nuevo registro
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = String.format("%d:%02d", gameTime / 60, gameTime % 60);
        String newEntry = String.format("%s,%d,%d,%d,%s,%s", 
            playerName, score, level, lines, dateFormat.format(new Date()), timeStr);
        
        // Insertar en posición correcta
        String[] newScores = insertInOrder(scores, newEntry, 10);
        
        // Guardar
        BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE));
        for (int i = 0; i < newScores.length && i < 10; i++) {
            if (newScores[i] != null) {
                writer.write((i + 1) + ". " + newScores[i]);
                writer.newLine();
            }
        }
        writer.close();
    }
    
    private static String[] readAllHighScores() throws IOException {
        File file = new File(HIGH_SCORES_FILE);
        if (!file.exists()) {
            return new String[0];
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        java.util.List<String> scoresList = new java.util.ArrayList<>();
        String line;
        
        while ((line = reader.readLine()) != null) {
            if (line.contains(".")) {
                // Extraer solo los datos después del número
                String data = line.substring(line.indexOf(".") + 1).trim();
                scoresList.add(data);
            }
        }
        reader.close();
        
        return scoresList.toArray(new String[0]);
    }
    
    private static String[] insertInOrder(String[] scores, String newEntry, int maxSize) {
        java.util.List<String> list = new java.util.ArrayList<>(java.util.Arrays.asList(scores));
        
        // Encontrar posición donde insertar
        int insertIndex = 0;
        int newScore = Integer.parseInt(newEntry.split(",")[1].trim());
        
        for (int i = 0; i < list.size(); i++) {
            String current = list.get(i);
            if (current != null) {
                int currentScore = Integer.parseInt(current.split(",")[1].trim());
                if (newScore > currentScore) {
                    insertIndex = i;
                    break;
                }
                insertIndex = i + 1;
            }
        }
        
        // Insertar
        list.add(insertIndex, newEntry);
        
        // Mantener solo los primeros maxSize
        while (list.size() > maxSize) {
            list.remove(list.size() - 1);
        }
        
        return list.toArray(new String[0]);
    }
    
    public static String[] readHighScores() {
        try {
            File file = new File(HIGH_SCORES_FILE);
            if (!file.exists()) {
                return new String[]{"No hay puntajes registrados aún."};
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            java.util.List<String> scores = new java.util.ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null && scores.size() < 10) {
                scores.add(line);
            }
            reader.close();
            
            return scores.toArray(new String[0]);
            
        } catch (IOException e) {
            return new String[]{"Error al leer puntajes."};
        }
    }
    
    public static String[] readStatistics() {
        try {
            File file = new File(STATS_FILE);
            if (!file.exists()) {
                return createDefaultStats();
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            java.util.List<String> stats = new java.util.ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                stats.add(line);
            }
            reader.close();
            
            return stats.toArray(new String[0]);
            
        } catch (IOException e) {
            return createDefaultStats();
        }
    }
    
    private static String[] createDefaultStats() {
        return new String[] {
            "PARTIDAS_JUGADAS: 0",
            "TOTAL_LINEAS: 0",
            "TOTAL_PUNTOS: 0",
            "PROMEDIO_PUNTOS: 0.00",
            "PROMEDIO_NIVEL: 0.00",
            "MEJOR_PUNTAJE: 0",
            "TETRIS_REALIZADOS: 0",
            "FECHA_ULTIMA_ACTUALIZACION: Nunca"
        };
    }
}