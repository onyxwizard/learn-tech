# 📘 React Quick Notes — Chapter 8: Testing 🧪  
> *“Write tests that catch bugs, not flaky nightmares. Focus on user behavior, not implementation.”*



## 🗂️ Table of Contents

1.  [🧪 Why Test? Unit vs Integration vs E2E](#-1-why-test-unit-vs-integration-vs-e2e)
2.  [⚙️ Setting Up Jest & React Testing Library (RTL)](#-2-setting-up-jest--react-testing-library-rtl)
3.  [🔍 Testing Components: Render, Fire Events, Assert](#-3-testing-components-render-fire-events-assert)
4.  [🧩 Testing Custom Hooks with `@testing-library/react-hooks`](#-4-testing-custom-hooks-with-testing-libraryreact-hooks)
5.  [🌀 Mocking: API Calls, Child Components, Modules](#-5-mocking-api-calls-child-components-modules)
6.  [✅ Best Practices: What to Test, What Not to Test](#-6-best-practices-what-to-test-what-not-to-test)


## 🧪 1. Why Test? Unit vs Integration vs E2E

### 💡 Concept
- **Unit Test**: Tests a single function or component in isolation. Fast, cheap.
- **Integration Test**: Tests how multiple units work together. (This is what RTL encourages!)
- **E2E Test**: Tests the entire app in a real browser (e.g., Cypress, Playwright). Slow, expensive.

### 🎯 Real-World Interview Example
> *“You’re starting a new React project. What testing strategy do you recommend?”*

✅ **Answer**:
> “I focus on **integration tests** using React Testing Library (RTL). RTL forces you to test components the way a user interacts with them — by clicking buttons, filling forms, and reading text — not by testing internal state or implementation details. I supplement with unit tests for pure utility functions and E2E tests for critical user flows like login or checkout. The ‘Testing Trophy’ (by Kent C. Dodds) is my guide: lots of integration tests, fewer unit and E2E tests.”

![Testing Trophy](https://testingjavascript.com/static/c34f0d5a7d1f6a7f8e8d4e4d4e4d4e4d/5a190/testing-trophy.png)  
*(Image: More integration tests, fewer unit/E2E)*

> 💬 **Interview Tip**: “Avoid testing implementation details like `useState` or internal function names. Test what the user sees and does. RTL’s guiding principle: ‘The more your tests resemble the way your software is used, the more confidence they can give you.’”

---

## ⚙️ 2. Setting Up Jest & React Testing Library (RTL)

### 💡 Concept
- **Jest**: Test runner and assertion library.
- **React Testing Library (RTL)**: Utilities to test React components *as a user would*.

### 🎯 Setup (Usually pre-configured in CRA or Vite)

```bash
npm install --save-dev jest @testing-library/react @testing-library/jest-dom
```

📁 `setupTests.js` (Optional — for global setup)
```js
// Optional: Add custom matchers like `toBeInTheDocument`
import '@testing-library/jest-dom';
```

📁 `jest.config.js`
```js
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/setupTests.js'],
};
```

✅ You’re ready to write tests!

---

## 🔍 3. Testing Components: Render, Fire Events, Assert

### 💡 Concept
The RTL testing mantra: **Arrange → Act → Assert**

1.  **Render** the component (`render`).
2.  **Find** elements the user would see (`screen.getBy...`).
3.  **Fire** events the user would do (`fireEvent`, `userEvent`).
4.  **Assert** the outcome (`expect`).

### 🎯 Real-World Interview Example
> *“Test a Counter component that increments when a button is clicked.”*

📁 `Counter.jsx`
```jsx
import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(c => c + 1)}>
        Increment
      </button>
    </div>
  );
}

export default Counter;
```

📁 `Counter.test.js`
```jsx
import { render, screen, fireEvent } from '@testing-library/react';
import Counter from './Counter';

test('counter increments when button is clicked', () => {
  // 🧪 ARRANGE: Render the component
  render(<Counter />);

  // 🔍 ASSERT (Initial State): Check initial count
  expect(screen.getByText('Count: 0')).toBeInTheDocument();

  // 🖱️ ACT: Find button and click it
  const button = screen.getByRole('button', { name: /increment/i });
  fireEvent.click(button);

  // ✅ ASSERT (Final State): Check count updated
  expect(screen.getByText('Count: 1')).toBeInTheDocument();
});
```

> 💬 **Interview Tip**: “I use `getByRole` with a `name` option because it’s the most accessible query — it mirrors how screen readers see the page. `fireEvent.click` simulates the user interaction. The test reads like a user story: ‘Given the counter is at 0, when I click the button, then the count should be 1.’”

---

## 🧩 4. Testing Custom Hooks with `@testing-library/react-hooks`

### 💡 Concept
Use `renderHook` to test custom hooks in isolation.

### 🎯 Real-World Interview Example
> *“Test the `useLocalStorage` hook you created earlier.”*

📁 `useLocalStorage.js`
```jsx
import { useState, useEffect } from 'react';

function useLocalStorage(key, initialValue) {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      return initialValue;
    }
  });

  useEffect(() => {
    try {
      window.localStorage.setItem(key, JSON.stringify(storedValue));
    } catch (error) {
      // Ignore write errors
    }
  }, [key, storedValue]);

  return [storedValue, setStoredValue];
}

export default useLocalStorage;
```

📁 `useLocalStorage.test.js`
```bash
npm install --save-dev @testing-library/react-hooks
```

```jsx
import { renderHook, act } from '@testing-library/react-hooks';
import useLocalStorage from './useLocalStorage';

// 🧪 Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn()
};
Object.defineProperty(window, 'localStorage', { value: localStorageMock });

test('should return initial value if nothing in localStorage', () => {
  localStorageMock.getItem.mockReturnValue(null);

  const { result } = renderHook(() => useLocalStorage('test-key', 'default'));

  expect(result.current[0]).toBe('default'); // [value, setValue]
});

test('should read from localStorage if value exists', () => {
  localStorageMock.getItem.mockReturnValue(JSON.stringify('stored-value'));

  const { result } = renderHook(() => useLocalStorage('test-key', 'default'));

  expect(result.current[0]).toBe('stored-value');
});

test('should update localStorage when value changes', () => {
  localStorageMock.getItem.mockReturnValue(null);

  const { result } = renderHook(() => useLocalStorage('test-key', 'initial'));

  act(() => {
    result.current[1]('new-value'); // Call setValue
  });

  expect(localStorageMock.setItem).toHaveBeenCalledWith('test-key', JSON.stringify('new-value'));
});
```

> 💬 **Interview Tip**: “I use `renderHook` to mount the hook. `act` wraps state updates to ensure they’re flushed. Always mock `localStorage` — you don’t want tests affecting each other or the real browser storage!”

---

## 🌀 5. Mocking: API Calls, Child Components, Modules

### 💡 Concept
Replace real dependencies with controlled, predictable versions.

### 🎯 Real-World Interview Example
> *“Test a component that fetches user data on mount. Mock the API call.”*

📁 `UserList.jsx`
```jsx
import { useState, useEffect } from 'react';

function UserList() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/users')
      .then(res => res.json())
      .then(data => {
        setUsers(data);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <ul>
      {users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}

export default UserList;
```

📁 `UserList.test.js`
```jsx
import { render, screen } from '@testing-library/react';
import UserList from './UserList';

// 🧪 Mock the global fetch
global.fetch = jest.fn();

test('displays list of users after fetching', async () => {
  const mockUsers = [
    { id: 1, name: 'Alice' },
    { id: 2, name: 'Bob' },
  ];

  // ✅ Mock the API response
  fetch.mockResolvedValueOnce({
    json: async () => mockUsers,
  });

  render(<UserList />);

  // 🕒 Wait for loading to finish
  const userItems = await screen.findAllByRole('listitem');
  expect(userItems).toHaveLength(2);
  expect(userItems[0]).toHaveTextContent('Alice');
  expect(userItems[1]).toHaveTextContent('Bob');

  // ✅ Verify fetch was called correctly
  expect(fetch).toHaveBeenCalledWith('/api/users');
});

// 🧹 Cleanup after each test
afterEach(() => {
  fetch.mockClear();
});
```

> 💬 **Interview Tip**: “I mock API calls to avoid hitting real servers in tests. `jest.fn()` and `mockResolvedValue` are perfect for this. Use `findBy...` queries (which return promises) when waiting for async operations like data fetching.”

---

## ✅ 6. Best Practices: What to Test, What Not to Test

### 💡 The Golden Rules

| Test This ✅                          | Don’t Test This 🚫                     |
|---------------------------------------|----------------------------------------|
| User-visible text & elements          | Internal state variable names          |
| Behavior after user interaction       | Implementation of `useState`/`useEffect`|
| Component renders without crashing    | Third-party library internals          |
| Correct props passed to child         | CSS styles (unless critical to function)|
| API calls made with correct params    | Console logs or trivial utils          |

### 🎯 Real-World Interview Example
> *“You have a `<Button>` component. What would you test?”*

✅ **Good Tests:**
- Renders with correct text.
- Calls `onClick` handler when clicked.
- Is disabled when `disabled` prop is true.
- Has correct accessible role (“button”).

🚫 **Bad Tests:**
- Tests that `useState` was called.
- Tests the internal CSS class name.
- Tests the exact order of hooks.

```jsx
// Button.test.js - GOOD EXAMPLE
test('calls onClick when clicked', () => {
  const handleClick = jest.fn();
  render(<Button onClick={handleClick}>Click Me</Button>);

  fireEvent.click(screen.getByRole('button', { name: /click me/i }));
  expect(handleClick).toHaveBeenCalledTimes(1);
});

test('is disabled when disabled prop is true', () => {
  render(<Button disabled>Click Me</Button>);

  const button = screen.getByRole('button', { name: /click me/i });
  expect(button).toBeDisabled();
});
```

> 💬 **Interview Answer**:  
> “I test what the user experiences: what they see, what they can click, and what happens when they interact. I avoid testing React’s internal mechanisms or implementation details that can change without affecting the user. This makes tests more resilient to refactoring and truly validate the user experience.”

