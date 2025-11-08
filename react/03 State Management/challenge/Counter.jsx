export function Counter({key, count, onIncrement, onDecrement }) {
  return (
    <div style={{ margin: '10px', padding: '10px', border: '1px solid #ccc' }}>
      <h3>Counter { key }</h3>
      <p>Count: {count}</p>
      <button onClick={onIncrement}>➕</button>
      <button onClick={onDecrement}>➖</button>
    </div>
  );
}