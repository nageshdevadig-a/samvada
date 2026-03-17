import { z } from 'zod';

export const loginSchema = z.object({
    // Matches @ValidIdentity logic
    usernameOrEmail: z.string()
  .min(3, "Must be at least 3 characters")
  .max(100, "Maximum 100 characters")
  .superRefine((val, ctx) => {
    const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
    const isUsername = /^[a-z][a-z0-9_]*$/.test(val);

    // If it's neither an email nor a valid username
    if (!isEmail && !isUsername) {
      // Logic: If it contains an '@', they probably tried an email and failed
      if (val.includes('@')) {
        ctx.addIssue({
          message: "Enter a valid email address",
          fatal: true,
        });
      } else {
        // Otherwise, they are trying a username, so give the specific regex warning
        ctx.addIssue({
          message: "Username must start with a-z and only contain lowercase letters, numbers, or underscores (_)",
          fatal: true,
        });
      }
    }
  }),

    // Matches @ValidPassword logic
    password: z.string()
        .min(8, "Password must be at least 8 characters")
        .max(32, "Maximum 32 characters")
        .regex(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{}|;:',.<>?])/,
            "Password must have uppercase, lowercase, number, and special character"
        )
});