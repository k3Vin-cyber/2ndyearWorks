# Big O Notation Cheatsheet (Java)

A comprehensive, runnable Java reference guide demonstrating every major **Big O time complexity class**. 

This repository serves as a practical cheatsheet for understanding how algorithms scale. Instead of just theoretical explanations, it provides concrete, executable code examples for each complexity level, from $O(1)$ all the way to $O(n!)$.

## 🧠 Complexities Covered

This project includes fully documented implementations of the following:

| Complexity | Name | Example Algorithm Used |
| :--- | :--- | :--- |
| **$O(1)$** | Constant | Array index access, Bitwise even/odd check |
| **$O(\log n)$** | Logarithmic | Binary Search (iterative) |
| **$O(n)$** | Linear | Finding the maximum value, Array summation |
| **$O(n \log n)$** | Linearithmic | Merge Sort |
| **$O(n^2)$** | Quadratic | Bubble Sort, Finding duplicate pairs |
| **$O(n^3)$** | Cubic | Naive Matrix Multiplication, 3-Sum |
| **$O(2^n)$** | Exponential | Recursive Fibonacci, Generating Power Sets |
| **$O(n!)$** | Factorial | Generating all permutations via backtracking |
| **$O(\sqrt{n})$** | Square Root | Primality testing (Bonus) |

## 🚀 Getting Started

### Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (version 8 or higher)

### Running the Code

Clone the repository and run the single `BigONotations` class to see every algorithm in action.

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/big-o-cheatsheet-java.git
   ```
2. Navigate to the directory:
   ```bash
   cd big-o-cheatsheet-java
   ```
3. Compile the Java file:
   ```bash
   javac BigONotations.java
   ```
4. Run the compiled Java class:
   ```bash
   java BigONotations
   ```

## 🛠️ Code Features
- **Defensive Programming**: Includes standard `null` checks and empty array handling.
- **Optimized Logic**: Uses fast bit-shifting for exponential generation and iterative binary search to prevent stack overflow.
- **Clean Output**: Uses standard `Arrays.toString()` and `System.out.printf()` for readable console formatting.

## 🤝 Contributing
Feel free to open an issue or submit a pull request if you want to add space complexity examples ($O(1)$ vs $O(n)$ space), or add new algorithmic examples to the existing time complexity tiers!