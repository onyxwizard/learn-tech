
## 🪝 Chapter 2 Checkpoint: Custom Hooks

> **🎯 Challenge (Interview Question Style):**  
> *“Create a custom hook called `useToggle` that returns a boolean value and a function to toggle it. Then, use it in a component to toggle between ‘light’ and ‘dark’ mode. Log the current mode to the console on every toggle.”*

✅ **What I’m Testing:**
- Can you create a **custom hook** (function starting with `use`)?
- Can you encapsulate `useState` logic inside it?
- Can you **reuse** this hook in a component?
- Do you understand **hook rules** (only call hooks at top level)?



📝 **Your Turn!**  
Write your `useToggle` hook and the component that uses it.

*(Starter template if you need it:)*

```jsx
// useToggle.js
import { useState } from 'react';

export function useToggle(initialValue = false) {
  // Your hook logic here...
}

// App.js
import { useToggle } from './useToggle';

export function App() {
  // Use your hook here...
}
```
