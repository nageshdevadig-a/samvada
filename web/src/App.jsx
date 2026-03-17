import './App.css';
import { Route, Routes } from 'react-router';
import LoginHome from './pages/LoginHome';
import CreateAccount from './pages/CreateAccount';


function App() {


  return (
    <Routes>
      <Route path="/" element={<LoginHome />} />
      <Route path="/signup" element={<CreateAccount />} />
    </Routes>
  )
}

export default App
