import express from 'express';

// Instanciate express
const app = express();
const PORT = 5000;

// Middleware: Parse JSON bodies
app.use(express.json());

// Middleware: Log all requests (like a traffic monitor)
app.use((req, res, next) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
  next();
});

// Root route
app.get('/', (req, res) => {
  res.json({
    message: 'Welcome to TaskVerse! 🎮',
    level: '2 - Express Highway',
    status: 'Running'
  });
});

// About route
app.get('/about', (req, res) => {
  res.json({
    name: 'TaskVerse',
    description: 'Social Task Management Platform',
    tech: 'Node.js + Express',
    level: 2
  });
});

// Status route
app.get('/status', (req, res) => {
  res.json({
    server: 'Running ✅',
    level: '2 - Express Highway',
    next: 'Database Dungeon',
    timestamp: new Date().toISOString()
  });
});


// Listen
app.listen(PORT, () => {
  console.log(`🚀 Express server running on http://localhost:${PORT}`);
  console.log(`🎯 LEVEL 2 ACTIVE - Express Highway`);
})


