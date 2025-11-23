<!-- PROJECT SHIELDS -->
![Java](https://img.shields.io/badge/Java-JDK%208%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/github/license/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems?style=for-the-badge)
![Issues](https://img.shields.io/github/issues/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems?style=for-the-badge)
![Stars](https://img.shields.io/github/stars/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems?style=for-the-badge&social)

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems">
    <!-- You can replace this with a custom logo if you have one -->
    <img src="https://user-images.githubusercontent.com/13601893/201481358-b0e6437c-f236-40ad-b6aa-5f25a27899a2.png" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center">Dynamic Programming Optimization Algorithms</h3>

  <p align="center">
    High-performance Java implementations for the Weighted Substring Matching and Largest Zero Sub-matrix problems.
    <br />
    <a href="https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems/issues">Report Bug</a>
    ·
    <a href="https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#algorithm-deep-dive">Algorithm Deep Dive</a></li>
    <li><a href="#performance-analysis">Performance Analysis</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

---

## About The Project



This repository contains efficient, non-recursive Java implementations for two classic dynamic programming optimization problems. The goal is to provide well-documented, performant, and easy-to-use solutions that demonstrate the power of DP in solving complex problems with optimal substructure.

| Problem | Goal | Complexity (Time/Space) | Key Application |
|---------|------|-------------------------|-------------------|
| **Weighted Approximate Common Substring** | Find the pair of equal-length substrings with the maximum weighted score (matches add weight, mismatches subtract a penalty). | `O(n*m*min(n,m))` / `O(n*m)` | Bioinformatics (Sequence Alignment), Plagiarism Detection |
| **Largest Zero Sub-matrix** | Find the largest square sub-matrix consisting entirely of zeros within a given boolean matrix. | `O(n*m)` / `O(n*m)` | Image Processing (Blank Region Detection), Data Compression |

### Built With

*   ![Java](https://img.shields.io/badge/Java-JDK%208%2B-ED8B00?style=flat-square&logo=java&logoColor=white)

---

## Getting Started

Follow these simple steps to get a local copy up and running.

### Prerequisites

You need to have Java Development Kit (JDK) 8 or a higher version installed on your system.
*   You can verify your installation by running:
    ```sh
    java -version
    ```

### Installation

1.  Clone the repository to your local machine:
    ```sh
    git clone https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems.git
    ```
2.  Navigate to the project directory:
    ```sh
    cd Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems
    ```

---

## Usage

Both programs are command-line driven and include options for manual input or running predefined performance tests on synthetic data.

### 📂 Problem 1: Weighted Substring Matcher

1.  **Compile the Java file:**
    ```bash
    javac WeightedSubstringMatcher.java
    ```

2.  **Run the program:**
    ```bash
    java WeightedSubstringMatcher
    ```

3.  **Sample Interaction:** The program will prompt you for two strings. Answering `yes` to the performance test prompt will run benchmarks and save all results to `Problem1_Results.txt`.

    ```bash
    Enter First String: ABCAABCAA
    Enter Second String: ABBCAACCBBBBBB
    Do you want to run performance tests? (yes/no): yes
    
    [...Program execution...]
    
    Results have been written to Problem1_Results.txt
    ```

### 📂 Problem 2: Largest Zero Sub-matrix

1.  **Compile the Java file:**
    ```bash
    javac LargestZeroSubmatrix.java
    ```

2.  **Run the program:**
    ```bash
    java LargestZeroSubmatrix
    ```

3.  **Sample Interaction:** You can choose to enter a matrix manually or use a built-in test case. Answering `yes` to the performance test prompt will run benchmarks on various matrix sizes and save results to `Problem2_Results.txt`.

    ```bash
    Choose input method:
    1. Enter matrix manually
    2. Use predefined test case
    Enter choice (1 or 2): 1
    
    Enter number of rows: 5
    Enter number of columns: 5
    Enter matrix values row by row:
    Row 1: 1 0 1 0 0
    Row 2: 1 0 0 0 0
    Row 3: 1 1 0 0 0
    Row 4: 1 0 0 0 0
    Row 5: 1 1 1 0 1
    
    Do you want to run performance tests? (yes/no): yes
    
    [...Program execution...]
    
    Results have been written to Problem2_Results.txt
    ```
---

## Algorithm Deep Dive

### Weighted Approximate Common Substring
*   **Optimization Function:** `dp[i][j][k]` represents the maximum score for a common substring of length `k` ending at `S1[i]` and `S2[j]`.
*   **Recurrence Relation:**
    ```
    dp[i][j][k] = dp[i-1][j-1][k-1] + score(S1[i], S2[j])
    ```
*   **Space Optimization:** Implemented using two 2D arrays (`O(n*m)`) instead of a 3D array (`O(n*m*min(n,m))`) by alternating between current and previous length tables.

### Largest Zero Sub-matrix
*   **Optimization Function:** `dp[i][j]` represents the side length of the largest square of zeros with its bottom-right corner at `(i, j)`.
*   **Recurrence Relation:**
    ```
    if M[i][j] == 1:
      dp[i][j] = 0
    else:
      dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])
    ```
*   **Memory Optimization:** The DP table is implemented as a `byte[][]` array, reducing memory usage by 75% compared to an `int[][]` array, as the side length rarely exceeds 255.

---

## Performance Analysis

Empirical results from synthetic data benchmarks validate the theoretical time and space complexities of both algorithms.

### Problem 1: Weighted Approximate Common Substring Analysis

#### Time Complexity: `O(n³)`


> The execution time demonstrates clear cubic growth as the string length `n` increases. The measured data points closely follow the theoretical `O(n³)` trendline, confirming the complexity of the three nested loops (length, string 1, string 2).

#### Space Complexity: `O(n²)`


> Memory usage scales quadratically with the string length. This matches the `O(n²)` complexity of using two `n x n` DP tables, which is a significant optimization from a naive `O(n³)` space approach.

### Problem 2: Largest Zero Sub-matrix Analysis

#### Time Complexity: `O(m*n)`


> The execution time grows linearly with the total number of cells in the matrix (`m x n`). The algorithm is exceptionally fast, processing a million-cell matrix in under 7ms. This confirms the single-pass `O(m*n)` efficiency.

#### Space Complexity: `O(m*n)`


> Memory usage also scales linearly with the matrix size. The initial memory spike for small matrices is due to JVM overhead, but the trend quickly aligns with the `O(m*n)` complexity of storing the DP table.

---

## Roadmap

*   [ ] Extend **LZSM** to find the largest *rectangular* zero sub-matrix.
*   [ ] Implement gap penalties (insertions/deletions) in **WACS** to align with the Smith-Waterman algorithm.
*   [ ] Add a GUI for interactive use and visualization of the DP tables.
*   [ ] Explore parallelization/GPU acceleration for handling massive datasets.
*   [ ] Package the project as a reusable `.jar` library.

See the [open issues](https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems/issues) for a full list of proposed features (and known issues).

---

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

---

## Contact

Vishnu Sai Padyala - `padyalavishnusai@ufl.edu`

Project Link: [https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems](https://github.com/VISHNU07202003/Dynamic-Programming-Solutions-for-Weighted-Substring-Matching-and-Largest-Zero-Sub-matrix-Problems)

---

## Acknowledgments

*   This project was developed as part of coursework for the Analysis of Algorithms course at the University of Florida.
*   Special thanks to the course instructor and teaching assistants for their guidance.
*   [Shields.io](https://shields.io) for the cool badges.
*   [Best-README-Template](https://github.com/othneildrew/Best-README-Template) for inspiration on the structure.
