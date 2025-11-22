/**
 * Problem 2: Largest Zero Sub-matrix
 * Output: Writes all results to "Problem2_Results.txt"
 * 
 * DP Algorithm Design:
 * 
 * Optimization Function:
 * Let dp[i][j] = size of the largest square sub-matrix of all zeros
 *                with bottom-right corner at position (i,j)
 * 
 * Bellman Equation:
 * If matrix[i][j] == 1:
 *     dp[i][j] = 0
 * Else:
 *     dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])
 * 
 * Justification:
 * A square can only be extended if all three neighbors (top, left, top-left)
 * also form squares. The minimum of these three determines the maximum
 * extension possible.
 * 
 * Base case: dp[i][0] = 1 if matrix[i][0] == 0, else 0
 *            dp[0][j] = 1 if matrix[0][j] == 0, else 0
 * 
 * Optimal Solution Extraction:
 * Track maximum dp value and its position during computation
 * Use stored position to extract the actual square sub-matrix
 * 
 * Complexity Analysis:
 * Time: O(m * n) - single pass through matrix
 * Space: O(m * n) - DP table storage
 */

import java.util.*;
import java.io.*;

public class LargestZeroSubmatrix {
    
    private byte[][] inputMatrix;
    private byte[][] dpTable;
    
    // Solution tracking
    private int maxSquareSize;
    private int maxSquareRow;
    private int maxSquareCol;
    
    // Output writer
    private PrintWriter outputWriter;
    
    public void setOutputWriter(PrintWriter writer) {
        this.outputWriter = writer;
    }
    
    private void writeLine(String text) {
        if (outputWriter != null) {
            outputWriter.println(text);
        }
    }
    
    private void write(String text) {
        if (outputWriter != null) {
            outputWriter.print(text);
        }
    }
    
    /**
     * Core DP algorithm implementation
     */
    public SquareResult findLargestZeroSquare(byte[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        
        this.inputMatrix = matrix;
        this.dpTable = new byte[rows][cols];
        
        maxSquareSize = 0;
        maxSquareRow = -1;
        maxSquareCol = -1;
        
        // Initialize and compute DP table
        computeDPTable(rows, cols);
        
        return extractSquareSubmatrix();
    }
    
    /**
     * Compute DP table using Bellman equation
     */
    private void computeDPTable(int numRows, int numCols) {
        // Process first row
        for (int col = 0; col < numCols; col++) {
            if (inputMatrix[0][col] == 0) {
                dpTable[0][col] = 1;
                updateMaxSquare(1, 0, col);
            }
        }
        
        // Process first column
        for (int row = 1; row < numRows; row++) {
            if (inputMatrix[row][0] == 0) {
                dpTable[row][0] = 1;
                updateMaxSquare(1, row, 0);
            }
        }
        
        // Process remaining cells using DP recurrence
        for (int row = 1; row < numRows; row++) {
            for (int col = 1; col < numCols; col++) {
                if (inputMatrix[row][col] == 0) {
                    // Apply Bellman equation
                    byte topValue = dpTable[row - 1][col];
                    byte leftValue = dpTable[row][col - 1];
                    byte diagonalValue = dpTable[row - 1][col - 1];
                    
                    byte minNeighbor = minOfThree(topValue, leftValue, diagonalValue);
                    dpTable[row][col] = (byte) (1 + minNeighbor);
                    
                    updateMaxSquare(dpTable[row][col], row, col);
                } else {
                    dpTable[row][col] = 0;
                }
            }
        }
    }
    
    /**
     * Helper method to find minimum of three bytes
     */
    private byte minOfThree(byte a, byte b, byte c) {
        return (byte) Math.min(a, Math.min(b, c));
    }
    
    /**
     * Update maximum square information if larger found
     */
    private void updateMaxSquare(int size, int row, int col) {
        if (size > maxSquareSize) {
            maxSquareSize = size;
            maxSquareRow = row;
            maxSquareCol = col;
        }
    }
    
    /**
     * Extract the optimal square sub-matrix from DP solution
     */
    private SquareResult extractSquareSubmatrix() {
        if (maxSquareSize == 0) {
            return new SquareResult(new byte[0][0], 0, -1, -1);
        }
        
        // Calculate top-left corner
        int topRow = maxSquareRow - maxSquareSize + 1;
        int leftCol = maxSquareCol - maxSquareSize + 1;
        
        // Extract sub-matrix
        byte[][] submatrix = new byte[maxSquareSize][maxSquareSize];
        for (int i = 0; i < maxSquareSize; i++) {
            for (int j = 0; j < maxSquareSize; j++) {
                submatrix[i][j] = inputMatrix[topRow + i][leftCol + j];
            }
        }
        
        return new SquareResult(submatrix, maxSquareSize, topRow, leftCol);
    }
    
    /**
     * Result container class
     */
    public static class SquareResult {
        public final byte[][] submatrix;
        public final int size;
        public final int topRow;
        public final int leftCol;
        
        public SquareResult(byte[][] sub, int sz, int row, int col) {
            this.submatrix = sub;
            this.size = sz;
            this.topRow = row;
            this.leftCol = col;
        }
        
        @Override
        public String toString() {
            if (size == 0) {
                return "No zero square found";
            }
            return String.format(
                "Largest zero square: %dx%d at position (%d, %d)",
                size, size, topRow, leftCol
            );
        }
        
        public void printSubmatrixToWriter(PrintWriter writer) {
            if (size == 0) {
                writer.println("No submatrix to display");
                return;
            }
            
            if (size > 20) {
                writer.println("Sub-matrix too large to display (size > 20)");
                return;
            }
            
            writer.println("Sub-matrix content:");
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    writer.print(submatrix[i][j] + " ");
                }
                writer.println();
            }
        }
    }
    
    /**
     * Generate random boolean matrix with specified density
     */
    public static byte[][] generateRandomMatrix(int rows, int cols, 
                                               double zeroDensity, Random rng) {
        byte[][] matrix = new byte[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (rng.nextDouble() < zeroDensity) ? (byte) 0 : (byte) 1;
            }
        }
        
        return matrix;
    }
    
    /**
     * Print matrix to writer (for small matrices only)
     */
    public static void printMatrixToWriter(byte[][] matrix, PrintWriter writer) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        
        if (rows > 20 || cols > 20) {
            writer.println("Matrix too large to display (max 20x20)");
            writer.println("Matrix dimensions: " + rows + " x " + cols);
            return;
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                writer.print(matrix[i][j] + " ");
            }
            writer.println();
        }
    }
    
    /**
     * Validate matrix input
     */
    private static boolean isValidValue(int val) {
        return val == 0 || val == 1;
    }
    
    /**
     * Experimental runner - outputs to file
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("================================================================================");
        System.out.println("           PROBLEM 2: LARGEST ZERO SUB-MATRIX");
        System.out.println("================================================================================");
        System.out.println();
        
        byte[][] userMatrix = null;
        
        // Ask user for input method
        System.out.println("Choose input method:");
        System.out.println("1. Enter matrix manually");
        System.out.println("2. Use predefined test case");
        System.out.print("Enter choice (1 or 2): ");
        
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } catch (Exception e) {
            System.out.println("Invalid input. Using predefined test case.");
            choice = 2;
        }
        
        if (choice == 1) {
            // Manual matrix input
            System.out.println();
            System.out.println("Enter matrix dimensions and values (0s and 1s only)");
            
            int rows = 0, cols = 0;
            
            // Get dimensions
            while (true) {
                try {
                    System.out.print("Enter number of rows (max 20 for display): ");
                    rows = scanner.nextInt();
                    System.out.print("Enter number of columns (max 20 for display): ");
                    cols = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    
                    if (rows > 0 && cols > 0) {
                        break;
                    } else {
                        System.out.println("Dimensions must be positive!");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input! Please enter positive integers.");
                    scanner.nextLine(); // clear buffer
                }
            }
            
            userMatrix = new byte[rows][cols];
            
            System.out.println();
            System.out.println("Enter matrix values row by row (space-separated 0s and 1s):");
            System.out.println("Example for 3x3: 0 1 0");
            System.out.println();
            
            // Get matrix values
            for (int i = 0; i < rows; i++) {
                while (true) {
                    try {
                        System.out.print("Row " + (i + 1) + ": ");
                        String[] values = scanner.nextLine().trim().split("\\s+");
                        
                        if (values.length != cols) {
                            System.out.println("Error: Expected " + cols + " values, got " + values.length);
                            continue;
                        }
                        
                        boolean validRow = true;
                        for (int j = 0; j < cols; j++) {
                            int val = Integer.parseInt(values[j]);
                            if (!isValidValue(val)) {
                                System.out.println("Error: Values must be 0 or 1 only!");
                                validRow = false;
                                break;
                            }
                            userMatrix[i][j] = (byte) val;
                        }
                        
                        if (validRow) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input! Please enter space-separated 0s and 1s.");
                    }
                }
            }
            
        } else {
            // Use predefined test case
            System.out.println();
            System.out.println("Using predefined test case...");
            userMatrix = new byte[][] {
                {1, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 1, 1, 0, 1}
            };
        }
        
        System.out.println();
        System.out.println("Processing matrix of size " + userMatrix.length + " x " + userMatrix[0].length);
        
        // Ask user if they want to run performance tests
        System.out.print("Do you want to run performance tests on synthetic data? (yes/no): ");
        String runTests = scanner.nextLine().trim().toLowerCase();
        boolean performanceTests = runTests.equals("yes") || runTests.equals("y");
        
        scanner.close();
        
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("Problem2_Results.txt"));
            
            writer.println("================================================================================");
            writer.println("           PROBLEM 2: LARGEST ZERO SUB-MATRIX");
            writer.println("                    EXPERIMENTAL RESULTS");
            writer.println("================================================================================");
            writer.println();
            writer.println("Author: Dynamic Programming Assignment");
            writer.println("Date: " + new java.util.Date());
            writer.println();
            
            // User input test case
            writer.println("================================================================================");
            writer.println("USER INPUT TEST CASE");
            writer.println("================================================================================");
            writer.println("Matrix dimensions: " + userMatrix.length + " x " + userMatrix[0].length);
            writer.println();
            writer.println("Input matrix:");
            printMatrixToWriter(userMatrix, writer);
            writer.println();
            
            LargestZeroSubmatrix finder1 = new LargestZeroSubmatrix();
            finder1.setOutputWriter(writer);
            
            long startTime = System.nanoTime();
            SquareResult result1 = finder1.findLargestZeroSquare(userMatrix);
            long endTime = System.nanoTime();
            
            writer.println("Result: " + result1);
            writer.println();
            result1.printSubmatrixToWriter(writer);
            writer.println();
            writer.printf("Execution time: %.3f ms%n", 
                (endTime - startTime) / 1_000_000.0);
            writer.println();
            
            // Additional test case for demonstration
            writer.println("================================================================================");
            writer.println("ADDITIONAL TEST CASE (For Comparison)");
            writer.println("================================================================================");
            byte[][] testMatrix2 = {
                {0, 1, 1, 0, 1},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0}
            };
            
            writer.println("Input matrix:");
            printMatrixToWriter(testMatrix2, writer);
            writer.println();
            
            LargestZeroSubmatrix finder2 = new LargestZeroSubmatrix();
            finder2.setOutputWriter(writer);
            
            startTime = System.nanoTime();
            SquareResult result2 = finder2.findLargestZeroSquare(testMatrix2);
            endTime = System.nanoTime();
            
            writer.println("Result: " + result2);
            writer.println();
            result2.printSubmatrixToWriter(writer);
            writer.println();
            writer.printf("Execution time: %.3f ms%n", 
                (endTime - startTime) / 1_000_000.0);
            writer.println();
            
            // Performance experiments (if requested)
            if (performanceTests) {
                writer.println("================================================================================");
                writer.println("PERFORMANCE EXPERIMENTS (SYNTHETIC DATA)");
                writer.println("================================================================================");
                runPerformanceExperiments(writer);
            } else {
                writer.println("================================================================================");
                writer.println("PERFORMANCE EXPERIMENTS");
                writer.println("================================================================================");
                writer.println("Performance tests on synthetic data skipped by user choice.");
                writer.println();
            }
            
            writer.println();
            writer.println("================================================================================");
            writer.println("ALGORITHM COMPLEXITY SUMMARY");
            writer.println("================================================================================");
            writer.println("Time Complexity: O(m * n)");
            writer.println("  where m = number of rows, n = number of columns");
            writer.println("  Single pass through the matrix with constant time per cell");
            writer.println();
            writer.println("Space Complexity: O(m * n)");
            writer.println("  DP table of same size as input matrix");
            writer.println("  Uses byte arrays for memory efficiency (1 byte per cell)");
            writer.println();
            writer.println("================================================================================");
            writer.println("END OF REPORT");
            writer.println("================================================================================");
            
            writer.close();
            
            System.out.println();
            System.out.println("================================================================================");
            System.out.println("SUCCESS!");
            System.out.println("================================================================================");
            System.out.println("Results successfully written to 'Problem2_Results.txt'");
            System.out.println("Please check the file for detailed results and analysis.");
            System.out.println("================================================================================");
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Run comprehensive performance experiments
     */
    private static void runPerformanceExperiments(PrintWriter writer) {
        int[][] testConfigs = {
            {10, 10},
            {10, 100},
            {10, 1000},
            {100, 1000},
            {1000, 1000}
        };
        
        Random rng = new Random(12345);
        double zeroDensity = 0.3; // 30% zeros
        
        writer.println("Testing with increasingly larger matrices (30% zero density):");
        writer.println();
        writer.println("--------------------------------------------------------------------------------");
        writer.printf("%-15s | %-12s | %-13s | %-12s%n", 
            "Matrix Size", "Time (ms)", "Memory (KB)", "Max Square");
        writer.println("--------------------------------------------------------------------------------");
        
        for (int[] config : testConfigs) {
            int rows = config[0];
            int cols = config[1];
            
            byte[][] matrix = generateRandomMatrix(rows, cols, zeroDensity, rng);
            
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memBefore = runtime.totalMemory() - runtime.freeMemory();
            
            LargestZeroSubmatrix finder = new LargestZeroSubmatrix();
            
            long startTime = System.nanoTime();
            SquareResult result = finder.findLargestZeroSquare(matrix);
            long endTime = System.nanoTime();
            
            long memAfter = runtime.totalMemory() - runtime.freeMemory();
            long memUsed = (memAfter - memBefore) / 1024;
            
            writer.printf("%5d x %-8d | %12.2f | %13d | %d x %d%n", 
                rows, cols, 
                (endTime - startTime) / 1_000_000.0, 
                memUsed,
                result.size, result.size);
        }
        writer.println("--------------------------------------------------------------------------------");
        writer.println();
        writer.println("Observations:");
        writer.println("  - Time grows linearly with matrix size (O(m*n) behavior)");
        writer.println("  - Space grows linearly with matrix size (O(m*n) behavior)");
        writer.println("  - Algorithm remains efficient even for 1000x1000 matrices");
        writer.println();
        
        // Test with varying zero densities
        writer.println("================================================================================");
        writer.println("IMPACT OF ZERO DENSITY (100x100 matrix)");
        writer.println("================================================================================");
        writer.println();
        writer.println("--------------------------------------------------------------------------------");
        writer.printf("%-14s | %-12s | %-12s%n", "Zero Density", "Time (ms)", "Max Square");
        writer.println("--------------------------------------------------------------------------------");
        
        double[] densities = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        
        for (double density : densities) {
            byte[][] matrix = generateRandomMatrix(100, 100, density, rng);
            
            LargestZeroSubmatrix finder = new LargestZeroSubmatrix();
            
            long startTime = System.nanoTime();
            SquareResult result = finder.findLargestZeroSquare(matrix);
            long endTime = System.nanoTime();
            
            writer.printf("    %.0f%%       | %12.2f | %d x %d%n", 
                density * 100,
                (endTime - startTime) / 1_000_000.0,
                result.size, result.size);
        }
        writer.println("--------------------------------------------------------------------------------");
        writer.println();
        writer.println("Observation: Higher zero density generally produces larger zero squares,");
        writer.println("            demonstrating the probabilistic nature of the problem.");
    }
}