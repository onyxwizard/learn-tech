import mongoose from "mongoose";

const taskSchema = new mongoose.Schema({
  title: {
    type: String,
    required: [true, 'Task Title is Required'],
    trim: true,
    minlength:[3,'Title must be atleast 3 Char long']
  },
  completed: {
    type: Boolean,
    default:false
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  updatedAt: {
    type: Date
  }
}, {
  timestamps:true
});

// Create compound index for better query performance
taskSchema.index({ completed: 1, createdAt: -1 });

const Task = mongoose.model('Task', taskSchema);
export default Task;
