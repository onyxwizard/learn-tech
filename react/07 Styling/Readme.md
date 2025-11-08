# 📘 React Quick Notes — Chapter 7: Styling in React 🎨  
> *“From global CSS to component-scoped styles — pick the right tool for your team and project.”*



## 🗂️ Table of Contents

1.  [🧩 CSS Modules — Scoped Styles](#-1-css-modules--scoped-styles)
2.  [🖌️ Styled Components — CSS-in-JS](#-2-styled-components--css-in-js)
3.  [⚡ Tailwind CSS — Utility-First](#-3-tailwind-css--utility-first)
4.  [🎨 CSS-in-JS vs Utility CSS — When to Use What?](#-4-css-in-js-vs-utility-css--when-to-use-what)
5.  [🌓 Dynamic & Conditional Styling](#-5-dynamic--conditional-styling)
6.  [🌍 Theming & Dark Mode](#-6-theming--dark-mode)



## 🧩 1. CSS Modules — Scoped Styles

### 💡 Concept
CSS Modules let you write CSS that is locally scoped to a component by default. No more class name collisions!

### 🎯 Real-World Interview Example
> *“Style a Button component using CSS Modules. Show how class names are scoped.”*

📁 `Button.module.css`
```css
/* CSS Modules file */
.primary {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.primary:hover {
  background-color: #0056b3;
}

.danger {
  background-color: #dc3545;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.danger:hover {
  background-color: #c82333;
}
```

📁 `Button.jsx`
```jsx
import styles from './Button.module.css'; // ✅ Import styles object

function Button({ variant = 'primary', children, onClick }) {
  // Dynamically pick class based on prop
  const className = variant === 'danger' ? styles.danger : styles.primary;

  return (
    <button className={className} onClick={onClick}>
      {children}
    </button>
  );
}

// Usage
function App() {
  return (
    <div>
      <Button>Primary Button</Button>
      <Button variant="danger">Delete Button 🗑️</Button>
    </div>
  );
}
```

✅ **Generated HTML (class names are auto-scoped):**
```html
<button class="Button_primary__abc123">Primary Button</button>
<button class="Button_danger__def456">Delete Button 🗑️</button>
```

> 💬 **Interview Tip**: “I use CSS Modules for projects that want to stick with traditional CSS but need scoping. It’s zero-runtime, familiar to CSS devs, and prevents naming collisions. Great for teams migrating from global CSS.”

---

## 🖌️ 2. Styled Components — CSS-in-JS

### 💡 Concept
Write actual CSS inside your JavaScript using tagged template literals. Styles are automatically scoped and can receive props.

### 🎯 Real-World Interview Example
> *“Create a dynamic Button component that changes color based on a `variant` prop using Styled Components.”*

```bash
npm install styled-components
```

```jsx
import styled from 'styled-components';

// ✅ Create a styled button
const StyledButton = styled.button`
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  color: white;

  /* 🎨 Dynamic styles based on props */
  background-color: ${props =>
    props.variant === 'danger' ? '#dc3545' : '#007bff'};

  &:hover {
    background-color: ${props =>
      props.variant === 'danger' ? '#c82333' : '#0056b3'};
  }
`;

// Reusable component
function Button({ variant = 'primary', children, onClick }) {
  return (
    <StyledButton variant={variant} onClick={onClick}>
      {children}
    </StyledButton>
  );
}

// Usage
function App() {
  return (
    <div>
      <Button>Primary Button</Button>
      <Button variant="danger">Delete Button 🗑️</Button>
    </div>
  );
}
```

> 💬 **Interview Tip**: “Styled Components is my go-to for complex, dynamic UIs. The ability to pass props directly into styles is powerful. It has a small runtime cost, but the developer experience is fantastic. Perfect for design systems.”

---

## ⚡ 3. Tailwind CSS — Utility-First

### 💡 Concept
A utility-first CSS framework. Instead of writing custom CSS, you apply pre-defined classes directly in your JSX.

### 🎯 Real-World Interview Example
> *“Build the same Button component using Tailwind CSS classes.”*

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

📁 `tailwind.config.js`
```js
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"], // ✅ Tell Tailwind where to look
  theme: {
    extend: {},
  },
  plugins: [],
}
```

📁 `index.css`
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

📁 `Button.jsx`
```jsx
function Button({ variant = 'primary', children, onClick }) {
  // ✅ Define classes based on variant
  const baseClasses = "px-5 py-2.5 rounded font-medium focus:outline-none focus:ring-2 focus:ring-offset-2";

  const variantClasses =
    variant === 'danger'
      ? "bg-red-600 hover:bg-red-700 text-white focus:ring-red-500"
      : "bg-blue-600 hover:bg-blue-700 text-white focus:ring-blue-500";

  return (
    <button className={`${baseClasses} ${variantClasses}`} onClick={onClick}>
      {children}
    </button>
  );
}

// Usage
function App() {
  return (
    <div className="p-6 space-x-4">
      <Button>Primary Button</Button>
      <Button variant="danger">Delete Button 🗑️</Button>
    </div>
  );
}
```

> 💬 **Interview Tip**: “Tailwind is perfect for rapid prototyping and teams that want consistency. It eliminates context-switching between files. The utility classes are highly optimized, and PurgeCSS removes unused styles in production. My favorite for new projects in 2025.”

---

## 🎨 4. CSS-in-JS vs Utility CSS — When to Use What?

| Method          | Best For                                      | Pros                                      | Cons                                     | Interview Answer                                                                 |
|-----------------|-----------------------------------------------|-------------------------------------------|------------------------------------------|----------------------------------------------------------------------------------|
| **CSS Modules** | Teams familiar with CSS, migration projects   | Zero runtime, familiar, scoped            | Less dynamic, separate files             | “Great for legacy migration or teams that prefer traditional CSS workflows.”     |
| **Styled Components** | Dynamic UIs, design systems, theming   | Highly dynamic, co-located styles, theming | Runtime cost, learning curve             | “I choose this for complex, interactive components where styles depend heavily on state or props.” |
| **Tailwind CSS** | Rapid development, consistency, new projects | Fast dev, no naming, highly optimized     | Learning utility classes, verbose JSX    | “My default for new projects. It’s fast, consistent, and the JIT engine is magic.” |

> 💬 **Ultimate Interview Answer**:  
> “For a new greenfield project, I’d pick **Tailwind CSS** for its speed and consistency. For a complex design system with heavy theming, I’d lean towards **Styled Components**. And if I’m working on a large legacy codebase, **CSS Modules** is the safest, incremental upgrade path. The key is matching the tool to the team’s expertise and the project’s needs.”

---

## 🌓 5. Dynamic & Conditional Styling

### 💡 Concept
Change styles based on state, props, or user interaction.

### 🎯 Real-World Interview Example
> *“Create a collapsible panel that changes its arrow icon and background color when open.”*

#### ✅ With Tailwind (Recommended for simplicity)
```jsx
import { useState } from 'react';

function CollapsiblePanel({ title, children }) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="border rounded-lg overflow-hidden shadow-sm">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className={`w-full p-4 text-left font-medium transition-colors ${
          isOpen ? 'bg-blue-50 text-blue-800' : 'bg-white hover:bg-gray-50'
        }`}
      >
        {title}
        <span className={`float-right transition-transform ${isOpen ? 'rotate-180' : ''}`}>
          ▼
        </span>
      </button>
      {isOpen && (
        <div className="p-4 border-t bg-white">
          {children}
        </div>
      )}
    </div>
  );
}
```

#### ✅ With Styled Components (For complex logic)
```jsx
const PanelButton = styled.button`
  width: 100%;
  padding: 1rem;
  text-align: left;
  font-weight: bold;
  background: ${props => props.isOpen ? '#f0f9ff' : 'white'};
  color: ${props => props.isOpen ? '#1e40af' : 'inherit'};
  border: none;
  cursor: pointer;

  &:hover {
    background: ${props => props.isOpen ? '#e0f2fe' : '#f9fafb'};
  }
`;

const ArrowIcon = styled.span`
  float: right;
  transition: transform 0.2s ease;
  transform: ${props => props.isOpen ? 'rotate(180deg)' : 'rotate(0)'};
`;

function CollapsiblePanel({ title, children }) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="border rounded-lg overflow-hidden shadow-sm">
      <PanelButton isOpen={isOpen} onClick={() => setIsOpen(!isOpen)}>
        {title}
        <ArrowIcon isOpen={isOpen}>▼</ArrowIcon>
      </PanelButton>
      {isOpen && (
        <div className="p-4 border-t bg-white">
          {children}
        </div>
      )}
    </div>
  );
}
```

> 💬 **Interview Tip**: “I prefer Tailwind for simple conditional classes — it’s readable and fast. For complex, interpolated styles (like animating based on a numeric prop), Styled Components is more expressive.”

---

## 🌍 6. Theming & Dark Mode

### 💡 Concept
Allow users to switch between light and dark themes. Often combined with React Context.

### 🎯 Real-World Interview Example
> *“Implement a dark mode toggle using Tailwind CSS and Context.”*

```jsx
// 1. ThemeContext.js
import { createContext, useContext, useState, useEffect } from 'react';

const ThemeContext = createContext();

export const useTheme = () => useContext(ThemeContext);

export function ThemeProvider({ children }) {
  const [darkMode, setDarkMode] = useState(() => {
    // Check user preference or localStorage
    if (typeof window !== 'undefined') {
      return localStorage.getItem('darkMode') === 'true' ||
             window.matchMedia('(prefers-color-scheme: dark)').matches;
    }
    return false;
  });

  useEffect(() => {
    // Apply class to body
    if (darkMode) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('darkMode', 'true');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('darkMode', 'false');
    }
  }, [darkMode]);

  const toggleDarkMode = () => setDarkMode(!darkMode);

  return (
    <ThemeContext.Provider value={{ darkMode, toggleDarkMode }}>
      {children}
    </ThemeContext.Provider>
  );
}
```

📁 `tailwind.config.js` (Enable dark mode)
```js
module.exports = {
  darkMode: 'class', // ✅ Enable class-based dark mode
  // ... rest of config
}
```

📁 `App.jsx`
```jsx
import { useTheme } from './ThemeContext';

function ThemeToggle() {
  const { darkMode, toggleDarkMode } = useTheme();

  return (
    <button
      onClick={toggleDarkMode}
      className="p-2 rounded-full bg-gray-200 dark:bg-gray-700"
    >
      {darkMode ? '☀️' : '🌙'}
    </button>
  );
}

function App() {
  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-900 text-gray-900 dark:text-gray-100 transition-colors duration-200">
      <header className="p-4 flex justify-between items-center bg-white dark:bg-gray-800 shadow">
        <h1>My App</h1>
        <ThemeToggle />
      </header>
      <main className="p-6">
        <h2>Welcome to the app!</h2>
        <p>This text will change color based on your theme.</p>
      </main>
    </div>
  );
}
```

> 💬 **Interview Tip**: “I implement dark mode by toggling a `.dark` class on the `<html>` element and using Tailwind’s `dark:` prefix. I persist the user’s choice in localStorage and respect their OS preference initially. Context is perfect for sharing the theme state globally.”

