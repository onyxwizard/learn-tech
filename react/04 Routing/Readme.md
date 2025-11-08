
# 📘 React Quick Notes — Chapter 4: Routing with React Router v6 🗺️  
> *“Navigate like a pro — from basic routes to protected layouts.”*


## 🗂️ Table of Contents

1.  [🚀 Setup & Basic Routes](#-1-setup--basic-routes)
2.  [🔗 Navigation: `<Link>` vs `useNavigate`](#-2-navigation--link-vs-usenavigate)
3.  [🔢 Dynamic Routing & URL Parameters](#-3-dynamic-routing--url-parameters)
4.  [🧭 Nested Routes & Layouts](#-4-nested-routes--layouts)
5.  [🔐 Protected Routes & Authentication](#-5-protected-routes--authentication)
6.  [🔀 Programmatic Navigation & State](#-6-programmatic-navigation--state)
7.  [🧩 404 & Wildcard Routes](#-7-404--wildcard-routes)



## 🚀 1. Setup & Basic Routes

### 💡 Concept
React Router lets you declaratively map URLs to components. `createBrowserRouter` is the modern, recommended way.

### 🎯 Real-World Interview Example
> *“Set up a basic app with Home, About, and Contact pages.”*

```bash
npm install react-router-dom
```

```jsx
// App.jsx
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

// Components
function Home() {
  return <h1>🏠 Home Page</h1>;
}

function About() {
  return <h1>ℹ️ About Us</h1>;
}

function Contact() {
  return <h1>📞 Contact Page</h1>;
}

// 1. Define Routes
const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/about",
    element: <About />,
  },
  {
    path: "/contact",
    element: <Contact />,
  },
]);

// 2. Provide Router to App
function App() {
  return <RouterProvider router={router} />;
}

export default App;
```

> 💬 **Interview Tip**: “I use `createBrowserRouter` because it’s data-router compatible and supports future features like data loading. The older `BrowserRouter` with `Routes` and `Route` still works, but the new API is the future.”

---

## 🔗 2. Navigation: `<Link>` vs `useNavigate`

### 💡 Concept
- `<Link>`: Declarative navigation (like an `<a>` tag, but doesn’t refresh the page).
- `useNavigate`: Imperative navigation (for redirects after form submission, etc.).

### 🎯 Real-World Interview Example
> *“Create a navbar with Links. After submitting a contact form, redirect to the Home page.”*

```jsx
import { Link, useNavigate } from 'react-router-dom';

// Navbar Component
function Navbar() {
  return (
    <nav>
      <Link to="/">🏠 Home</Link> | 
      <Link to="/about">ℹ️ About</Link> | 
      <Link to="/contact">📞 Contact</Link>
    </nav>
  );
}

// Contact Form Component
function ContactForm() {
  const navigate = useNavigate(); // 🧭 Hook for programmatic nav

  const handleSubmit = (e) => {
    e.preventDefault();
    // Simulate form submission
    console.log("Form submitted!");
    alert("Thank you for your message!");
    navigate("/"); // ✅ Redirect to Home after submit
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="text" placeholder="Your Name" required />
      <input type="email" placeholder="Your Email" required />
      <textarea placeholder="Your Message"></textarea>
      <button type="submit">Send Message ✉️</button>
    </form>
  );
}

// Update your route config
const router = createBrowserRouter([
  { path: "/", element: <><Navbar /><Home /></> },
  { path: "/about", element: <><Navbar /><About /></> },
  { path: "/contact", element: <><Navbar /><ContactForm /></> }, // Use form here
]);
```

> 💬 **Interview Tip**: “I use `<Link>` for user-initiated navigation in the UI (buttons, menus). I use `useNavigate` for redirects after async operations like form submissions, login, or API calls.”

---

## 🔢 3. Dynamic Routing & URL Parameters

### 💡 Concept
Define routes with parameters (e.g., `/users/:id`) and access them with `useParams`.

### 🎯 Real-World Interview Example
> *“Create a user profile page that displays info based on the user ID in the URL.”*

```jsx
import { useParams } from 'react-router-dom';

// Mock user data
const users = {
  1: { name: "Alice", email: "alice@example.com" },
  2: { name: "Bob", email: "bob@example.com" },
  3: { name: "Carol", email: "carol@example.com" },
};

// User Profile Component
function UserProfile() {
  const { id } = useParams(); // ✅ Get URL parameter
  const user = users[id];

  if (!user) {
    return <div>User not found! ❌</div>;
  }

  return (
    <div>
      <h2>👤 Profile: {user.name}</h2>
      <p>Email: {user.email}</p>
    </div>
  );
}

// Update route config
const router = createBrowserRouter([
  // ... other routes
  {
    path: "/user/:id", // 🎯 Dynamic segment
    element: <><Navbar /><UserProfile /></>,
  },
]);

// In your UI, create links to profiles
function UserList() {
  return (
    <div>
      <h2>👥 Users</h2>
      {Object.entries(users).map(([id, user]) => (
        <div key={id}>
          <Link to={`/user/${id}`}>{user.name}</Link>
        </div>
      ))}
    </div>
  );
}
```

> 💬 **Interview Tip**: “Dynamic routes are essential for detail pages. `useParams` is the hook to extract the values. Always validate the parameter — users can type anything into the URL!”

---

## 🧭 4. Nested Routes & Layouts

### 💡 Concept
Organize routes hierarchically. Child routes render inside their parent’s `<Outlet />`. Perfect for layouts (e.g., dashboard with sidebar).

### 🎯 Real-World Interview Example
> *“Create a Dashboard layout with a sidebar. Dashboard has nested routes: Overview and Settings.”*

```jsx
import { Outlet } from 'react-router-dom';

// Dashboard Layout Component
function DashboardLayout() {
  return (
    <div style={{ display: 'flex' }}>
      {/* Sidebar */}
      <aside style={{ width: '200px', background: '#f0f0f0', padding: '20px' }}>
        <h3>📊 Dashboard</h3>
        <nav>
          <Link to="overview">📈 Overview</Link><br />
          <Link to="settings">⚙️ Settings</Link>
        </nav>
      </aside>
      {/* Main Content Area - Child routes render here */}
      <main style={{ flex: 1, padding: '20px' }}>
        <Outlet /> {/* 🎯 This is where child routes render */}
      </main>
    </div>
  );
}

// Child Components
function DashboardOverview() {
  return <h2>📈 Welcome to your Dashboard Overview!</h2>;
}

function DashboardSettings() {
  return <h2>⚙️ Manage your settings here.</h2>;
}

// Update route config with nested children
const router = createBrowserRouter([
  // ... other routes
  {
    path: "/dashboard",
    element: <DashboardLayout />,
    children: [ // ✅ Nested routes
      {
        index: true, // Default child route
        element: <DashboardOverview />,
      },
      {
        path: "overview", // Full path: /dashboard/overview
        element: <DashboardOverview />,
      },
      {
        path: "settings", // Full path: /dashboard/settings
        element: <DashboardSettings />,
      },
    ],
  },
]);
```

> 💬 **Interview Tip**: “Nested routes are a game-changer for complex UIs. The parent layout component renders the `<Outlet />` where children appear. The `index` route is the default when you land on the parent path.”

---

## 🔐 5. Protected Routes & Authentication

### 💡 Concept
Prevent unauthorized users from accessing certain routes. Redirect them to login.

### 🎯 Real-World Interview Example
> *“Protect the Dashboard route. Only show it if the user is logged in. Redirect to Login if not.”*

```jsx
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './path-to-your-auth-hook'; // Assume you have this

// Protected Route Wrapper Component
function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <div>Loading... ⏳</div>;
  }

  if (!user) {
    // Redirect to login, but save the attempted URL
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}

// Login Component
function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const handleLogin = () => {
    login({ name: "Demo User", id: 123 }); // Simulate login
    // Redirect back to where user tried to go, or to dashboard
    const from = location.state?.from?.pathname || "/dashboard";
    navigate(from, { replace: true });
  };

  return (
    <div>
      <h2>🔐 Please Log In</h2>
      <button onClick={handleLogin}>Log In as Demo User</button>
    </div>
  );
}

// Update route config
const router = createBrowserRouter([
  // ... public routes (Home, About, Contact, Login)
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/dashboard",
    element: (
      <ProtectedRoute>
        <DashboardLayout />
      </ProtectedRoute>
    ),
    children: [
      { index: true, element: <DashboardOverview /> },
      { path: "overview", element: <DashboardOverview /> },
      { path: "settings", element: <DashboardSettings /> },
    ],
  },
]);
```

> 💬 **Interview Tip**: “I create a `ProtectedRoute` component that checks auth status. If not authenticated, I use `Navigate` to send them to login, and I pass the `location` in state so I can redirect them back after login. Always handle the loading state to avoid flickering.”

---

## 🔄 6. Programmatic Navigation & State

### 💡 Concept
Use `useNavigate` to change routes imperatively and optionally pass state along.

### 🎯 Real-World Interview Example
> *“After a user logs in, redirect them to the page they originally tried to access, and show a welcome message.”*

```jsx
// In Login.js (from previous example)
function Login() {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogin = () => {
    // Simulate API call
    setTimeout(() => {
      // Pass state to the next route
      const from = location.state?.from?.pathname || "/dashboard";
      navigate(from, { 
        replace: true,
        state: { welcomeMessage: "Welcome back! 🎉" } // ✅ Pass state
      });
    }, 1000);
  };

  return (
    <div>
      <h2>🔐 Please Log In</h2>
      <button onClick={handleLogin}>Log In</button>
    </div>
  );
}

// In DashboardOverview.js
import { useLocation } from 'react-router-dom';

function DashboardOverview() {
  const location = useLocation();
  const welcomeMessage = location.state?.welcomeMessage;

  return (
    <div>
      {welcomeMessage && (
        <div style={{ background: '#d4edda', padding: '10px', marginBottom: '20px' }}>
          {welcomeMessage}
        </div>
      )}
      <h2>📈 Welcome to your Dashboard Overview!</h2>
    </div>
  );
}
```

> 💬 **Interview Tip**: “You can pass serializable data (objects, strings, numbers) via the `state` property in `navigate`. Access it with `useLocation().state`. This is great for flash messages or passing temporary context.”

---

## 🧩 7. 404 & Wildcard Routes

### 💡 Concept
Catch-all route for undefined URLs. Essential for good UX.

### 🎯 Real-World Interview Example
> *“Create a 404 page and show it for any unknown route.”*

```jsx
// 404 Component
function NotFound() {
  const navigate = useNavigate();

  return (
    <div style={{ textAlign: 'center', padding: '50px' }}>
      <h1>404 - Page Not Found! 🚫</h1>
      <p>Sorry, the page you're looking for doesn't exist.</p>
      <button onClick={() => navigate(-1)}>Go Back ◀️</button> | 
      <button onClick={() => navigate('/')}>Go Home 🏠</button>
    </div>
  );
}

// Add this as the VERY LAST route
const router = createBrowserRouter([
  // ... all your other routes
  {
    path: "*", // 🎯 Wildcard - matches everything else
    element: <NotFound />,
  },
]);
```

> 💬 **Interview Tip**: “The wildcard route `*` must be the last route in your config. Order matters in React Router — it picks the first match. This is your safety net for broken links or typos.”
