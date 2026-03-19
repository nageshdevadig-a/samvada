import api from "../api/axios";
import { getDeviceId } from "../utils/device";

export const login = (formData) => {

   return api.post("/v1/auth/login", formData, {
        headers: {
            "X-Device-Id": getDeviceId()
        }
    });
};

export const signup = (formData) => {
    return api.post("/v1/auth/signup", formData, {
        headers: {
            "X-Device-Id": getDeviceId()
        }
    });
}

export const logout = () => {
    return api.post("/v1/auth/logout", null);
}