import {logout} from "../services/authService";

const Logout = () => {

    const logoutHandler = () => {
            logout().then(() => {
                sessionStorage.removeItem('samvada_user');
                document.cookie = "samvada_logged_in=; Path=/; Max-Age=0; SameSite=Lax; Secure";
                window.location.href = "/"; // Redirect to login page after logout
            }).catch((error) => {
                console.error("Logout failed:", error.response ? error.response.data.detail : error);
                alert("Logout failed. Please try again.");
            });
    };


    return (
         <button  className="w-full bg-[rgb(96,94,232)] hover:bg-[rgb(85,83,226)] active:bg-[rgb(85,83,226)] text-white font-semibold py-2 rounded-lg transition-colors text-sm cursor-pointer" onClick={logoutHandler}>
                        Log Out
                    </button>
    )
}

export default Logout;