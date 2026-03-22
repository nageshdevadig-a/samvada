import './App.css';
import { Navigate, Route, Routes, useNavigate } from 'react-router';
import { lazy, Suspense, useEffect, useState, useCallback, use } from 'react';
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

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
};

function App() {

  const navigate = useNavigate();

  const [user, setUser] = useState(() => {
    const sessionUser = sessionStorage.getItem('samvada_user');
    return sessionUser ? JSON.parse(sessionUser) : null;
  });


  const [isInitializing, setIsInitializing] = useState(true);


  const verifySession = useCallback(async () => {
    try {
      const res = await api.get("/v1/users/me");
      console.log("Session verified", res.data);

      sessionStorage.setItem('samvada_user', JSON.stringify(res.data));
      setUser(res.data);
      if(window.location.pathname === "/") {
        navigate("/", { replace: true });
      }
    } catch (error) {
      console.error("Error fetching user info:", error.response ? error.response.data.detail : error);
      sessionStorage.removeItem('samvada_user');
      document.cookie = "samvada_logged_in=; Max-Age=0; path=/; SameSite=Lax";
      setUser(null);
    }
  }, [navigate]);

  useEffect(() => {
    const initAuth = async () => {
      const hasSignal = getCookie("samvada_logged_in");
      if (hasSignal && !user) {
        await verifySession()
      }
      else if(!hasSignal) {
        sessionStorage.removeItem('samvada_user');
        document.cookie = "samvada_logged_in=; Max-Age=0; path=/; SameSite=Lax";
        setUser(null);
      }
      setIsInitializing(false);
    };
    initAuth();
  }, [verifySession,user]);



  if (isInitializing) {
    return <SplashScreen />;
  }

  return (
    <Suspense fallback={<SplashScreen />}>
      <Routes>
        {/* Public Routes: Only show if NOT logged in */}
        {!user ? (
          <>
            <Route path="/" element={<LoginHome onLoginSuccess={verifySession} />} />
            <Route path="/signup" element={<CreateAccount onAccountCreated={verifySession} />} />
            {/* Fallback for guests: redirect any unknown path to Login */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </>
        ) : (
          <>
            {/* Private Routes: Only show if logged in */}
            <Route path="/" element={<DashBoard />} >
              <Route path="room/:roomId" element={<DashBoard />} />
              <Route path="search-user" element={<DashBoard />} />
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
