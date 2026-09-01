# 📝 Student Task Tracker

A minimalist, tactile-themed web application designed to help students track their assignments and daily tasks. Built entirely with vanilla web technologies, this project features a clean, retro-inspired interface with satisfying interactive animations.

## ✨ Features

* **Tactile UI Design:** Retro/brutalist aesthetic using solid borders, custom drop shadows, and a monospace typography (`Courier New`).
* **Smooth Animations:** Tasks slide in when added and slide out gracefully when deleted.
* **Mark as Done:** Easily toggle a visual strikethrough for completed assignments.
* **Keyboard Accessibility:** Hit `Enter` to quickly add a new task without reaching for the mouse.
* **Event Delegation:** Efficient JavaScript event handling for dynamic elements (Done and Delete buttons).

## 🛠️ Technologies Used

* **HTML5:** Semantic structure and layout.
* **CSS3:** Custom CSS variables, flexbox, hover/active states, and keyframe animations (`@keyframes`).
* **Vanilla JavaScript:** DOM manipulation, event listeners, and logic. No external libraries or frameworks required.

## 🚀 How to Run

Because this project is built with raw HTML, CSS, and JS, there is no build process or server setup required.

1. Clone or download this repository.
2. Open the project folder.
3. Double-click the `index.html` file to open it in your default web browser.

*Alternatively, you can drag and drop the `index.html` file directly into a new browser tab.*

## 💡 Code Highlights

### Smooth Removal Animation
When deleting a task, the app applies a `.removing` class before completely removing the element from the DOM, creating a smooth visual exit:
```javascript
rowToRemove.classList.add("removing");
setTimeout(() => {
    rowToRemove.remove(); 
}, 300); // Waits for the CSS slideOut animation to finish