# Python Mini Drills

A collection of introductory Python scripts demonstrating practical applications of standard libraries and external packages. This project was built to practice core Python concepts including image generation and advanced iteration techniques.

## 📌 Features

This repository contains a single, multi-part script (`mini_demo.py`) that executes three distinct tasks:

1. **QR Code Generator**: Uses the `qrcode` library to generate a scannable QR code (`my_qr.png`) containing student information.
2. **Itertools Permutations**: Uses `itertools.permutations()` to calculate and display all possible orderings of a given string array.
3. **Itertools Chunker**: Implements the standard `grouper` recipe using `itertools.zip_longest` to cleanly chunk a list of numbers into rows of three.

## 🚀 Getting Started

### Prerequisites
To run this code, you will need Python installed on your system, as well as the third-party `qrcode` library.

- [Python 3.x](https://www.python.org/downloads/)
- Install required packages:
  ```bash
  pip install "qrcode[pil]"
  ```

### Running the Script

1. Clone this repository and switch to the `python-drills` branch.
   ```bash
   git clone https://github.com/yourusername/2ndyearWorks.git
   cd 2ndyearWorks
   git checkout python-drills
   ```
2. Navigate to the project directory (if applicable).
3. Run the script:
   ```bash
   python mini_demo.py
   ```

## 💻 Expected Output

Upon running the script, the terminal will display the mathematical permutations and the chunked arrays. Additionally, a new image file (`my_qr.png`) will be generated and saved in the root directory.