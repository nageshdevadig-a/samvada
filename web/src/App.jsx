import './App.css';
import { Navigate, Route, Routes, useNavigate } from 'react-router';
import { lazy, Suspense, useEffect, useState, useCallback } from 'react';
import api from './api/axios';
import LoginHome from './pages/LoginHome';
import CreateAccount from './pages/CreateAccount';
import Logout from './components/Logout';

const DashBoard = lazy(() => import('./pages/DashBoard'));

const SplashScreen = () => (
  <div className="h-screen bg-[#0a0a0a] flex items-center justify-center">
    <div className="animate-pulse text-[#605ee8] font-bold tracking-widest">SAMVADA</div>
  </div>
);

function App() {

  const navigate = useNavigate();

  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('samvada_user');
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [isInitializing, setIsInitializing] = useState(true);


  const setUserInfo = useCallback(async () => {
    try {
      const res = await api.get("/v1/users/me");
      console.log("User res", res);

      localStorage.setItem('samvada_user', JSON.stringify(res.data));
      setUser(res.data);
      navigate("/", { replace: true });
    } catch (error) {
      console.error("Error fetching user info:", error.response ? error.response.data.detail : error);
      localStorage.removeItem('samvada_user');
      setUser(null);
    }
  }, []);

  useEffect(() => {
    const initAuth = async () => {
      const hint = localStorage.getItem('samvada_user');
      if (hint) {
        await setUserInfo()
      }
      setIsInitializing(false);
    };
    initAuth();
  }, [setUserInfo]);



  if (isInitializing) {
    return <SplashScreen />;
  }

  return (
    <Suspense fallback={<SplashScreen />}>
      <Routes>
        {/* Public Routes: Only show if NOT logged in */}
        {!user ? (
          <>
            <Route path="/" element={<LoginHome onLoginSuccess={setUserInfo} />} />
            <Route path="/signup" element={<CreateAccount onAccountCreated={setUserInfo} />} />
            {/* Fallback for guests: redirect any unknown path to Login */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </>
        ) : (
          <>
            {/* Private Routes: Only show if logged in */}
            <Route path="/" element={<DashBoard />} >
              <Route path="room/:roomId" element={<DashBoard />} />
            </Route>
            <Route path="/logout" element={<Logout />} />
            {/* Fallback for logged-in users: redirect unknown to Dashboard */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </>
        )}
      </Routes>
    </Suspense>
  );
}

export default App;
