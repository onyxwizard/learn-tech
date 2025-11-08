## 🗃️ Chapter 3 Checkpoint: Lifting State + Child Control

> **🎯 Challenge (Interview Question Style):**  
> *“You have a parent component rendering a list of child `<Counter />` components. Each counter should manage its own count. But you also want a ‘Reset All’ button in the parent that resets every child counter to zero. How would you design this?”*

✅ **What I’m Testing:**
- Can you decide **where state should live**?
- Can you **lift state up** when needed?
- Can you **pass callbacks** from parent to children?
- Can you **reset multiple children** from a single parent action?



📝 **Your Turn!**  
Design the `Parent` and `Counter` components.

*(Starter template if you need it:)*

```jsx
// Counter.jsx
export function Counter({ /* ??? */ }) {
  // Should it manage its own state? Or receive it from parent?
}

// Parent.jsx
import { Counter } from './Counter';

export function Parent() {
  // Manage state for all counters here?
  // Render multiple <Counter /> and a "Reset All" button
}
```


💡 **Hint**: If the parent needs to control or reset the children’s state, the state probably shouldn’t live in the children!

