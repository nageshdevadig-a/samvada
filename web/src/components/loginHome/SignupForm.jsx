import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { signupSchema } from "../../schemas/signupSchema";
import { signup } from "../../services/authService";
import { Link } from "react-router";
import { CircleAlert } from "lucide-react";

const SignupForm = ({ onAccountCreated }) => {

    const [apiError, setApiError] = useState("");

    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({
        resolver: zodResolver(signupSchema),
        mode: "onChange"
    });

    const onSubmit = async (data) => {
        try {

            const response = await signup(data);
            console.log("Account Created", response);
            if (response.status === 201){onAccountCreated();}
            
        }
        catch (error) {
            // 1. Check if the server actually sent a response
            if (error.response) {
                // Access the JSON from your Global Exception Handler
                const backendError = error.response.data;
                console.error("Backend error:", backendError);

                // Use the specific message from your Spring Boot response
                setApiError(backendError.detail || "An unexpected error occurred");
            }
            // 2. Check if the request was made but no response received (like the SSL/Timeout issue)
            else if (error.request) {
                setApiError("Server is unreachable. Please check your internet or try again later.");
            }
            // 3. Everything else (setup errors)
            else {
                setApiError("Error: " + error.detail);
            }
        }

    };

    return (
        <div className="flex-1 flex flex-col items-center justify-center p-6 lg:p-12 bg-[#0a0a0a]">
            <div className="w-full max-w-[500px] space-y-6">
                <span className="text-xs font-bold tracking-widest text-white">SAMVADA</span>
                <h2 className="mt-10 text-3xl font-semibold mb-1 text-white">Get Started on Samvada</h2>
                <p className=" mb-8 text-white">Sign up to chat with your friends</p>
                {apiError && (
                    <div className="flex items-center gap-3 w-full p-4 bg-red-500/10 border border-red-500/30 rounded-lg animate-in fade-in zoom-in duration-200">
                        <CircleAlert className="w-5 h-5 text-red-500 shrink-0" />
                        <h6 className="text-red-500 text-sm font-medium leading-tight">{apiError}</h6>
                    </div>)}
                <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
                    {/* Input Group */}
                    <div className="space-y-3">
                        <div className="relative">
                            <label className="text-sm font-medium text-gray-300">Email</label>
                            <input
                                type="text"
                                name="email"
                                {...register("email")}
                                onFocus={() => setApiError("")}
                                placeholder="Email"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)]  outline-none transition-all placeholder-gray-500"
                            />
                            {errors.email && (
                                <span className="text-xs text-red-400">{errors.email.message}</span>)}
                        </div>
                        <div className="relative">
                            <label className="text-sm font-medium text-gray-300">Password</label>
                            <input
                                type="password"
                                name="password"
                                {...register("password")}
                                onFocus={() => setApiError("")}
                                placeholder="Password"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)] outline-none transition-all placeholder-gray-500"
                            />
                            {errors.password && (
                                <span className="text-xs text-red-400">{errors.password.message}</span>)}
                        </div>
                        <div className="relative">
                            <label className="text-sm font-medium text-gray-300">Full Name</label>
                            <input
                                type="text"
                                name="fullName"
                                {...register("fullName")}
                                onFocus={() => setApiError("")}
                                placeholder="Full name"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)] outline-none transition-all placeholder-gray-500"
                            />
                            {errors.fullName && (
                                <span className="text-xs text-red-400">{errors.fullName.message}</span>)}
                        </div>
                        <div className="relative">
                            <label className="text-sm font-medium text-gray-300">Username</label>
                            <input
                                type="text"
                                name="userName"
                                {...register("userName")}
                                onFocus={() => setApiError("")}
                                placeholder="Username"
                                className="w-full bg-[#121212] border border-gray-800 rounded-sm px-3 py-3 text-sm hover:border-gray-500 focus:border-[rgb(96,94,232)] outline-none transition-all placeholder-gray-500"
                            />
                            {errors.userName && (
                                <span className="text-xs text-red-400">{errors.userName.message}</span>)}
                        </div>
                    </div>

                    <button type="submit" className="w-full bg-[rgb(96,94,232)] hover:bg-[rgb(85,83,226)] active:bg-[rgb(85,83,226)] text-white font-semibold py-2 rounded-lg transition-colors text-sm cursor-pointer">
                        {isSubmitting ? "Creating..." : "Create Account"}
                    </button>
                </form>

                <div className="pt-8 space-y-4">
                    <Link to="/" className="flex items-center justify-center border border-gray-800 py-2 rounded-lg text-sm font-semibold hover:bg-[rgb(85,83,226)] active:bg-[rgb(85,83,226)] transition-all cursor-pointer">
                        Login
                    </Link>
                </div>
                <div className="flex justify-center pt-8 opacity-60">
                    <span className="text-xs font-bold tracking-widest text-gray-400">UPARYA</span>
                </div>
            </div>
        </div>
    )
}

export default SignupForm;