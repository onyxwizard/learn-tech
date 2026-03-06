/**
 * @author onyxwizard
 * @date 06-03-2026
 */

import express from 'express';

const app = express();
const PORT = 5000;

// Middleware
app.use(express.json());

// logging middleware
app.use((req, res, next) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
  next();
});


let tasks = [
  { id: 1, title: 'python', completed: true },
  { id: 2, title: 'Master Express', completed: false },
  { id: 2, title: 'Node', completed: false },
  { id: 2, title: 'mongo', completed: true },
  { id: 2, title: 'sql', completed: false },
  { id: 2, title: 'javascript', completed: true },
  { id: 2, title: 'java', completed: false }
]


// GET /tasks with optional filtering
app.get('/tasks', (req, res) => {
  let filteredTasks = [...tasks]; // Start with all tasks
  
  // Filter by completed status (if provided)
  if (req.query.completed !== undefined) {
    const isCompleted = req.query.completed === 'true'; // Convert string to boolean
    filteredTasks = filteredTasks.filter(task => task.completed === isCompleted);
  }
  
  // Filter by title search (case-insensitive partial match)
  if (req.query.title) {
    const searchTerm = req.query.title.toLowerCase();
    filteredTasks = filteredTasks.filter(task => 
      task.title.toLowerCase().includes(searchTerm)
    );
  }
  
  res.json({ 
    success: true, 
    count: filteredTasks.length, 
    data: filteredTasks 
  });
});


// Get All Tasks
app.get('/tasks', (req, res) => {
  res.json({message : 'All Task List',success: true, count: tasks.length, data:tasks});
});

// Get Single Task By ID
app.get('/tasks/:id', (req, res) => {
  const id  = req.params.id;
  
  if (!id) { 
    return res.status(400).json('ID - field is missing');
  }
  const taskItem = tasks.filter((task) => task.id == id);

  if (taskItem.length === 0) {
    return res.status(400).json('ID - Not in range');
  }
  res.status(200).json(taskItem[0]);
});


// Create Task
app.post('/tasks', (req, res) => {
  const { title, completed } = req.body;
  
  if (!title) {
    return res.json('Title Cannot be Empty');
  }

  // Get the Last Index - ID
  const id = tasks.length > 0 ? tasks[tasks.length - 1].id + 1 : 1;
  
  const newTask = {
    id,
    title,
    completed: completed ?? false
  }

  tasks.push(newTask);

  res.json({ message: 'Task Added', success: true, count: tasks.length, data: tasks });
});


// PUT - Update task
app.put('/tasks/:id', (req, res) => {
  const taskId = parseInt(req.params.id);
  const { title, completed } = req.body;
  
  const taskIndex = tasks.findIndex(t => t.id === taskId);
  
  if (taskIndex === -1) {
    return res.status(404).json({ success: false, error: 'Task not found' });
  }
  
  // Update task
  if (title !== undefined) tasks[taskIndex].title = title;
  if (completed !== undefined) tasks[taskIndex].completed = completed;
  tasks[taskIndex].updatedAt = new Date();
  
  res.json({ success: true, data: tasks[taskIndex] });
});

// DELETE - Remove task
app.delete('/tasks/:id', (req, res) => {
  const taskId = parseInt(req.params.id);
  
  const taskIndex = tasks.findIndex(t => t.id === taskId);
  
  if (taskIndex === -1) {
    return res.status(404).json({ success: false, error: 'Task not found' });
  }
  
  const deletedTask = tasks.splice(taskIndex, 1)[0];
  
  res.json({ success: true, message: 'Task deleted', data: deletedTask });
});


app.use((req, res) => {
  res.json('Page Not found');
})

// Server Listen
app.listen(PORT, () => {
  console.log('Task - Server Started');
});