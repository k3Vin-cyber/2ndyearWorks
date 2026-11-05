# 🎟️ ICC EventPass: Official Student QR Attendance & Geofenced Check-In System

**Institution:** Interface Computer College (ICC) – Iloilo City (BSIT Department)  
**Instructor:** Joshua V. Porras  
**Subjects:** Human-Computer Interaction 2 (HCI) & Platform Technologies  
**Folder / Package Name:** `icc-eventpass-qr-attendance`

---

## 🚀 1. Project Overview & Purpose

**ICC EventPass** is a mobile-first web utility engineered specifically for Interface Computer College (ICC) students, Student Government (SG) officers, and departmental organizers. It replaces slow, error-prone paper sign-in sheets during ICC General Assemblies, BSIT / Computer Studies days, NSTP outreach programs, and Intramurals with an optical QR check-in workflow.

Designed with a **Mobbin.com Swiss Editorial UI/UX architecture**, the platform combines a Split Screen Student Security Gate, a Live Digital Student Wallet Pass Generator, an iOS Framed Optical Camera QR Scanner with Dynamic Island telemetry, Haversine GPS Campus Verification, and a Real-Time Cloud Attendance Directory with one-click CSV export.

---

## 👥 2. Grouping & Deployment Rules

* **Grouping:** Done in **solo or duo** pairing as per curriculum guidelines.
* **Repository Structure:** Hosted inside the `html-drills` branch under the dedicated folder: `icc-eventpass-qr-attendance/`.
* **Hosting & HTTPS:** Deployed on **Vercel** to provide the secure `https://` environment required by mobile browsers for camera and geolocation permissions.

---

## 🔄 3. Core User Flow

1. **Security Authentication Gate:** Students sign in using their **Full Name** as their username and their **6-digit Student Number** (no spaces) as their security password, or use the *Quick Demo Sign-In* pill during oral defense grading.
2. **Program & Section Selection:** Users select their **Program** (`BSIT`, `BSCS`, `BSCpE`, `BSBA`, `BSOA`) and **Year & Section** (`1`, `2`, `3`, `4`).
3. **Digital Student Wallet Pass Generation:** The authenticated student's credentials automatically populate an iOS Wallet-style pass containing a high-contrast QR matrix encoded with a UTF-8 JSON payload. Students can click **Download PNG Pass** (rendered via the HTML5 `<canvas>` 2D API at `600x820px`) or click **Test Scan This Pass Now** to immediately simulate scanning their pass.
4. **Optical QR Scanning & Geofence Verification:** At the venue entrance, officers launch the device camera (`html5-qrcode`) or trigger the **Instant Defense Simulator** (*Valid Scan*, *Duplicate Scan*, *Outside GPS*). The system verifies that the scan is within `15m` of the Interface Computer College campus coordinates (`10.6969° N, 122.5644° E`), blocks duplicate check-ins with a 3-second optical cooldown lock, and triggers Web Audio APT acoustic chiling alongside Dynamic Island visual feedback.
5. **Live Directory Filtering & Export:** Organizers filter attendance records in real time using Mobbin horizontal filter pills by Program (`BSIT`, `BSCS`, `BSCpE`, `BSBA`, `BSOA`), Year & Section (`1` to `4`), or keyboard search (`Ctrl+K` / `Cmd+K`), and export the verified roster to an Excel-ready `.csv` file.

---

## 🎨 4. Required Application of Concepts (Rubric Mapping)

### A. Human-Computer Interaction (HCI) Concepts Applied
* **Mobbin.com Swiss Editorial Visual Hierarchy & Light/Dark Mode:** Uses high contrast monochromatic surfaces (`#F7F7F8` stone canvas, `#FFFFFF` pure cards, `#111111` ink black pill controls, and `#1;text (px)$` hairline borders) alongside a full Dark Mode toggle to reduce eye strain in dim auditoriums or outdoor venues.
* **Two-Step Progressive Disclosure (Hick's Law):** Instead of overwhelming mobile users with a single 20-item dropdown menu, cohort selection is split into two linked steps: 
  * *Step 1:* Program (`BSIT`, `BSCS`, `BSCpE`, `BSBA`, `BSOA`) 
  * *Step 2:* Year & Section (`1`, `2`, `3`, `4`), dynamically formatting the combined cohort code (e.g., `BSIT-2`).
* **Input Constraints & Poka-Yoke Error Prevention (6-Digit Student ID):** All student ID inputs enforce `inputmode="numeric"`, `maxlength="6"`, and real-time regex sanitization (`value.replace(/\D/g, '').slice(0, 6)`) with a live `0 / 6 digits` → `6 / 6 digits ✓` counter so spaces, dashes, or letters can never corrupt attendance records.
* **Multi-Modal Sensory Feedback (Web Audio API + Dynamic Island):** Synthesizes zero-latency browser audio oscillators—an ascending dual tone sine chime ($520\text{ Hz} \rightarrow 1318.5\text{ Hz}$) on verified check-in, and a low frequency sawtooth buzz ($220\text{ Hz} \rightarrow 165\text{ Hz}$) on duplicate or out-of-range errors. Visual combines a simulated iOS Dynamic Island status pill, a pop-in verification overlay (`.animate-pop`), and a horizontal error shake animation (`#keyframes hciShake`).
* **Optical Debouncing (3-Second Scan Cooldown):** Prevents continuous camera frame reads ($\ge 10\text{ fps}$) from spamming duplicate error alerts while a student holds their phone in front of the lens.
* **Accessibility & Error Recovery (Manual Check-In Fallback):** Provides an on-screen Manual Check-In Fallback form if a student's phone battery is dead or their screen is cracked, plus an accessible audio mute toggle and `aria-live="polite"` status announcements.
* **Thumb-Zone Mobile Ergonomics:** Puts primary navigation into a floating bottom pill bar (`fixed bottom-4`) on mobile viewports while expanding into a top segmented control on desktop viewports.

### B. Platform Technologies Concepts Applied
* **Camera & MediaDevices Web Api (`html5-qrcode`):** Streams live video from device cameras (`facingMode: "environment"` for rear mobile cameras) with an interactive camera flip button) and decodes UTF-8 JSON payloads inside QR matrices.
* **HTML5 Geolocation API & Haversine Spherical Trigonometry:** Retrieves live device coordinates via `navigator.geolocation.getCurrentPosition()` and calculates the exact spherical distance $d$ in meters from the Interface Computer College venue using the Haversine Formula:
  $$d = 2R \arcsin\left(\sqrt{\sin^2\left(\frac{\Delta\phi}{2}\right) + \cos(\phi_1)\cos(\phi_2)\sin^2\left(\frac{\Delta\lambda}{2}\right)}\right)$$
* **HTML5 `<canvas>` 2D Render API:** Composites the student's QR code, 6 digit ID, name, section, and official Interface Computer College branding onto an off-screen `600 \times 820\text{ (px)} \text{ <canvas>}` and serializes it into a downloadable PNG (`canvas.toDataURL('image/png')`).
* **Cloud NoSQL Persistence (Firebase Firestore):** Uses Firebase Authentication and Cloud Firestore (`onSnapshot` real-time listeners) to sync attendance logs across devices, paired with an automatic local state fallback and `localStorage` session persistence so the app runs reliably even offline.

---

## 🛠️ 5. Step-by-Step GitHub & Vercel Deployment Guide

### Step 1: Clone & Navigate to Your Local Repository
Open your terminal in VS Code and make sure you are on your `html-drills` branch:
```bash
git checkout html-drills
git pull origin html-drills