import React, { useState } from "react";
import { Link } from "react-router";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema } from "../schemas/loginSchema";
import { login } from "../services/authService";
import { CircleAlert } from "lucide-react";

const LoginForm = ({ onLoginSuccess }) => {

    const [apiError, setApiError] = useState("");

    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({
        resolver: zodResolver(loginSchema),
        mode: "onChange"
    });

    const onSubmit = async (data) => {
        try {

            const response = await login(data);
            console.log("Login successful", response);
            if (response.status === 200) {
                onLoginSuccess();
            }
        }
        catch (error) {
            if (error.response) {
                const backendError = error.response.data;
                console.error("Backend error:", backendError);

                setApiError(backendError.detail || "An unexpected error occurred");
            }
            else if (error.request) {
                setApiError("Server is unreachable. Please check your internet or try again later.");
            }
            else {
                setApiError("Error: " + error.detail);
            }
        }

    };

    return (
        <div className="flex-1 flex flex-col items-center justify-center p-6 lg:p-12 bg-[#0a0a0a]">
            <div className="w-full max-w-[350px] space-y-6">
                <span className="text-xs font-bold tracking-widest text-white md:hidden">SAMVADA</span>
                <h2 className="text-xl font-semibold mb-8 mt-10">Log into Samvada</h2>
                {apiError && (
                    <div className="flex items-center gap-3 w-full p-4 bg-red-500/10 border border-red-500/30 rounded-lg animate-in fade-in zoom-in duration-200">
                        <CircleAlert className="w-5 h-5 text-red-500 shrink-0" />
                        <h6 className="text-red-500 text-sm font-medium leading-tight">{apiError}</h6>
                    </div>)}

                <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
                    {/* Input Group */}
                    <div className="space-y-3">
                        <div className="relative">
                            <input
                                type="text"
                                name="usernameOrEmail"
                                {...register("usernameOrEmail")}
                                onFocus={()=>setApiError("")}
                                placeholder="Username or email"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)]  outline-none transition-all placeholder-gray-500"
                            />
                            {errors.usernameOrEmail && (
                                <span className="text-xs text-red-400">{errors.usernameOrEmail.message}</span>)}
                        </div>
                        <div className="relative">
                            <input
                                type="password"
                                name="password"
                                {...register("password")}
                                onFocus={()=>setApiError("")}
                                placeholder="Password"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)] outline-none transition-all placeholder-gray-500"
                            />
                            {errors.password && (
                                <span className="text-xs text-red-400">{errors.password.message}</span>)}
                        </div>
                    </div>

                    <button type="submit" className="w-full bg-[rgb(96,94,232)] hover:bg-[rgb(85,83,226)] active:bg-[rgb(85,83,226)] text-white font-semibold py-2 rounded-lg transition-colors text-sm cursor-pointer">
                        {isSubmitting ? "Verifying..." : "Login"}
                    </button>
                </form>

                <div className="text-center">
                    <a href="#" className="text-xs text-gray-400 hover:underline">Forgot password?</a>
                </div>

                <div className="pt-8 space-y-4">
                    <Link to="/signup" className="flex items-center justify-center border border-gray-800 py-2 rounded-lg text-sm font-semibold hover:bg-[rgb(85,83,226)] active:bg-[rgb(85,83,226)] transition-all cursor-pointer">
                        Create new account
                    </Link>
                </div>
                <div className="flex justify-center pt-8 opacity-60">
                    <span className="text-xs font-bold tracking-widest text-gray-400">UPARYA</span>
                </div>
            </div>
        </div>
    )
}

export default LoginForm;