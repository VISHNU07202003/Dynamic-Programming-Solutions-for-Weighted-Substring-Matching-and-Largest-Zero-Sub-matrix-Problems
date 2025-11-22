/**
 * Problem 1: Weighted Approximate Common Substring
 * Output: Writes all results to "Problem1_Results.txt"
 * 
 * DP Algorithm Design:
 * 
 * Optimization Function:
 * Let dp[i][j][len] = maximum score achievable for matching substrings of length 'len'
 *                     ending at position i in string1 and position j in string2
 * 
 * Bellman Equation:
 * dp[i][j][len] = dp[i-1][j-1][len-1] + score(s1[i], s2[j])
 * where score(a,b) = weight[a] if a==b, else -penalty
 * 
 * Base case: dp[i][j][1] = score(s1[i], s2[j]) for all valid i,j
 * 
 * Optimal Solution Extraction:
 * Track maxScore, and corresponding indices (maxI, maxJ, maxLen) during DP computation
 * Extract substring using these stored indices
 * 
 * Complexity Analysis:
 * Time: O(n * m * min(n,m)) where n,m are string lengths
 * Space: O(n * m) - optimized to 2D instead of 3D by computing on-the-fly
 */

import java.util.*;
import java.io.*;

public class WeightedSubstringMatcher {
    
    private static final double[] ENGLISH_FREQ = {
        8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966, 0.153,
        0.772, 4.025, 2.406, 6.749, 7.507, 1.929, 0.095, 5.987, 6.327, 9.056,
        2.758, 0.978, 2.360, 0.150, 1.974, 0.074
    };
    
    private double[] charWeights;
    private double mismatchPenalty;
    private String firstSeq;
    private String secondSeq;
    
    // DP table and tracking variables
    private double[][] currentScores;
    private double[][] previousScores;
    
    // Solution tracking
    private double optimalScore;
    private int optimalEndPos1;
    private int optimalEndPos2;
    private int optimalLength;
    
    // Output writer
    private PrintWriter outputWriter;
    
    public WeightedSubstringMatcher(double[] weights, double penalty) {
        this.charWeights = weights;
        this.mismatchPenalty = penalty;
    }
    
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
    public SubstringMatch findBestMatch(String seq1, String seq2) {
        this.firstSeq = seq1;
        this.secondSeq = seq2;
        
        int len1 = seq1.length();
        int len2 = seq2.length();
        
        // Initialize DP tables
        currentScores = new double[len1][len2];
        previousScores = new double[len1][len2];
        
        optimalScore = Double.NEGATIVE_INFINITY;
        optimalEndPos1 = -1;
        optimalEndPos2 = -1;
        optimalLength = 0;
        
        // Compute for each possible substring length
        for (int substringLen = 1; substringLen <= Math.min(len1, len2); substringLen++) {
            computeForLength(substringLen);
            
            // Swap references for next iteration
            double[][] temp = previousScores;
            previousScores = currentScores;
            currentScores = temp;
        }
        
        return extractOptimalSolution();
    }
    
    /**
     * Compute scores for all substrings of a specific length
     */
    private void computeForLength(int targetLen) {
        int len1 = firstSeq.length();
        int len2 = secondSeq.length();
        
        for (int idx1 = targetLen - 1; idx1 < len1; idx1++) {
            for (int idx2 = targetLen - 1; idx2 < len2; idx2++) {
                double matchScore = calculateMatchScore(
                    firstSeq.charAt(idx1), 
                    secondSeq.charAt(idx2)
                );
                
                if (targetLen == 1) {
                    currentScores[idx1][idx2] = matchScore;
                } else {
                    currentScores[idx1][idx2] = 
                        previousScores[idx1 - 1][idx2 - 1] + matchScore;
                }
                
                // Update optimal solution if better score found
                if (currentScores[idx1][idx2] > optimalScore) {
                    optimalScore = currentScores[idx1][idx2];
                    optimalEndPos1 = idx1;
                    optimalEndPos2 = idx2;
                    optimalLength = targetLen;
                }
            }
        }
    }
    
    /**
     * Calculate score for matching two characters
     */
    private double calculateMatchScore(char c1, char c2) {
        if (c1 == c2) {
            int charIndex = c1 - 'A';
            return charWeights[charIndex];
        }
        return -mismatchPenalty;
    }
    
    /**
     * Extract the optimal substring match from DP solution
     */
    private SubstringMatch extractOptimalSolution() {
        if (optimalLength == 0) {
            return new SubstringMatch("", "", 0, 0, 0, 0.0, 0);
        }
        
        int startPos1 = optimalEndPos1 - optimalLength + 1;
        int startPos2 = optimalEndPos2 - optimalLength + 1;
        
        String substr1 = firstSeq.substring(startPos1, optimalEndPos1 + 1);
        String substr2 = secondSeq.substring(startPos2, optimalEndPos2 + 1);
        
        // Count mismatches
        int mismatches = 0;
        for (int i = 0; i < optimalLength; i++) {
            if (substr1.charAt(i) != substr2.charAt(i)) {
                mismatches++;
            }
        }
        
        return new SubstringMatch(
            substr1, substr2, 
            startPos1, startPos2, 
            optimalLength, optimalScore, mismatches
        );
    }
    
    /**
     * Result container class
     */
    public static class SubstringMatch {
        public final String substring1;
        public final String substring2;
        public final int position1;
        public final int position2;
        public final int length;
        public final double score;
        public final int mismatches;
        
        public SubstringMatch(String s1, String s2, int pos1, int pos2, 
                            int len, double sc, int mm) {
            this.substring1 = s1;
            this.substring2 = s2;
            this.position1 = pos1;
            this.position2 = pos2;
            this.length = len;
            this.score = sc;
            this.mismatches = mm;
        }
        
        @Override
        public String toString() {
            return String.format(
                "'%s' (pos %d) <-> '%s' (pos %d) | Len: %d, Mismatches: %d, Score: %.2f",
                substring1, position1, substring2, position2, length, mismatches, score
            );
        }
    }
    
    /**
     * Scenario 1: Uniform weights
     */
    public static double[] createUniformWeights() {
        double[] weights = new double[26];
        Arrays.fill(weights, 1.0);
        return weights;
    }
    
    /**
     * Scenario 2: Frequency-based weights
     */
    public static double[] createFrequencyWeights() {
        double[] weights = new double[26];
        double minFreq = Arrays.stream(ENGLISH_FREQ).min().getAsDouble();
        double maxFreq = Arrays.stream(ENGLISH_FREQ).max().getAsDouble();
        
        for (int i = 0; i < 26; i++) {
            // Normalize to range [1, 10]
            weights[i] = 1.0 + 9.0 * (ENGLISH_FREQ[i] - minFreq) / (maxFreq - minFreq);
        }
        return weights;
    }
    
    /**
     * Validate that string contains only A-Z characters
     */
    private static boolean isValidString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (c < 'A' || c > 'Z') {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Experimental runner - outputs to file
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("================================================================================");
        System.out.println("        PROBLEM 1: WEIGHTED APPROXIMATE COMMON SUBSTRING");
        System.out.println("================================================================================");
        System.out.println();
        
        // Get user input for strings
        String test1 = "";
        String test2 = "";
        
        System.out.println("Enter two strings containing only uppercase letters (A-Z)");
        System.out.println();
        
        // Get first string
        while (true) {
            System.out.print("Enter First String: ");
            test1 = scanner.nextLine().trim().toUpperCase();
            
            if (isValidString(test1)) {
                break;
            } else {
                System.out.println("Invalid input! Please enter only uppercase letters A-Z.");
            }
        }
        
        // Get second string
        while (true) {
            System.out.print("Enter Second String: ");
            test2 = scanner.nextLine().trim().toUpperCase();
            
            if (isValidString(test2)) {
                break;
            } else {
                System.out.println("Invalid input! Please enter only uppercase letters A-Z.");
            }
        }
        
        System.out.println();
        System.out.println("Processing strings...");
        System.out.println("String 1: " + test1);
        System.out.println("String 2: " + test2);
        System.out.println();
        
        // Ask user if they want to run performance tests
        System.out.print("Do you want to run performance tests on synthetic data? (yes/no): ");
        String runTests = scanner.nextLine().trim().toLowerCase();
        boolean performanceTests = runTests.equals("yes") || runTests.equals("y");
        
        scanner.close();
        
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("Problem1_Results.txt"));
            
            writer.println("================================================================================");
            writer.println("        PROBLEM 1: WEIGHTED APPROXIMATE COMMON SUBSTRING");
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
            writer.println("String 1: " + test1);
            writer.println("String 2: " + test2);
            writer.println("String 1 Length: " + test1.length());
            writer.println("String 2 Length: " + test2.length());
            writer.println();
            
            // Scenario 1: Uniform weights, penalty = 10 (AS REQUIRED)
            writer.println("--------------------------------------------------------------------------------");
            writer.println("SCENARIO 1: UNIFORM WEIGHTS (w = 1.0, penalty δ = 10)");
            writer.println("--------------------------------------------------------------------------------");
            writer.println("Description: All characters have equal weight of 1.0");
            writer.println("             Mismatch penalty δ = 10 (as per requirements)");
            writer.println();
            
            double[] uniformWeights = createUniformWeights();
            WeightedSubstringMatcher matcher1 = 
                new WeightedSubstringMatcher(uniformWeights, 10.0);  // FIXED: δ = 10
            matcher1.setOutputWriter(writer);
            
            long startTime = System.nanoTime();
            SubstringMatch result1 = matcher1.findBestMatch(test1, test2);
            long endTime = System.nanoTime();
            
            writer.println("Best Match: " + result1);
            writer.printf("Execution time: %.3f ms%n", 
                (endTime - startTime) / 1_000_000.0);
            writer.println();
            
            // Scenario 2: Frequency-based weights
            writer.println("================================================================================");
            writer.println("SCENARIO 2: FREQUENCY-BASED WEIGHTS");
            writer.println("================================================================================");
            writer.println("Description: Weights are based on English letter frequency");
            writer.println("             Common letters (E, T, A) get higher weights");
            writer.println("             Rare letters (Z, Q, X) get lower weights");
            writer.println("             Weight range normalized to [1, 10]");
            writer.println("             Penalty δ varies from min weight to max weight");
            writer.println();
            
            double[] freqWeights = createFrequencyWeights();
            
            double minWeight = Arrays.stream(freqWeights).min().getAsDouble();
            double maxWeight = Arrays.stream(freqWeights).max().getAsDouble();
            
            writer.println("Weight Range: [" + String.format("%.3f", minWeight) + 
                          ", " + String.format("%.3f", maxWeight) + "]");
            writer.println();
            writer.println("Testing with 10 intermediate penalty values (as required):");
            writer.println("(Penalty δ ranges from minimum weight to maximum weight)");
            writer.println();
            writer.println("--------------------------------------------------------------------------------");
            writer.printf("%-12s | %-60s | %-10s%n", "Penalty δ", "Result", "Time (ms)");
            writer.println("--------------------------------------------------------------------------------");
            
            // Test with 10 intermediate penalty values (AS REQUIRED)
            for (int i = 0; i <= 10; i++) {
                double penalty = minWeight + (maxWeight - minWeight) * i / 10.0;
                
                WeightedSubstringMatcher matcher2 = 
                    new WeightedSubstringMatcher(freqWeights, penalty);
                
                startTime = System.nanoTime();
                SubstringMatch result2 = matcher2.findBestMatch(test1, test2);
                endTime = System.nanoTime();
                
                writer.printf("%-12.3f | %-60s | %-10.3f%n", 
                    penalty, result2.toString(), (endTime - startTime) / 1_000_000.0);
            }
            writer.println("--------------------------------------------------------------------------------");
            writer.println();
            
            // Performance testing with larger strings (if requested)
            if (performanceTests) {
                writer.println("================================================================================");
                writer.println("PERFORMANCE ANALYSIS (SYNTHETIC DATA)");
                writer.println("================================================================================");
                runPerformanceTests(writer);
            } else {
                writer.println("================================================================================");
                writer.println("PERFORMANCE ANALYSIS");
                writer.println("================================================================================");
                writer.println("Performance tests on synthetic data skipped by user choice.");
                writer.println();
            }
            
            writer.println();
            writer.println("================================================================================");
            writer.println("ALGORITHM COMPLEXITY SUMMARY");
            writer.println("================================================================================");
            writer.println("Time Complexity: O(n * m * min(n,m))");
            writer.println("  where n = length of first string, m = length of second string");
            writer.println();
            writer.println("Space Complexity: O(n * m)");
            writer.println("  Two 2D arrays used (optimized from 3D)");
            writer.println();
            writer.println("DP Recurrence Relation:");
            writer.println("  dp[i][j][k] = dp[i-1][j-1][k-1] + score(s1[i], s2[j])");
            writer.println("  where score(a,b) = w[a] if match, else -δ");
            writer.println();
            writer.println("================================================================================");
            writer.println("END OF REPORT");
            writer.println("================================================================================");
            
            writer.close();
            
            System.out.println("================================================================================");
            System.out.println("SUCCESS!");
            System.out.println("================================================================================");
            System.out.println("Results successfully written to 'Problem1_Results.txt'");
            System.out.println("Please check the file for detailed results and analysis.");
            System.out.println("================================================================================");
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Performance testing with synthetic data
     */
    private static void runPerformanceTests(PrintWriter writer) {
        Random rng = new Random(42);
        int[] testSizes = {50, 100, 200, 500, 1000};
        
        writer.println("Testing with increasingly larger string lengths:");
        writer.println("(Randomly generated strings with uniform distribution of A-Z)");
        writer.println();
        writer.println("--------------------------------------------------------------------------------");
        writer.printf("%-15s | %-12s | %-12s%n", "String Lengths", "Time (ms)", "Memory (KB)");
        writer.println("--------------------------------------------------------------------------------");
        
        for (int size : testSizes) {
            String s1 = generateRandomString(size, rng);
            String s2 = generateRandomString(size, rng);
            
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memBefore = runtime.totalMemory() - runtime.freeMemory();
            
            WeightedSubstringMatcher matcher = 
                new WeightedSubstringMatcher(createUniformWeights(), 10.0);
            
            long startTime = System.nanoTime();
            matcher.findBestMatch(s1, s2);
            long endTime = System.nanoTime();
            
            long memAfter = runtime.totalMemory() - runtime.freeMemory();
            long memUsed = (memAfter - memBefore) / 1024;
            
            writer.printf("%6d x %-6d | %12.2f | %12d%n", 
                size, size, (endTime - startTime) / 1_000_000.0, memUsed);
        }
        writer.println("--------------------------------------------------------------------------------");
        writer.println();
        writer.println("Observations:");
        writer.println("  - Time grows as O(n^3) due to three nested loops");
        writer.println("  - Space grows as O(n^2) for the DP table");
        writer.println("  - Algorithm remains efficient for strings up to 1000 characters");
    }
    
    private static String generateRandomString(int length, Random rng) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + rng.nextInt(26)));
        }
        return sb.toString();
    }
}