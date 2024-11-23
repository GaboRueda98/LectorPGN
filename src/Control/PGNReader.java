package Control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PGNReader {
    private List<String> moves; // Store parsed moves

    public PGNReader() {
        this.moves = new ArrayList<>();
    }

    /**
     * Reads a PGN file and parses the moves.
     * @param filePath Path to the PGN file.
     */
    public void readPGN(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean moveSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip metadata
                if (line.isEmpty() || line.startsWith("[")) {
                    continue;
                }

                moveSection = true; // Metadata is done; moves are starting
                if (moveSection) {
                    String[] tokens = line.split("\\s+");
                    for (String token : tokens) {
                        if (!token.matches("\\d+\\.")) { // Skip move numbers
                            moves.add(token);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading PGN file: " + e.getMessage());
        }
    }

    /**
     * Get the parsed moves as a list.
     * @return List of PGN moves.
     */
    public List<String> getMoves() {
        return moves;
    }
}
