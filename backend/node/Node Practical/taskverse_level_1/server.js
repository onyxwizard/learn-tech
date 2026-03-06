import http from 'http';

// Create HTTP Server
const server = http.createServer((req, res) => {
  // Set Response Header
  res.writeHead(200, { 'Content-Type': 'text/plain' });

  // Set Response URL
  if (req.url === '/') {
    res.end('Welcome to TaskVerse! \nLevel 1: The Foundation');
  } else if (req.url === '/about') {
    res.end('TaskVerse - Social Task Management Platform\nBuilt with Node.js');
  } else if (req.url === '/status') {
    res.write('TaskVerse Status:');
    res.write('\n- Server: Running ✅');
    res.write('\n- Level: 1 - The Foundation');
    res.write('\n- Next: Express Highway');
    res.end();
  }
  else {
    res.end('404 - Page Not Found');
  }
});

// Start Server Port
const PORT = 5000;
server.listen(PORT, () => { 
  console.log(`🚀 Server running on http://localhost:${PORT}`);
  console.log(`🎯 LEVEL 1 ACTIVE - The Foundation`);
});

