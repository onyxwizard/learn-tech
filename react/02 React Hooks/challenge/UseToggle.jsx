import { useState } from 'react';

const useToggle = ()=> {
  const [isDark, setIsDark] = useState(false);

  const toggleTheme = () => {
    setIsDark(prev => {
      const newValue = !prev;
      console.log(`Theme: ${newValue ? 'DARK' : 'LIGHT'}`);
      return newValue;
    });
  };

  const bgColor = isDark ? 'black' : 'white';
  const textColor = isDark ? 'white' : 'black';

  return { bgColor, textColor, toggleTheme, isDark };
}

export { useToggle };