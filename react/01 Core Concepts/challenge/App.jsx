import { useState } from 'react';

export function App() {
  const [showHello, setShowHello] = useState(true); // ✅ Descriptive name

  const toggleMessage = () => {
    setShowHello(prev => !prev); // ✅ Functional update (best practice)
  };

  return (
    <>
      <div style={{ backgroundColor: "pink", padding: "20px", margin: "20px" }}>
        <h2>{showHello ? "Hello, World!" : "Goodbye, World!"}</h2> {/* ✅ Full message */}
      </div>
      <button onClick={toggleMessage}> {/* ✅ Direct function reference */}
        Toggle Message 🔄
      </button>
    </>
  );
}