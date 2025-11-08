import { useState } from 'react';
import { Counter } from './Counter';

export function Parent() {
  // ✅ Lift state up: Parent manages state for ALL counters
  const [counters, setCounters] = useState([
    { id: 1, count: 0 },
    { id: 2, count: 0 },
    { id: 3, count: 0 },
  ]);

  // 🔁 Update a specific counter
  const updateCounter = (id, delta) => {
    setCounters(prev =>
      prev.map(counter =>
        counter.id === id ? { ...counter, count: counter.count + delta } : counter
      )
    );
  };

  // 🧹 Reset ALL counters to zero
  const resetAll = () => {
    setCounters(prev =>
      prev.map(counter => ({ ...counter, count: 0 }))
    );
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Parent-Controlled Counters</h2>
      <button 
        onClick={resetAll}
        style={{ padding: '10px 20px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '5px', marginBottom: '20px' }}
      >
        🔄 Reset All Counters
      </button>

      {counters.map(counter => (
        <Counter
          key={counter.id}
          count={counter.count}
          onIncrement={() => updateCounter(counter.id, 1)}
          onDecrement={() => updateCounter(counter.id, -1)}
        />
      ))}
    </div>
  );
}