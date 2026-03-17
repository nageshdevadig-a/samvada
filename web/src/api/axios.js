import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
    timeout: 5000,
    withCredentials: true,

})


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


export default api;