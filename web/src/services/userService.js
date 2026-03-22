import api from "../api/axios";

export const searchUsers = (query) => {
    return api.get(`/v1/users/search?query=${query}`)
}