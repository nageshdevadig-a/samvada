import api from "../api/axios"

export const getRooms = () => {
   return api.get("/v1/rooms")
}

export const getMessages = (roomId) => {
   return api.get(`/v1/messages/${roomId}`)
}