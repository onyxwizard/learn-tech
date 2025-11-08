# 📘 React Quick Notes — Chapter 2: React Hooks Deep Dive 🪝  
> *“Hooks are where React gets superpowers. Understand them deeply — they’re 90% of modern interviews.”*


## 🗂️ Table of Contents

1.  [.useState — Managing Local State](#-1-usestate--managing-local-state)
2.  [useEffect — Side Effects & Lifecycle](#-2-useeffect--side-effects--lifecycle)
3.  [useContext — Avoiding Prop Drilling](#-3-usecontext--avoiding-prop-drilling)
4.  [useReducer — Complex State Logic](#-4-usereducer--complex-state-logic)
5.  [useCallback & useMemo — Performance Optimization](#-5-usecallback--usememo--performance-optimization)
6.  [useRef — Accessing DOM & Persisting Values](#-6-useref--accessing-dom--persisting-values)
7.  [Custom Hooks — Reusable Logic](#-7-custom-hooks--reusable-logic)



## 🪝 1. `useState` — Managing Local State

### 💡 Concept
The foundational hook for adding state to functional components. Returns a stateful value and a function to update it.

### 🎯 Real-World Interview Example
> *“Create a form with two inputs: name and email. Manage their state using `useState`. Show how to handle controlled inputs.”*

```jsx
import { useState } from 'react';

function UserForm() {
  const [formData, setFormData] = useState({
    name: '',
    email: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value // 🔑 Dynamic key update
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form Submitted:', formData);
    alert(`Welcome, ${formData.name}! 🎉`);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        name="name"
        value={formData.name}
        onChange={handleChange}
        placeholder="Your Name"
        required
      />
      <input
        name="email"
        type="email"
        value={formData.email}
        onChange={handleChange}
        placeholder="Your Email"
        required
      />
      <button type="submit">Submit 🚀</button>
    </form>
  );
}
```

> 💬 **Interview Tip**: “I always use the functional update form `setFormData(prev => ...)` when the new state depends on the previous state. This avoids stale closures, especially in async scenarios or rapid updates.”

---

## ⚡ 2. `useEffect` — Side Effects & Lifecycle

### 💡 Concept
Performs side effects in function components (data fetching, subscriptions, manually changing DOM). Replaces `componentDidMount`, `componentDidUpdate`, and `componentWillUnmount`.

### 🎯 Real-World Interview Example
> *“Fetch a list of products from an API when the component mounts. Also, add a search feature that re-fetches when the search term changes. Include cleanup to cancel requests.”*

```jsx
import { useState, useEffect } from 'react';

function ProductList() {
  const [products, setProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const controller = new AbortController(); // 🧹 Cleanup for fetch

    const fetchProducts = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `/api/products?q=${searchTerm}`,
          { signal: controller.signal }
        );
        const data = await response.json();
        setProducts(data);
      } catch (err) {
        if (err.name !== 'AbortError') {
          console.error('Fetch error:', err);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();

    return () => controller.abort(); // 🚫 Cleanup on unmount or re-run
  }, [searchTerm]); // 🔄 Re-run when searchTerm changes

  return (
    <div>
      <input
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder="Search products..."
      />
      {loading ? (
        <p>Loading products... ⏳</p>
      ) : (
        <ul>
          {products.map(product => (
            <li key={product.id}>{product.name}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
```

> 💬 **Interview Tip**: “The dependency array is crucial. An empty array `[]` means ‘run once on mount’. Including a variable like `[searchTerm]` means ‘run when this changes’. Always clean up subscriptions or network requests to prevent memory leaks and race conditions.”

---

## 🌐 3. `useContext` — Avoiding Prop Drilling

### 💡 Concept
Allows you to share state globally without passing props down manually through every level (prop drilling).

### 🎯 Real-World Interview Example
> *“Create a ThemeContext to toggle between light and dark mode. Show how a deeply nested component can access the theme without intermediate props.”*

```jsx
// 1. Create Context
import { createContext, useContext, useState } from 'react';

const ThemeContext = createContext();

// 2. Create Provider Component
function ThemeProvider({ children }) {
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleTheme = () => setIsDarkMode(prev => !prev);

  return (
    <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

// 3. Custom Hook for Easy Access
const useTheme = () => useContext(ThemeContext);

// 4. A Deeply Nested Component
function ThemedButton() {
  const { isDarkMode, toggleTheme } = useTheme();

  return (
    <button
      onClick={toggleTheme}
      style={{
        background: isDarkMode ? '#333' : '#eee',
        color: isDarkMode ? '#fff' : '#000',
        padding: '10px 20px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer'
      }}
    >
      {isDarkMode ? '🌙 Switch to Light' : '☀️ Switch to Dark'}
    </button>
  );
}

// 5. App Usage
function App() {
  return (
    <ThemeProvider>
      <div>
        <h1>My App</h1>
        {/* Imagine this button is 5 levels deep */}
        <ThemedButton />
      </div>
    </ThemeProvider>
  );
}
```

> 💬 **Interview Tip**: “I use Context for truly global state like themes, user auth, or language settings. For complex state with frequent updates, I’d consider Zustand or Redux to avoid unnecessary re-renders.”

---

## 🔄 4. `useReducer` — Complex State Logic

### 💡 Concept
An alternative to `useState` for managing complex state logic. Inspired by Redux. Great for when state involves multiple sub-values or when the next state depends on the previous one.

### 🎯 Real-World Interview Example
> *“Build a shopping cart reducer that can add, remove, and clear items. Each item has an id, name, and quantity.”*

```jsx
import { useReducer } from 'react';

// 1. Define Actions
const ADD_ITEM = 'ADD_ITEM';
const REMOVE_ITEM = 'REMOVE_ITEM';
const CLEAR_CART = 'CLEAR_CART';

// 2. Reducer Function
function cartReducer(state, action) {
  switch (action.type) {
    case ADD_ITEM: {
      const existingItem = state.find(item => item.id === action.payload.id);
      if (existingItem) {
        return state.map(item =>
          item.id === action.payload.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      } else {
        return [...state, { ...action.payload, quantity: 1 }];
      }
    }
    case REMOVE_ITEM:
      return state.filter(item => item.id !== action.payload.id);
    case CLEAR_CART:
      return [];
    default:
      throw new Error(`Unhandled action type: ${action.type}`);
  }
}

// 3. Component Using useReducer
function ShoppingCart() {
  const [cart, dispatch] = useReducer(cartReducer, []);

  const addItem = (item) => {
    dispatch({ type: ADD_ITEM, payload: item });
  };

  const removeItem = (id) => {
    dispatch({ type: REMOVE_ITEM, payload: { id } });
  };

  const clearCart = () => {
    dispatch({ type: CLEAR_CART });
  };

  return (
    <div>
      <h2>🛒 Cart ({cart.reduce((sum, item) => sum + item.quantity, 0)} items)</h2>
      <button onClick={() => addItem({ id: 1, name: 'Apple' })}>
        Add Apple 🍎
      </button>
      <button onClick={() => addItem({ id: 2, name: 'Banana' })}>
        Add Banana 🍌
      </button>
      <ul>
        {cart.map(item => (
          <li key={item.id}>
            {item.name} x{item.quantity}
            <button onClick={() => removeItem(item.id)}>Remove</button>
          </li>
        ))}
      </ul>
      {cart.length > 0 && (
        <button onClick={clearCart}>Clear Cart 🗑️</button>
      )}
    </div>
  );
}
```

> 💬 **Interview Tip**: “I reach for `useReducer` when state logic gets complex or when I need to debug state changes (you can log actions easily). It makes state transitions predictable and is easier to test.”

---

## 🚀 5. `useCallback` & `useMemo` — Performance Optimization

### 💡 Concept
- `useCallback`: Memoizes a function to prevent unnecessary re-creation.
- `useMemo`: Memoizes a computed value to avoid expensive recalculations.

### 🎯 Real-World Interview Example
> *“You have a parent component rendering a list of expensive child components. Optimize it so children only re-render when their specific data changes.”*

```jsx
import { useState, useCallback, useMemo } from 'react';

// 🐢 Expensive Child Component (e.g., renders charts)
const ExpensiveChild = ({ data, onRemove }) => {
  console.log(`Rendering child for ${data.id}`); // Log to see re-renders

  // Simulate expensive calculation
  const processedData = useMemo(() => {
    return data.items.map(item => item * 2); // Imagine this is heavy
  }, [data.items]);

  return (
    <div>
      <h3>{data.name}</h3>
      <p>Processed: {processedData.join(', ')}</p>
      <button onClick={() => onRemove(data.id)}>Remove ❌</button>
    </div>
  );
};

// Parent Component
function Parent() {
  const [items, setItems] = useState([
    { id: 1, name: 'List A', items: [1, 2, 3] },
    { id: 2, name: 'List B', items: [4, 5, 6] },
  ]);

  const [count, setCount] = useState(0); // 🎯 This will trigger re-renders

  // ✅ Memoize the handler so it doesn't change on every render
  const handleRemove = useCallback((id) => {
    setItems(prev => prev.filter(item => item.id !== id));
  }, []); // Empty deps since setItems never changes

  // ✅ Memoize the list of children so they don't re-render unnecessarily
  const renderedChildren = useMemo(() => {
    return items.map(item => (
      <ExpensiveChild
        key={item.id}
        data={item}
        onRemove={handleRemove}
      />
    ));
  }, [items, handleRemove]); // Only recompute if items or handler changes

  return (
    <div>
      <h2>Parent Component</h2>
      <p>Counter (unrelated state): {count}</p>
      <button onClick={() => setCount(c => c + 1)}>Increment Counter ➕</button>
      <div>
        {renderedChildren}
      </div>
    </div>
  );
}
```

> 💬 **Interview Tip**: “Don’t optimize prematurely! Only use `useCallback` and `useMemo` when you’ve identified a performance bottleneck via profiling. Overusing them can hurt performance due to the overhead of memoization.”

---

## 📌 6. `useRef` — Accessing DOM & Persisting Values

### 💡 Concept
Returns a mutable ref object whose `.current` property is persisted across renders. Used for:
- Accessing DOM nodes directly (focus, measurements).
- Storing mutable values that don’t trigger re-renders (like previous state, timers, intervals).

### 🎯 Real-World Interview Example
> *“Create an input that auto-focuses when the component mounts. Also, track how many times a button has been clicked without causing re-renders.”*

```jsx
import { useRef, useEffect } from 'react';

function InputWithFocusAndClickTracker() {
  const inputRef = useRef(null);
  const clickCountRef = useRef(0); // 🧠 Doesn't trigger re-render

  useEffect(() => {
    // Focus the input on mount
    inputRef.current.focus();
  }, []);

  const handleClick = () => {
    clickCountRef.current += 1; // 🚫 No re-render
    console.log(`Button clicked ${clickCountRef.current} times`);
  };

  return (
    <div>
      <input
        ref={inputRef} // 🔗 Connect ref to DOM element
        type="text"
        placeholder="Auto-focused input 🎯"
      />
      <button onClick={handleClick}>
        Click Me (Count stored in ref) 🖱️
      </button>
      {/* This text WON'T update on click because ref change doesn't trigger render */}
      <p>Clicks (won't update): {clickCountRef.current}</p>
    </div>
  );
}
```

> 💬 **Interview Tip**: “I use `useRef` for imperative actions like focusing inputs or measuring DOM elements. For storing values like previous props/state or intervals, it’s perfect because it doesn’t cause re-renders. Remember, mutating `ref.current` is side-effect free in terms of rendering.”

---

## 🧩 7. Custom Hooks — Reusable Logic

### 💡 Concept
Functions that let you extract component logic into reusable functions. Must start with `use`.

### 🎯 Real-World Interview Example
> *“Create a custom hook `useLocalStorage` that syncs state with localStorage. Then use it to persist a user’s theme preference.”*

```jsx
import { useState, useEffect } from 'react';

// ✅ Custom Hook: useLocalStorage
function useLocalStorage(key, initialValue) {
  // Initialize state from localStorage or default
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(error);
      return initialValue;
    }
  });

  // Update localStorage when state changes
  useEffect(() => {
    try {
      window.localStorage.setItem(key, JSON.stringify(storedValue));
    } catch (error) {
      console.error(error);
    }
  }, [key, storedValue]);

  return [storedValue, setStoredValue];
}

// Usage: Persisting Theme
function App() {
  const [theme, setTheme] = useLocalStorage('app-theme', 'light');

  const toggleTheme = () => {
    setTheme(prev => (prev === 'light' ? 'dark' : 'light'));
  };

  return (
    <div style={{ background: theme === 'dark' ? '#333' : '#fff', color: theme === 'dark' ? '#fff' : '#000', padding: '20px' }}>
      <h1>Custom Hook Demo</h1>
      <p>Current theme: <strong>{theme}</strong></p>
      <button onClick={toggleTheme}>
        Toggle Theme 🌓
      </button>
      {/* Refresh the page — your choice is saved! */}
    </div>
  );
}
```

> 💬 **Interview Tip**: “Custom hooks are my favorite way to share logic between components. They can encapsulate complex behavior like data fetching, form handling, or in this case, persistence. The key rule: always start the name with `use` so React can enforce the Rules of Hooks.”
