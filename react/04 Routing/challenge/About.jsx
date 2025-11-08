import { useNavigate } from 'react-router-dom'; // ✅ Import the hook

const About = () => {
  const navigate = useNavigate(); // ✅ Initialize hook

  const goHome = () => {
    navigate('/home'); // ✅ Programmatically navigate
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>📝 About Page</h2>
      <p>This is the about section.</p>
      <button 
        onClick={goHome}
        style={{ 
          padding: '10px 20px', 
          backgroundColor: '#007bff', 
          color: 'white', 
          border: 'none', 
          borderRadius: '5px',
          cursor: 'pointer'
        }}
      >
        🏠 Go to Home (Programmatic Navigation)
      </button>
    </div>
  );
};

export default About;