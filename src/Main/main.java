package Main;

import Control.Board;
import Control.PGNReader;
import Control.PGNDebugger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

public class main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the chess replay application
            JFrame frame = new JFrame("Chess Replay");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Create the chessboard
            Board board = new Board();
            frame.add(board, BorderLayout.CENTER);

            // Initialize PGN utilities
            PGNReader pgnReader = new PGNReader();
            PGNDebugger pgnDebugger = new PGNDebugger(board);
            final ArrayList<String>[] moves = new ArrayList[]{new ArrayList<>()}; // To store moves from PGN
            int[] currentMoveIndex = {0}; // Track the current move

            // Menu Bar
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            JMenuItem loadPGN = new JMenuItem("Load PGN");
            loadPGN.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    pgnReader.readPGN(selectedFile.getAbsolutePath());
                    moves[0] = (ArrayList<String>) pgnReader.getMoves(); // Load moves into the list
                    currentMoveIndex[0] = 0; // Reset the index
                    resetGame(board);
                    JOptionPane.showMessageDialog(frame, "PGN loaded successfully!");
                }
            });
            fileMenu.add(loadPGN);
            menuBar.add(fileMenu);
            frame.setJMenuBar(menuBar);

            // Navigation Controls
            JPanel controls = new JPanel();
            JButton nextMoveButton = new JButton("Next Move");
            nextMoveButton.addActionListener(e -> {
                if (moves[0] != null && currentMoveIndex[0] < moves[0].size()) {
                    String pgnMove = moves[0].get(currentMoveIndex[0]);
                    var move = pgnDebugger.parseMove(pgnMove);
                    if (move != null && board.isValidMove(move)) {
                        board.makeMove(move);
                        board.repaint();
                        currentMoveIndex[0]++;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid move or end of PGN reached.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No more moves to execute.");
                }
            });

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> resetGame(board));

            controls.add(nextMoveButton);
            controls.add(restartButton);
            frame.add(controls, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }

    /**
     * Reset the game to its initial state.
     */
    private static void resetGame(Board board) {
        board.pieceList.clear();
        board.addPieces();
        board.isWhiteToMove = true;
        board.isGameOver = false;
        board.repaint();
    }
}
