import { email, regex, z } from 'zod';

export const signupSchema = z.object({

    email: z.string()
    .regex(/^[^\s@]+@[^\s@]+\.[^\s@]+$/, 
        "Enter a valid email address")
    .max(150, "Maximum 150 characters"),

    password: z.string()
        .min(8, "Password must be at least 8 characters")
        .max(32, "Maximum 32 characters")
        .regex(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{}|;:',.<>?])/,
            "Password must have uppercase, lowercase, number, and special character"
        ),

    fullName: z.string()
        .regex(/^[a-zA-Z][a-zA-Z\s]*$/, "Full name must only contain letters and spaces"),


    userName: z.string()
        .min(3, "Must be at least 3 characters")
        .max(30, "Maximum 30 characters")
        .regex(/^[a-z][a-z0-9_]*$/, "Username must start with a letter and only contain lowercase letters, numbers, or underscores (_)"),
    
});