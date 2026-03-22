import api from "../api/axios"

export const getRooms = () => {
   return api.get("/v1/rooms")
}

export const getMessages = (roomId) => {
   return api.get(`/v1/messages/${roomId}`)
}

export const sendMessage = (message, roomId) => {
   return api.post(`/v1/messages/${roomId}`, {content:message})
}

export const createRoom = (targetEmail) => {
   return api.post('/v1/rooms', { targetEmail : targetEmail })
}