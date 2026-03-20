import { useEffect, useState, useRef } from "react";
import ChatHeader from "./ChatHeader";
import MessageInput from "./MessageInput";
import { getMessages } from "../../../services/roomService";
import MesssageBubble from "./MessageBubble";
import { ms } from "zod/locales";

const ChatWindow = ({ activeChat, onSendMessage }) => {

  const [messages, setMessages] = useState([]);
  const scrollRef = useRef(null);

  useEffect(() => {
    getMessages(activeChat.roomId)
      .then((response) => {
        console.log("Messages for room", activeChat.roomId, response.data);
        setMessages(response.data.content);
      })
      .catch((error) => {
      console.error("Error fetching messages:", error.response ? error.response.data.detail : error);
    });
  }, [activeChat.roomId]);


  useEffect(() => {
    // Only proceed if there is a message and it belongs to THIS room
    if (activeChat?.lastMessage && activeChat.lastMessage.roomId === activeChat.roomId) {
        
        setMessages((prev) => {
            // Use a unique ID check to prevent duplicates (Standard Practice)
            const isDuplicate = prev.some(m => m.messageId === activeChat.lastMessage.messageId);
            if (isDuplicate) return prev;
            
            console.log("New message received in Window:", activeChat.lastMessage);
            
            // Since you use flex-col-reverse, new messages go at the START [0]
            return [activeChat.lastMessage, ...prev];
        });
    }
}, [activeChat.lastMessage, activeChat.roomId]);

// Auto-scroll to bottom when messages load
  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages]);


  
  return (
    <div className="flex-1 flex flex-col h-screen bg-white">
      <ChatHeader room={activeChat} />
      
      <main 
        ref={scrollRef}
        className="flex-1 overflow-y-auto p-4 md:p-6 space-y-4 bg-[#f9f9f9] flex flex-col-reverse"
      >
        {messages.map((msg) => {
          return (
              <MesssageBubble msg={msg} key={msg.messageId} />
          );
        })}
      </main>

      <MessageInput onSendMessage={onSendMessage} />
    </div>
  );
};

export default ChatWindow;