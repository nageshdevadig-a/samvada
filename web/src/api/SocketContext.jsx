import React, { createContext, useContext, useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { set } from 'zod';

const SocketContext = createContext(null);

export const SocketProvider = ({ children }) => {
    const [stompClient, setStompClient] = useState(null);
    const [lastMessage, setLastMessage] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem("samvada_user"));


    useEffect(() => {

        if (!isAuthenticated) {
            console.log("User not authenticated, WebSocket will not activate.");
            return;
        }
        // Construct the WebSocket URL
        // If your API is at http://localhost:8080, this becomes ws://localhost:8080/ws
        const wsUrl = import.meta.env.VITE_WS_URL || "ws://localhost:8080/ws";

        const client = new Client({
            brokerURL: wsUrl,
            
            // Standard heartbeats to keep the Azure connection alive
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            reconnectDelay: 5000, // Auto-reconnect if server goes down

            onConnect: () => {
                console.log('Connected via Native WebSocket');
                
                // Save the client to state ONLY after successful connection
                setStompClient(client);

                // Subscribe to personal message queue
                client.subscribe('/user/queue/messages', (message) => {
                    try {
                        const payload = JSON.parse(message.body);
                        setLastMessage(payload);
                    } catch (err) {
                        console.error("Error parsing socket message:", err);
                    }
                });
            },

            onDisconnect: () => {
                console.log('Disconnected from WS');
                setStompClient(null);
            },

            onStompError: (frame) => {
                console.error('STOMP Broker error:', frame.headers['message']);
                console.error('Details:', frame.body);
            },
        });

        client.activate();

        // Cleanup: Disconnect when the user logs out or closes the app
        return () => {
            if (client.active) {
                client.deactivate();
            }
        };
    }, [isAuthenticated]); // Re-run if user changes (e.g., login/logout)

    return (
        <SocketContext.Provider value={{ stompClient, lastMessage, setIsAuthenticated }}>
            {children}
        </SocketContext.Provider>
    );
};

export const useSocket = () => useContext(SocketContext);