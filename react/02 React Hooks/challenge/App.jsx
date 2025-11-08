// App.js
import { useToggle } from './useThemeToggle';

export function App() {
  const { bgColor, textColor, toggleTheme } = useToggle();

  return (
    <div style={{ backgroundColor: bgColor, color: textColor, padding: '40px' }}>
      <h2>Custom Theme Toggle</h2>
      <button onClick={toggleTheme}>Switch Theme 🌓</button>
    </div>
  );
}