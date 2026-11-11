# ⚡ ICC EventPass — Role-Based Dynamic QR Attendance & Geofenced Kiosk System

![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![JavaScript ES6+](https://img.shields.io/badge/JavaScript_ES6+-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Firebase](https://img.shields.io/badge/Firebase_Cloud-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Vercel](https://img.shields.io/badge/Vercel_HTTPS-000000?style=for-the-badge&logo=vercel&logoColor=white)

> **A combined Human-Computer Interaction (HCI 2) and Platform Technologies capstone project built for Interface Computer College (ICC) — Iloilo City.**

---

## 📋 Project Metadata

| Field | Details |
| :--- | :--- |
| **Project Title** | ICC EventPass — Role-Based Dynamic QR Attendance & Geofenced Check-In System |
| **Developers / Presenters** | **Kevin Bryan M. Galgo** & **Noviehm Grace Alonsabe** (BSIT-2) |
| **Institution** | Interface Computer College (ICC) — Iloilo City |
| **Course Subjects** | Human-Computer Interaction 2 (HCI 2) & Platform Technologies |
| **Instructor** | Sir Joshua V. Porras |
| **Cloud Infrastructure** | Firebase Authentication, Firebase Realtime Database & Cloud Firestore (`asia-southeast1` Singapore) |
| **Deployment Branch** | `html-drills` (`icc-eventpass-qr-attendance/index.html`) |

---

## 🎯 Executive Summary

Traditional paper attendance sheets create long queues at campus events, while standard static QR codes can be easily screenshot and forwarded to absent classmates. **ICC EventPass** solves both problems inside a responsive, zero-build single-file web application (`index.html`).

The system pairs **cognitive HCI engineering** (Poka-Yoke mistake-proofing, Hick's Law progressive disclosure, multi-modal audio-visual feedback, and manual error recovery) with **modern Platform Technologies** (dynamic 30-second rotating QR tokens with cryptographic nonces, continuous `Html5Qrcode` optical scanning, Haversine GPS geofencing, Web Audio API sound synthesis, HTML5 Canvas PNG export, and real-time Firebase Cloud synchronization).

---

## 🚀 Core Architectural Features

### 1. Secure Login Gate & Student Registry Credential Validation
* **Student Record Cross-Check (`STUDENT_REGISTRY`):** Validates the user's **Full Name** and **6-Digit ICC Student ID** against official student records to prevent identity spoofing.
* **Firebase Authentication (`signInAnonymously`):** Establishes an authenticated cloud session upon login while persisting session state in `localStorage` for instant page reloads.
* **One-Click Oral Defense Presets:** Includes quick-fill preset buttons for **Kevin (Student Dashboard View)** and **Noviehm (Admin Kiosk View)** for rapid live grading demonstrations.

### 2. Role-Based Single-Page Routing System (`routeToView`)
* **🎓 Personal Student Dashboard (`#view-student`):** Automatically routes authenticated students to their dynamic QR pass wallet and personal monthly attendance ledger.
* **📷 Admin Scanner Kiosk (`#view-kiosk`):** Routes Student Government (SG) officers and event admins directly to the optical camera scanner, token/nonce verification console, and manual fallback form.
* **📊 Live Attendance Directory (`#view-directory`):** Provides real-time KPI counters, cohort filtering (`BSIT`, `BSCS`, `BSCpE`, `BSBA`, `BSOA`), instant search, and one-click `.csv` spreadsheet export.

### 3. Dynamic Auto-Refreshing Student QR Pass & Monthly History
* **30-Second Rotating QR Matrix (`QRCode.js`):** Embeds a live JSON payload containing `studentId`, `name`, `section`, a random 8-character hexadecimal **Cryptographic Nonce** (e.g., `9F2A-71C4`), `issuedAt`, and `expiresAt` ($t_{\text{issued}} + 30\text{ s}$).
* **Visual Countdown Progress Bar:** Displays a live 30-second countdown bar that automatically rotates the QR matrix and nonce when the timer reaches $0\text{ s}$, rendering saved screenshots useless.
* **Collapsible Monthly Attendance Accordions:** Groups the student's personal check-in history by month (**November 2026**, **October 2026**, **September 2026**) with interactive expand/collapse headers.
* **Real-Time Multi-Modal Scan Celebration:** Triggers a synthesized Web Audio API chime, a `canvas-confetti` burst, a floating verification toast, and an immediate token rotation the moment the pass is scanned.

### 4. Continuous Optical Kiosk Scanner & 3-Layer Security Validator
* **Hardware Camera Integration (`Html5Qrcode`):** Continuously scans student QR passes at $10\text{ fps}$ with an automatic **3-second optical debounce lock** (`isScanCooldown`) to prevent accidental double-reads.
* **Three-Stage Token & Nonce Verification (`validateAndLogQRToken`):**
  1. **GPS Geofence Guard:** Confirms physical presence within $150\text{ m}$ of ICC Iloilo City ($10.6970^\circ\text{ N}, 122.5645^\circ\text{ E}$) using spherical trigonometry, with an **Indoor GPS Bypass** toggle for classroom demos.
  2. **Token Expiration Check:** Rejects any QR code where $t_{\text{now}} > t_{\text{expiresAt}}$ (`QR TOKEN EXPIRED — SCREENSHOT BLOCKED`).
  3. **Cryptographic Nonce Replay Guard:** Tracks every consumed nonce inside an in-memory `Set` (`usedNonces`) synced with Firebase, immediately blocking any second scan of the same token (`NONCE REPLAY ATTACK BLOCKED`).

---

## 🧠 Human-Computer Interaction (HCI 2) Principles Applied

| HCI Principle | Implementation in ICC EventPass | User Experience Benefit |
| :--- | :--- | :--- |
| **Poka-Yoke (Mistake-Proofing)** | `sanitizeStudentIdInput()` runs `.replace(/\D/g, '').slice(0, 6)` on every keystroke with a live `6 / 6 digits` counter. | Prevents users from typing letters, spaces, or symbols into the 6-digit ICC Student ID field. |
| **Hick's Law (Progressive Disclosure)** | Splits cohort selection into **Step 1: Program** (`5` options) and **Step 2: Year & Section** (`4` dynamically labeled options). | Eliminates cognitive overload compared to scrolling through a single $20$-item dropdown list. |
| **Multi-Modal Feedback** | Pairs iOS Dynamic Island visual banners (Emerald, Amber, Rose) and confetti with Web Audio API synthesized tones. | Allows kiosk operators to distinguish valid scans from expired/duplicate scans by sound alone. |
| **Graceful Error Recovery** | Includes a dedicated **Manual Check-In Fallback** form on the Admin Kiosk. | Enables officers to log students whose phone batteries died or screens are cracked. |

---

## 🛠️ Platform Technologies & Browser APIs Integrated

### 1. Haversine Spherical GPS Geofencing (`navigator.geolocation`)
To verify that check-ins occur on campus, the app calculates the great-circle distance $d$ (in meters) between the user's coordinates $(\phi_1, \lambda_1)$ and Interface Computer College $(\phi_2, \lambda_2)$ using Earth's mean radius $R = 6,371,000\text{ m}$:

$$a = \sin^2\left(\frac{\Delta\phi}{2}\right) + \cos(\phi_1)\cdot\cos(\phi_2)\cdot\sin^2\left(\frac{\Delta\lambda}{2}\right)$$

$$d = 2R \cdot \operatorname{atan2}\left(\sqrt{a}, \sqrt{1 - a}\right)$$

If $d > 150\text{ m}$ and Strict GPS Mode is active, the check-in is automatically rejected.

### 2. Web Audio API Synthesizer (`AudioContext`)
Instead of relying on external `.mp3` assets that can fail to load on slow networks, `playSynthesizedTone()` generates real-time oscillator waveforms directly in hardware:
* **Verified Check-In (`success`):** Ascending `sine` wave ramping from $587.33\text{ Hz}$ ($\text{D}_5$) to $1174.66\text{ Hz}$ ($\text{D}_6$) over $0.30\text{ s}$.
* **Blocked / Invalid Scan (`error`):** Low `sawtooth` warning buzz ramping down from $195\text{ Hz}$ to $130\text{ Hz}$ over $0.34\text{ s}$.

### 3. Hybrid Cloud + Local Persistence (`Firebase RTDB` + `Firestore` + `localStorage`)
* **Firebase Realtime Database (`onValue` & `set`):** Streams live check-ins to `https://icc-eventpass-846c7-default-rtdb.asia-southeast1.firebasedatabase.app/attendance_logs`.
* **Cloud Firestore (`onSnapshot` & `setDoc`):** Mirrors structured documents inside the `attendance_logs` collection.
* **Offline-First Resilience (`localStorage`):** Caches all logs locally so the kiosk never crashes if campus Wi-Fi drops mid-event.

### 4. HTML5 Canvas 2D Printable Pass & CSV Export
* **Printable PNG Wallet Pass (`downloadStudentPassPNG`):** Renders a $600 \times 820\text{ px}$ high-contrast ID card with the student's QR matrix, 6-digit ID, cohort badge, and active nonce.
* **Spreadsheet Export (`exportDirectoryCSV`):** Encodes the attendance roster into RFC-4180 compliant `.csv` format for Microsoft Excel and Google Sheets.

---

## 🔍 Quick Defense Code Lookup (`Ctrl + F` Cheat Sheet)

Use `Ctrl + F` inside `index.html` in VS Code to jump straight to any method during your code walkthrough:

| Feature to Demonstrate | Search Keyword (`Ctrl + F`) | Description |
| :--- | :--- | :--- |
| **Poka-Yoke Input Sanitizer** | `sanitizeStudentIdInput` | Strips non-numeric characters and enforces a strict 6-digit cap. |
| **Hick's Law Dynamic Labels** | `updateYearLabels` | Dynamically updates Year 1–4 labels based on the chosen Program. |
| **Login & Registry Validation** | `handleRoleBasedLogin` | Verifies `STUDENT_REGISTRY`, triggers Firebase Auth, and routes by role. |
| **Role-Based View Router** | `routeToView` | Switches seamlessly between `student`, `kiosk`, and `directory` views. |
| **Cryptographic Nonce Generator** | `generateCryptographicNonce` | Generates random 8-character hexadecimal token IDs (`XXXX-XXXX`). |
| **30s Dynamic QR Generator** | `regenerateStudentQRToken` | Builds the expiring JSON payload and renders `new QRCode(...)`. |
| **Collapsible Monthly History** | `renderStudentMonthlyHistory` | Groups personal attendance records into expandable monthly accordions. |
| **3-Layer Kiosk Token Validator** | `validateAndLogQRToken` | Validates 150m GPS geofence, 30s `expiresAt`, and `usedNonces` replay guard. |
| **Continuous Camera Scanner** | `toggleKioskCamera` | Manages the `Html5Qrcode` camera stream and 3-second cooldown lock. |
| **Haversine GPS Calculation** | `pingHardwareGPS` | Computes spherical meter distance from ICC Iloilo coordinates. |
| **Web Audio Chime Synthesizer** | `playSynthesizedTone` | Synthesizes `sine` and `sawtooth` audio feedback via `AudioContext`. |
| **Dual Firebase Cloud Sync** | `firebaseConfig` | Connects Firebase Auth, Realtime Database (`ref`), and Firestore (`collection`). |

---

## 💻 Local Setup & Live Deployment Guide

### 1. Run Locally in VS Code
1. Open the `icc-eventpass-qr-attendance` folder in **Visual Studio Code**.
2. Right-click `index.html` and select **Open with Live Server** (or open `index.html` directly in Chrome/Edge).

### 2. Firebase Realtime Database Rules (`asia-southeast1`)
Ensure your Firebase Realtime Database rules allow live kiosk reads and writes during your defense demo:
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

### 3. Push Updates to GitHub & Vercel
Run these commands in your VS Code terminal to deploy changes live:
```bash
git add index.html README.md
git commit -m "Update ICC EventPass v2 with role routing, dynamic nonce QR, and README docs"
git push origin html-drills
```

---

## 👥 Authors & Acknowledgments

* **Kevin Bryan M. Galgo** — *Lead Developer & Platform Technologies Architecture (BSIT-2)*
* **Noviehm Grace Alonsabe** — *Co-Lead Developer & HCI / UI-UX Engineering (BSIT-2)*
* **Instructor:** Sir Joshua V. Porras — *Interface Computer College (ICC), Iloilo City*