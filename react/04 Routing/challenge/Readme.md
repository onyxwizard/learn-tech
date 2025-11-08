## 🗺️ Chapter 4 Checkpoint: React Router v6 — Navigation & Programmatic Routing

> **🎯 Challenge (Interview Question Style):**  
> *“Create two routes: ‘/home’ and ‘/about’. Add a navbar with links to both. When the user is on ‘/about’, show a button that programmatically navigates back to ‘/home’ when clicked.”*

✅ **What I’m Testing:**
- Can you set up basic routes with `createBrowserRouter`?
- Can you use `<Link>` for declarative navigation?
- Can you use `useNavigate` for programmatic navigation?
- Do you understand how to structure a router config?



📝 **Your Turn!**  
Write the code for:
- Router setup (App.jsx or main.jsx)
- Home component
- About component (with the programmatic button)
- Navbar component

*(Starter template if you need it:)*

```jsx
// App.jsx
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  // Your routes here...
]);

export default function App() {
  return <RouterProvider router={router} />;
}
```



💡 **Hint**: Use `useNavigate` hook in the About component to go back to “/home”.

