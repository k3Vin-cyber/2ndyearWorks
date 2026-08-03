# Java Book Collection Demo

A straightforward Java console application designed to demonstrate foundational Object-Oriented Programming (OOP) concepts. This project creates a simple catalog of classic literature, showcasing how to define classes, utilize custom constructors, instantiate objects, and format console output.

## Features

* **Class Definition:** Establishes a structured `Book` blueprint with specific attributes (title, author, pages).
* **Object Instantiation:** Demonstrates the creation of multiple distinct objects from a single class.
* **Custom Constructors:** Streamlines object creation by initializing properties at the moment of instantiation.
* **Encapsulated Methods:** Utilizes a dedicated `display()` method to neatly format and print object data to the console.

## Project Structure

The codebase consists of two primary components:

1. **`Book` Class**: The template containing the properties (`String title`, `String author`, `int pages`) and the behavior (`display()`) for each book.
2. **`Main` Class**: The driver class containing the `public static void main(String[] args)` method. This is where five classic books (e.g., *1984*, *The Hobbit*, *To Kill a Mockingbird*) are instantiated and executed.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) installed.
* A code editor (like Visual Studio Code) or terminal.

### Installation & Execution

1. **File Naming:** Ensure your file is correctly named `Main.java` (capital **M**) so that it matches the `public class Main` declaration. *Note: Java is strictly case-sensitive.*
2. **Open Terminal:** Navigate to your project directory (e.g., `...\2ND YEAR PROGRAMMING PROJECTS\book`).
3. **Compile:** Run the following command to compile the code into bytecode:
   ```bash
   javac Main.java