/**
 * @author onyxwizard
 * @date 07-03-2026
 */

import express from 'express';
import mongoose from "mongoose";
import Task from './models/Task.js';

// MongoDB Connection
const connectDB = async () => {
  try {
    await mongoose.connect('mongodb://127.0.0.1:27017/taskverse');
    console.log('MongoDB Connected!');
  } catch (err) {
    console.log('MongoDB Connection Failed', err.message);
    process.exit(1);

  }
};

// Connect DB
await connectDB();

// Seed initial tasks (run once)
const seedTasks = async () => {
  try {
    // Only seed if collection is empty
    const count = await Task.countDocuments();
    if (count === 0) {
      await Task.insertMany([
        { title: 'Complete Level 1 - The Foundation', completed: true },
        { title: 'Master Express Highway', completed: true },
        { title: 'Conquer Database Dungeon', completed: false },
        { title: 'Defeat Authentication Arena boss', completed: false }
      ]);
      console.log('✅ Database seeded with initial tasks');
    }
  } catch (err) {
    console.error('Seeding error:', err);
  }
};

await seedTasks();


const app = express();
const PORT = 5000;

// Middle ware
app.use(express.json());



// GET all tasks with filtering
app.get('/tasks', async (req, res) => {
  try {
    const filter = {};

    // Filter by completed status
    if (req.query.completed !== undefined) {
      filter.completed = req.query.completed === 'true';
    }

    // Filter by title (case-insensitive partial match)
    if (req.query.title) {
      filter.title = { $regex: req.query.title, $options: 'i' };
    }

    // Execute query with sorting
    const tasks = await Task.find(filter).sort({ createdAt: -1 });

    res.json({
      success: true,
      count: tasks.length,
      tasks
    });
  } catch (err) {
    console.error('GET /tasks error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});


// GET /tasks/stats
app.get('/tasks/stats', async (req, res) => {
  try {
    const stats = await Task.aggregate([
      {
        $group: {
          _id: null,
          total: { $sum: 1 },
          completed: {
            $sum: {
              $cond: [{ $eq: ['$completed', true] }, 1, 0]
            }
          }
        }
      },
      {
        $project: {
          _id: 0,
          total: 1,
          completed: 1,
          incomplete: { $subtract: ['$total', '$completed'] },
          completionRate: {
            $concat: [
              {
                $toString: {
                  $round: [
                    { $multiply: [{ $divide: ['$completed', '$total'] }, 100] },
                    2
                  ]
                }
              },
              '%'
            ]
          }
        }
      }
    ]);

    if (stats.length === 0) {
      return res.json({
        success: true,
        data: { total: 0, completed: 0, incomplete: 0, completionRate: '0.00%' }
      });
    }

    res.json({ success: true, data: stats[0] });
  } catch (err) {
    console.error('Stats error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});

// GET single task by ID
app.get('/tasks/:id', async (req, res) => {
  try {
    const task = await Task.findById(req.params.id);

    if (!task) {
      return res.status(404).json({ success: false, error: 'Task not found' });
    }

    res.json({ success: true, task });
  } catch (err) {
    console.error('GET /tasks/:id error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});

// CREATE new task
app.post('/tasks', async (req, res) => {
  try {
    const { title, completed } = req.body;

    // Validation (Mongoose will also validate, but we check early)
    if (!title?.trim()) {
      return res.status(400).json({ success: false, error: 'Title is required' });
    }

    // Create and save task
    const task = await Task.create({
      title: title.trim(),
      completed: completed ?? false
    });

    res.status(201).json({ success: true, task });
  } catch (err) {
    // Handle validation errors specifically
    if (err.name === 'ValidationError') {
      const errors = Object.values(err.errors).map(e => e.message);
      return res.status(400).json({ success: false, errors });
    }

    console.error('POST /tasks error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});


// UPDATE task
app.put('/tasks/:id', async (req, res) => {
  try {
    const { title, completed } = req.body;
    const updateData = {};

    if (title !== undefined) updateData.title = title.trim();
    if (completed !== undefined) updateData.completed = completed;
    updateData.updatedAt = Date.now();

    const task = await Task.findByIdAndUpdate(
      req.params.id,
      updateData,
      {
        new: true,           // Return updated document
        runValidators: true  // Run schema validators
      }
    );

    if (!task) {
      return res.status(404).json({ success: false, error: 'Task not found' });
    }

    res.json({ success: true, task });
  } catch (err) {
    console.error('PUT /tasks/:id error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});

// DELETE task
app.delete('/tasks/:id', async (req, res) => {
  try {
    const task = await Task.findByIdAndDelete(req.params.id);

    if (!task) {
      return res.status(404).json({ success: false, error: 'Task not found' });
    }

    res.json({
      success: true,
      message: 'Task deleted successfully',
      task
    });
  } catch (err) {
    console.error('DELETE /tasks/:id error:', err);
    res.status(500).json({ success: false, error: 'Server error' });
  }
});




// Listener
app.listen(PORT, () => {
  console.log('Server Started');
});