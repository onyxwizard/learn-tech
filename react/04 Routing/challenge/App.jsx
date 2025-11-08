import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Home from './Home';
import About from './About';
import NotFound from './NotFound'; // Import your new component

const router = createBrowserRouter([
  {
    path: '/',
    element: <Home />,
  },
  {
    path: '/about',
    element: <About />,
  },
  // Catch-all route for any undefined path
  {
    path: '*',
    element: <NotFound />,
  },
]);

export default function App() {
  return <RouterProvider router={router} />;
}
