export const getDeviceId = () => {
    let deviceId = localStorage.getItem("samvada_deviceId");

    if (!deviceId) {
        deviceId = window.crypto.randomUUID().replaceAll("-", ""); // Generate a new UUID and remove dashes

        localStorage.setItem("samvada_deviceId", deviceId);
    } 
    return deviceId;
};