# 🎬 CineVerse IMAX — Animation Studio Ticketing & Reservation System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Swing](https://img.shields.io/badge/GUI-Java_Swing_%26_Graphics2D-6366F1?style=for-the-badge)
![DSA](https://img.shields.io/badge/DSA-Arrays_%7C_FIFO_Queue_%7C_Quick_Sort-F59E0B?style=for-the-badge)
![XAMPP MySQL](https://img.shields.io/badge/XAMPP-MySQL_%26_PHP_Bridge-00758F?style=for-the-badge&logo=mysql&logoColor=white)

**CineVerse IMAX** (`CinemaTicketingSystem.java`) is a modern, single-file Java Swing desktop application and box-office reservation engine built around **8 major Animation Studios**. It combines a **60 FPS custom `Graphics2D` user interface**, procedural high-resolution movie poster generation, core **Object-Oriented Programming (OOP)** principles, **Data Structures and Algorithms (DSA)**, **Structured Programming**, and real-time **XAMPP MySQL (`cinema_ticketing_db`)** synchronization.

---

## ✨ Key Features

- **🎨 Modern Sidebar UI & 60 FPS `Graphics2D` Engine:**
  - Sleek dark-mode streaming/box-office layout with a dedicated left sidebar, top live KPI metrics bar, animated spotlight rings, projector light beams, and celebratory particle bursts.
  - **Automated Movie Poster Engine (`PosterManager`):** Automatically generates and caches high-resolution illustrated studio posters (`./posters/MOV-DIS01.png` to `MOV-PAR08.png`) and supports uploading custom `.jpg` / `.png` posters via `JFileChooser`.
- **💺 Interactive 2D Auditorium Seat Map (`[6 Rows][10 Columns]`):**
  - Real-time clickable 60-seat IMAX theater layout with custom-drawn cushions, headrest pillows, and armrests.
  - Color-coded seat states: **Standard Available** (Emerald), **VIP Recliner Rows E–F** (Purple, `+PHP 60`), **Selected** (Gold), and **Reserved/Sold** (Crimson).
  - Live perforated **IMAX Boarding Pass** preview with dynamic barcode rendering.
- **🎟️ Live FIFO Box Office Queue Visualizer:**
  - Interactive linked-node **First-In, First-Out (FIFO)** queue diagram displaying the `FRONT` and `REAR` pointers in real time.
  - Supports $O(1)$ single-customer dequeueing, batch processing via `while` loops, and simulated walk-in customer enqueueing.
- **🗄️ Zero-Config XAMPP MySQL & Web Dashboard Sync:**
  - Automatically provisions the `cinema_ticketing_db` database (`studio_movies` and `ticket_reservations` tables) in **phpMyAdmin**.
  - Deploys a live companion web portal at `http://localhost/cinema_ticketing/`.

---

## 🧠 Academic Curriculum Mapping (OOP + DSA + Structured Programming)

| Subject Area | Concept / Requirement | Implementation in `CinemaTicketingSystem.java` | Complexity / Purpose |
| :--- | :--- | :--- | :--- |
| **DSA** | **1D Arrays** | `Movie[] movieCatalog`, `String[] STUDIO_FILTERS`, `String[] seatNumbers` | $O(1)$ index access for the 8 studio films and booked seat codes per ticket. |
| **DSA** | **2D Arrays** | `int[][] seatGrid = new int[6][10]`, `String[][] reservedByTicket` | Models the 6-row (A–F) by 10-column (1–10) auditorium seat matrix (`0`=Available, `1`=Selected, `2`=Reserved). |
| **DSA** | **FIFO Queue** | `ReservationQueue` & `QueueNode` (`enqueue()`, `dequeue()`, `toArray()`) | Processes waiting box-office customers in strict First-In, First-Out order in $O(1)$ time. |
| **DSA** | **Quick Sort** | `TicketQuickSorter` (`sortByPriceDesc()`, `sortByNameAsc()`) | Sorts confirmed ticket sales by highest price or customer name (A–Z) in $O(n \log n)$ time. |
| **Structured** | **Loops** | Nested `for` loops & `while` loops | Scans and renders all 60 seats in the 2D array and traverses linked nodes in the FIFO queue. |
| **Structured** | **Conditionals** | `if / else if / else` & `switch` statements | Computes Student (`15%`), Senior/PWD (`20%`), and VIP (`10%`) discounts, VIP row fees (`+PHP 60`), and snack combos. |
| **OOP** | **Abstraction** | `interface Bookable` & `abstract class Person` | Enforces required pricing and customer discount contracts across subclasses. |
| **OOP** | **Inheritance** | `class Customer extends Person`, `CinemaTicketingSystem extends JFrame` | Inherits shared personal attributes (`id`, `fullName`, `contactNumber`) via `super(...)`. |
| **OOP** | **Encapsulation** | `private final` fields with `public` getters/setters | Protects internal seat states, ticket totals, and customer records from external mutation. |
| **OOP** | **Polymorphism** | Overloaded `computePrice(...)` & Overridden `getDiscountRate()` | Supports both compile-time method overloading and runtime method overriding. |

---

## 🎥 The 8 Animation Studios Lineup

| # | Movie ID | Animation Studio | Featured Film | Auditorium & Format | Base Price |
| :-: | :--- | :--- | :--- | :--- | :--- |
| **1** | `MOV-DIS01` | **Disney** | *Zootopia 2* | Hall 1 • IMAX Laser (IMAX 3D) | PHP 340 |
| **2** | `MOV-DRW02` | **DreamWorks Animation** | *The Wild Robot* | Hall 2 • Dolby Atmos (Dolby 7.1) | PHP 350 |
| **3** | `MOV-PIX03` | **Pixar** | *Inside Out: Core Memories* | Hall 3 • Director's Club (VIP 2D) | PHP 330 |
| **4** | `MOV-ILL04` | **Illumination** | *Super Mario Galaxy Movie* | Hall 4 • 4DX Motion (4DX 3D) | PHP 380 |
| **5** | `MOV-SNY05` | **Sony Pictures Animation** | *Beyond the Spider-Verse* | Hall 5 • IMAX Grand (IMAX 3D) | PHP 400 |
| **6** | `MOV-WBA06` | **Warner Bros. Animation** | *Batman: Gotham by Gaslight* | Hall 6 • Noir Screen (2D Digital) | PHP 320 |
| **7** | `MOV-GHB07` | **Studio Ghibli** | *Spirited Away (4K Remaster)* | Hall 7 • Heritage Theater (4K IMAX) | PHP 310 |
| **8** | `MOV-PAR08` | **Paramount Animation** | *Transformers One* | Hall 8 • IMAX Laser (IMAX 3D) | PHP 370 |

---

## 🚀 How to Run & Connect to XAMPP in VS Code

### 1. Start XAMPP Services
1. Open the **XAMPP Control Panel**.
2. Click **Start** next to **Apache** and **MySQL** until both module indicators turn green.

### 2. Compile & Launch the Application
Open your terminal in **VS Code** inside the `CinemaTicketingSystem` directory and run:

```bash
javac CinemaTicketingSystem.java
java CinemaTicketingSystem
```

### 3. Access the Live XAMPP MySQL Database & Web Portal
- **phpMyAdmin Database:** [http://localhost/phpmyadmin](http://localhost/phpmyadmin) → Select **`cinema_ticketing_db`** (`studio_movies` and `ticket_reservations` tables).
- **Live Web Box Office Dashboard:** [http://localhost/cinema_ticketing/](http://localhost/cinema_ticketing/)

---

## 📂 Project Directory Structure

```text
CinemaTicketingSystem/
├── CinemaTicketingSystem.java   # Complete single-file Swing GUI, OOP, DSA & XAMPP engine
├── README.md                    # Project documentation and setup guide
└── posters/                     # Auto-generated & custom studio movie posters (MOV-DIS01.png - MOV-PAR08.png)
```

---

## 👤 Author

**Kevin Bryan Galgo**  
- GitHub: [@k3vin-cyber](https://github.com/k3vin-cyber)  
- Branch: `java-drills`