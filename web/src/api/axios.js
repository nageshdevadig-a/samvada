import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
    timeout: 5000,
    withCredentials: true,

});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) prom.reject(error);
        else prom.resolve(token);
    });
    failedQueue = [];
};


api.interceptors.request.use((config) => {
    const method = config.method.toUpperCase();

    const stateChangeMethods = ["POST", "PUT", "PATCH", "DELETE"];

    if (stateChangeMethods.includes(method)) {
        config.headers["X-Samvada-CSRF"] = "v1";

    }
    // 1. Check if the data we are sending is a File or FormData
    if (config.data instanceof FormData) {
        config.headers['Content-Type'] = 'multipart/form-data';
    } else {
        // 2. Default for everything else (JSON)
        config.headers['Content-Type'] = 'application/json';
    }

    // 3. Set the Accept header based on the request
    // If we are calling an 'image' endpoint, we might expect a blob/binary
    if (config.url.includes('/images/download')) {
        config.headers['Accept'] = 'image/png, image/jpeg, application/octet-stream';
    } else {
        config.headers['Accept'] = 'application/json';
    }

    return config;
}, (error) => {
    return Promise.reject(error);
})


api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        const deviceId = localStorage.getItem("samvada_deviceId");

        const isAuthRequest = originalRequest.url.includes("/v1/auth/login");

        if (error.response?.status === 401 && !originalRequest._retry && !isAuthRequest && deviceId) {
            console.log("Message from Response interceptor");

            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(() => api(originalRequest))
                    .catch(err => Promise.reject(err));
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                await api.post("/v1/auth/refresh_token", null, {
                    headers: {
                        "X-Device-Id": deviceId
                    }
                });
                isRefreshing = false;
                processQueue(null);
                return api(originalRequest);
            }
            catch (refreshError) {
                isRefreshing = false;
                processQueue(refreshError, null);
                localStorage.removeItem("samvada_user");
                window.location.href = "/";
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error)
    }
);


export default api;