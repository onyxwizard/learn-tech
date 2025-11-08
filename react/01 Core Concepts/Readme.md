

# 📘 React Quick Notes — Chapter 1: Core React Concepts 🧠⚛️  
> *"Master the fundamentals like a pro — interview-ready, real-world examples included!"*



## 🗂️ Table of Contents

1.  [🧩 Components (Functional & Class)](#-1-components-functional--class)
2.  [📝 JSX Syntax](#-2-jsx-syntax)
3.  [📦 Props and State](#-3-props-and-state)
4.  [🔄 The Component Lifecycle (Class) / useEffect Hook (Functional)](#-4-the-component-lifecycle-class--useeffect-hook-functional)
5.  [🖱️ Event Handling](#️-5-event-handling)
6.  [🎯 Conditional Rendering](#-6-conditional-rendering)
7.  [📋 Lists and Keys](#-7-lists-and-keys)


## 🧩 1. Components (Functional & Class)

### 💡 Concept
Components are the LEGO blocks of React. You build UIs by composing them. Two types: **Functional (modern)** and **Class (legacy, but still asked in interviews)**.

### 🎯 Real-World Interview Example
> *“Show me how you’d create a simple ‘Welcome’ component using both functional and class syntax.”*

#### ✅ Functional Component (Preferred)
```jsx
// Functional Component — Clean & Modern
function Welcome({ name }) {
  return <h1>Hello, {name}! 👋</h1>;
}

// Usage
<Welcome name="Alice" />
```

#### ✅ Class Component (Legacy — Know It!)
```jsx
// Class Component — For Interviews & Legacy Codebases
import React, { Component } from 'react';

class Welcome extends Component {
  render() {
    return <h1>Hello, {this.props.name}! 👋</h1>;
  }
}

// Usage
<Welcome name="Bob" />
```

> 💬 **Interview Tip**: “I prefer functional components with hooks for simplicity and readability. Class components are useful to understand for maintaining older codebases.”

---

## 📝 2. JSX Syntax

### 💡 Concept
JSX = JavaScript + XML. It lets you write HTML-like code in JavaScript. It’s syntactic sugar — gets compiled to `React.createElement()` calls.

### 🎯 Real-World Interview Example
> *“Explain what JSX is and how this gets transformed under the hood.”*

```jsx
const element = <h1 className="greet">Hello, world!</h1>;
```

⬇️ Compiles to ⬇️

```js
const element = React.createElement(
  'h1',
  { className: 'greet' },
  'Hello, world!'
);
```

### 🚫 Common Gotchas
```jsx
// ❌ Wrong — HTML attributes
<div class="box">...</div>

// ✅ Correct — JSX uses camelCase & className
<div className="box">...</div>

// ✅ Embedding JS expressions
<p>You have {itemCount} items in cart 🛒</p>
```

> 💬 **Interview Tip**: “JSX isn’t HTML — it’s closer to JavaScript. That’s why we use `className` instead of `class`, and we can embed JS with `{}`.”

---

## 📦 3. Props and State

### 💡 Concept
- **Props**: Read-only data passed from parent to child. Like function arguments.
- **State**: Mutable data managed within the component. Triggers re-render when changed.

### 🎯 Real-World Interview Example
> *“Build a Counter component that receives a `title` prop and manages its own count state.”*

```jsx
import { useState } from 'react';

function Counter({ title }) {
  const [count, setCount] = useState(0); // 🔁 State

  return (
    <div>
      <h2>{title} 📊</h2> {/* 📥 Prop */}
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>
        Increment ➕
      </button>
    </div>
  );
}

// Usage
<Counter title="User Clicks" />
```

> 💬 **Interview Tip**: “Props are for configuration, State is for interaction. Never mutate props — if you need to change something based on a prop, copy it into state (carefully!) or lift state up.”

---

## 🔄 4. The Component Lifecycle (Class) / useEffect Hook (Functional)

### 💡 Concept
How components behave over time: Mount → Update → Unmount.

- **Class**: `componentDidMount`, `componentDidUpdate`, `componentWillUnmount`
- **Functional**: `useEffect` hook replaces all three.

### 🎯 Real-World Interview Example
> *“Show how to fetch user data when a component mounts, using both class and functional styles.”*

#### ✅ Class Component (Legacy)
```jsx
class UserProfile extends Component {
  state = { user: null };

  componentDidMount() {
    fetch('/api/user/123')
      .then(res => res.json())
      .then(user => this.setState({ user }));
  }

  render() {
    return <div>{this.state.user?.name || 'Loading...'}</div>;
  }
}
```

#### ✅ Functional Component (Modern)
```jsx
import { useState, useEffect } from 'react';

function UserProfile() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetch('/api/user/123')
      .then(res => res.json())
      .then(setUser);
  }, []); // 🚨 Empty dependency array = componentDidMount

  return <div>{user?.name || 'Loading...'}</div>;
}
```

> 💬 **Interview Tip**: “I use `useEffect` with an empty dependency array for ‘componentDidMount’ behavior. Always remember cleanup for subscriptions or timers to avoid memory leaks!”

---

## 🖱️ 5. Event Handling

### 💡 Concept
React events are named using camelCase (`onClick`), and you pass a function, not a string.

### 🎯 Real-World Interview Example
> *“Create a button that logs its click count. Show proper event binding.”*

```jsx
import { useState } from 'react';

function ClickLogger() {
  const [clicks, setClicks] = useState(0);

  const handleClick = () => {
    setClicks(prev => prev + 1); // ✅ Use functional update
    console.log(`Button clicked ${clicks + 1} times!`);
  };

  return (
    <button onClick={handleClick}>
      Click Me! 🖱️ ({clicks})
    </button>
  );
}
```

> 💬 **Interview Tip**: “Always avoid inline arrow functions in render if passed to frequently-rendering children — it can hurt performance. Define handlers outside or use `useCallback`.”

---

## 🎯 6. Conditional Rendering

### 💡 Concept
Render different UI based on state or props. Use `&&`, ternary `? :`, or variables.

### 🎯 Real-World Interview Example
> *“Show a ‘Login’ button if user is not logged in, else show ‘Welcome, [name]’ and a ‘Logout’ button.”*

```jsx
function Greeting({ isLoggedIn, username }) {
  return (
    <div>
      {isLoggedIn ? (
        <>
          <p>Welcome back, {username}! 😊</p>
          <button>Logout 🚪</button>
        </>
      ) : (
        <button>Login to Continue 🔐</button>
      )}
    </div>
  );
}
```

#### ✅ Using `&&` for simple conditions
```jsx
{isLoading && <p>Loading... ⏳</p>}
{error && <p>Error: {error.message} ❌</p>}
```

> 💬 **Interview Tip**: “I prefer ternary for if/else UI, and `&&` for simple ‘if’ conditions. Avoid complex logic in JSX — extract to a variable or helper function.”

---

## 📋 7. Lists and Keys

### 💡 Concept
Use `map()` to render lists. Each item needs a unique `key` prop for React’s reconciliation.

### 🎯 Real-World Interview Example
> *“Render a list of todo items. Each todo has an id and text. Explain why keys are important.”*

```jsx
function TodoList({ todos }) {
  return (
    <ul>
      {todos.map(todo => (
        <li key={todo.id}> {/* ✅ Key is CRITICAL */}
          {todo.text} ✅
        </li>
      ))}
    </ul>
  );
}

// Sample data
const todos = [
  { id: 1, text: 'Learn React' },
  { id: 2, text: 'Build a project' },
  { id: 3, text: 'Ace the interview' }
];
```

> 💬 **Interview Tip**: “Keys help React identify which items have changed, been added, or removed. Never use index as key if the list can reorder — it can cause bugs. Use a unique ID from your data.”

---