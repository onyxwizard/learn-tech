# 📘 React Quick Notes — Chapter 5: Advanced Patterns & Performance 🚀  
> *“Build apps that are fast, resilient, and architecturally sound.”*



## 🗂️ Table of Contents

1.  [🧩 Higher-Order Components (HOCs)](#-1-higher-order-components-hocs)
2.  [🎨 Render Props Pattern](#-2-render-props-pattern)
3.  [⚡ Code Splitting with `React.lazy` & `Suspense`](#-3-code-splitting-with-reactlazy--suspense)
4.  [🛡️ Error Boundaries](#-4-error-boundaries)
5.  [📉 Virtualization (React Window)](#-5-virtualization-react-window)
6.  [🔍 Performance Debugging: React DevTools & Profiler](#-6-performance-debugging-react-devtools--profiler)



## 🧩 1. Higher-Order Components (HOCs)

### 💡 Concept
A function that takes a component and returns a new component with additional props or behavior. Common in legacy code (e.g., `connect` from Redux).

### 🎯 Real-World Interview Example
> *“Create a HOC that injects the current user and a ‘loading’ state into any component.”*

```jsx
import { useAuth } from './auth'; // Assume you have this hook

// ✅ HOC: withAuth
function withAuth(WrappedComponent) {
  return function WithAuthComponent(props) {
    const { user, loading } = useAuth();

    if (loading) {
      return <div>Loading user... ⏳</div>;
    }

    // Inject `user` as a prop to the wrapped component
    return <WrappedComponent {...props} user={user} />;
  };
}

// Usage: Wrap any component that needs user data
function UserProfile({ user }) { // ← user is injected by HOC
  if (!user) {
    return <div>Please log in to view profile. 🔐</div>;
  }
  return (
    <div>
      <h2>Welcome, {user.name}! 👋</h2>
      <p>Email: {user.email}</p>
    </div>
  );
}

// ✨ Wrap it!
const UserProfileWithAuth = withAuth(UserProfile);

// In your route or app
<Route path="/profile" element={<UserProfileWithAuth />} />
```

> 💬 **Interview Tip**: “HOCs were popular before hooks. I still use them occasionally for cross-cutting concerns like logging or auth. But today, I prefer Custom Hooks for logic reuse — they’re more composable and don’t suffer from wrapper hell.”

---

## 🎨 2. Render Props Pattern

### 💡 Concept
A component accepts a function (the “render prop”) as a prop, which it calls to render its output. Great for sharing logic without HOCs.

### 🎯 Real-World Interview Example
> *“Create a `MouseTracker` component that tracks mouse position and uses a render prop to let the parent decide how to display it.”*

```jsx
import { useState, useEffect } from 'react';

// ✅ MouseTracker Component with Render Prop
function MouseTracker({ render }) {
  const [position, setPosition] = useState({ x: 0, y: 0 });

  useEffect(() => {
    const handleMouseMove = (e) => {
      setPosition({ x: e.clientX, y: e.clientY });
    };

    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  // 🎨 Call the render prop function and pass it data
  return render(position);
}

// Usage: Parent decides what to render
function App() {
  return (
    <div>
      <h1>🎨 Render Props Demo</h1>
      <MouseTracker
        render={(mouse) => (
          <p>
            Mouse at: ({mouse.x}, {mouse.y}) 🐭
          </p>
        )}
      />
      {/* You can reuse the same logic to render something else! */}
      <MouseTracker
        render={(mouse) => (
          <div
            style={{
              width: '20px',
              height: '20px',
              background: 'red',
              position: 'absolute',
              left: mouse.x - 10,
              top: mouse.y - 10,
              borderRadius: '50%'
            }}
          />
        )}
      />
    </div>
  );
}
```

> 💬 **Interview Tip**: “Render Props are powerful for sharing stateful logic. They avoid HOC naming collisions and are very flexible. But they can lead to nested ‘callback hell’. Today, I’d often use a Custom Hook instead — like `useMousePosition()` — for cleaner code.”

---

## ⚡ 3. Code Splitting with `React.lazy` & `Suspense`

### 💡 Concept
Split your bundle and load components only when needed. Reduces initial load time.

### 🎯 Real-World Interview Example
> *“Lazy-load a heavy ‘Dashboard’ component. Show a loading spinner while it loads.”*

```jsx
import { Suspense, lazy } from 'react';

// ✅ Lazy-load the Dashboard component
const LazyDashboard = lazy(() => import('./Dashboard'));

function App() {
  return (
    <div>
      <h1>My App</h1>
      {/* Wrap lazy component in Suspense */}
      <Suspense fallback={<div>Loading Dashboard... 🌀</div>}>
        <LazyDashboard />
      </Suspense>
    </div>
  );
}

// Dashboard.jsx (this file will be split into a separate chunk)
function Dashboard() {
  // Imagine this imports heavy charts or libraries
  return (
    <div>
      <h2>📊 Welcome to your Lazy-Loaded Dashboard!</h2>
      <p>This component was loaded only when needed.</p>
    </div>
  );
}

export default Dashboard;
```

> 💬 **Interview Tip**: “I use `React.lazy` for route-level or feature-level code splitting. Always wrap it in `Suspense` with a fallback. For more granular control (like loading multiple components), I use libraries like `loadable-components`.”

---

## 🛡️ 4. Error Boundaries

### 💡 Concept
A component that catches JavaScript errors anywhere in its child component tree, logs them, and displays a fallback UI.

### 🎯 Real-World Interview Example
> *“Create an Error Boundary that catches errors in a child component and shows a friendly message.”*

```jsx
import { Component } from 'react';

// ✅ Error Boundary Class Component
class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    // Update state to trigger fallback UI
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    // Log error to service (Sentry, etc.)
    console.error("Error caught by boundary:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{ border: '2px solid red', padding: '20px' }}>
          <h2>Something went wrong! 🛑</h2>
          <p>{this.state.error?.toString()}</p>
          <button onClick={() => this.setState({ hasError: false, error: null })}>
            Try Again 🔄
          </button>
        </div>
      );
    }

    return this.props.children; // Render children if no error
  }
}

// A component that throws an error
function BuggyComponent() {
  throw new Error("I crashed! 💥");
  return <div>This won't render</div>;
}

// Usage
function App() {
  return (
    <div>
      <h1>🛡️ Error Boundary Demo</h1>
      <ErrorBoundary>
        <BuggyComponent /> {/* This will crash */}
      </ErrorBoundary>
    </div>
  );
}
```

> 💬 **Interview Tip**: “Error Boundaries are crucial for production apps. They prevent the whole app from crashing due to one component’s error. Remember: they only catch errors in lifecycle methods, constructors, and render — not in event handlers or async code.”

---

## 📉 5. Virtualization (React Window)

### 💡 Concept
Render only the items currently visible in a large list. Dramatically improves performance.

### 🎯 Real-World Interview Example
> *“Render a list of 10,000 items efficiently using `react-window`.”*

```bash
npm install react-window
```

```jsx
import { FixedSizeList as List } from 'react-window';

const items = Array.from({ length: 10000 }, (_, index) => `Item ${index + 1}`);

// ✅ Row Renderer Component
function Row({ index, style }) {
  return (
    <div style={style} style={{ ...style, padding: '10px', borderBottom: '1px solid #eee' }}>
      {items[index]}
    </div>
  );
}

function VirtualizedList() {
  return (
    <div>
      <h2>📉 Virtualized List (10,000 items)</h2>
      <List
        height={600} // Container height
        itemCount={items.length} // Total items
        itemSize={50} // Height of each item
        width="100%"
      >
        {Row} {/* Render prop for each visible row */}
      </List>
    </div>
  );
}
```

> 💬 **Interview Tip**: “If you’re rendering long lists (>100 items), virtualization is non-negotiable for performance. `react-window` is the gold standard — lightweight and highly optimized. Never render huge lists with `.map()` directly.”

---

## 🔍 6. Performance Debugging: React DevTools & Profiler

### 💡 Concept
Use React DevTools to identify unnecessary re-renders and measure performance.

### 🎯 Real-World Interview Example
> *“You notice your app is slow. Walk me through how you’d diagnose and fix it.”*

#### ✅ Step-by-Step Debugging Process:

1.  **Open React DevTools → Profiler Tab.**
2.  **Record Interaction:** Click “Start Profiling”, interact with your app (e.g., type in a search box), then “Stop Profiling”.
3.  **Analyze Flamegraph:** Look for components that re-render too often or take too long.
4.  **Check Why It Re-rendered:** Click a component in the profiler → “Why did this render?”.
5.  **Apply Fixes:**
    -   Wrap expensive components in `React.memo`.
    -   Use `useCallback` for event handlers passed to children.
    -   Use `useMemo` for expensive calculations.
    -   Implement virtualization for long lists.

```jsx
// Example: Optimizing a child component
const ExpensiveChild = React.memo(({ data, onClick }) => {
  console.log("ExpensiveChild rendered"); // Log to see if it re-renders
  // ... heavy rendering logic
  return <div>{data}</div>;
});

// Parent
function Parent() {
  const [count, setCount] = useState(0);
  const [items, setItems] = useState([]);

  // ✅ Memoize handler so it doesn't change on every render
  const handleClick = useCallback(() => {
    console.log("Item clicked");
  }, []);

  // Only recompute if items change
  const processedItems = useMemo(() => {
    return items.map(item => heavyTransformation(item));
  }, [items]);

  return (
    <div>
      <button onClick={() => setCount(c => c + 1)}>Re-render Parent ({count})</button>
      {processedItems.map(item => (
        <ExpensiveChild key={item.id} data={item} onClick={handleClick} />
      ))}
    </div>
  );
}
```

> 💬 **Interview Tip**: “I start with the Profiler to find the bottleneck. Most performance issues come from unnecessary re-renders. I fix them with `React.memo`, `useCallback`, and `useMemo` — but only after profiling, not prematurely!”
