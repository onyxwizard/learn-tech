import { Link } from 'react-router-dom';
import React from 'react';
const NotFound = () => {
  return (
    <div style={{ padding: '2rem', textAlign: 'center' }}>
      <h1>404 - Page Not Found</h1>
      <p>Sorry, the page you are looking for does not exist.</p>
      <Link to="/">Go to Home</Link>
    </div>
  );
};

export default NotFound;
