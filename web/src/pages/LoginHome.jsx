import Footer from "../components/Footer"
import MainLeft from "../components/MainLeft"
import LoginForm from "../components/LoginForm"

function LoginHome({onLoginSuccess}) {
    return (
        <div className="flex flex-col min-h-screen bg-black text-white font-sans">
            {/* Main Container */}
            <div className="flex flex-1 flex-col md:flex-row">
                <MainLeft />
                <LoginForm onLoginSuccess={onLoginSuccess} />
            </div>
            <Footer />
        </div>
    )
}

export default LoginHome;