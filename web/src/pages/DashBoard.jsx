import { Link, useParams, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { useSocket } from "../api/SocketContext";
import { Plus } from "lucide-react";
import SideBar from "../components/dashboard/SideBar";
import SearchBar from "../components/dashboard/chatSideBar/SearchBar";
import ConversationCard from "../components/dashboard/chatSideBar/ConversationCard";
import { getRooms, sendMessage } from "../services/roomService";
import ChatWindow from "../components/dashboard/chatWindow/ChatWindow";


const DashBoard = () => {
    const [chats, setChats] = useState([]);
    const [selectedChat, setSelectedChat] = useState(null);


    const navigate = useNavigate();
    const { roomId } = useParams();

    const { lastMessage, setIsAuthenticated } = useSocket();

    useEffect(() => {
        setIsAuthenticated(!!sessionStorage.getItem("samvada_user"));
        getRooms().then((response) => {
            console.log("Data", response);
            setChats(response.data);
        }).catch((error) => {
            console.error("Error fetching rooms:", error.response ? error.response.data.detail : error);
        });
    }, []);

    useEffect(() => {
        if (roomId && chats.length > 0) {
            const active = chats.find(c => c.roomId === roomId);
            if (active) {
                setSelectedChat(active);
            }
        }
    }, [roomId, chats]);

    const handleChatSelect = (chat) => {
        // Industry standard: /room/ID?params
        
        navigate(`/room/${chat.roomId}`);
    };

    useEffect(() => {
        if (!lastMessage) return;

        // 1. Update the chat list (Sidebar)
        setChats(prevChats => {
            return prevChats.map(chat =>
                chat.roomId === lastMessage.roomId
                    ? { ...chat, lastMessage: lastMessage }
                    : chat
            );
        });

        // 2. If the user is currently looking at this room, add it to the message window
        if (selectedChat?.roomId === lastMessage.roomId) {
            setSelectedChat(prev => ({ ...prev, lastMessage: lastMessage }));
        }
    }, [lastMessage]);

    const onSendMessage = async (text) => {
        const tempMsg = {
            content: text,
            sending: true // Local flag to show a "loading" spinner if you want
        };

        // Optimistic Update: Show it on screen instantly
        // setMessages(prev => [...prev, tempMsg]);

        try {
            await sendMessage(text, roomId);
        } catch (err) {
            // Handle error (e.g., remove the message or show "Failed to send")
            console.error("Message failed");
        }
    };

    return (
        /* h-screen stops the page from growing; overflow-hidden keeps scrollbars internal */
        <div className="flex h-screen w-full bg-white overflow-hidden font-sans">

            {/* 1. Leftmost Navigation (80px) */}
            <SideBar />

            {/* 2. Chat List Sidebar (Fixed width: 350px) */}
            <div className={` w-full md:w-87.5 flex-col border-r border-gray-100 bg-white
                 ${roomId ? 'hidden md:flex' : 'flex'} `}>
                <SearchBar messageCount={chats.length} setNewRoom={setChats}/>

                <div className="flex-1 relative flex flex-col min-h-0"> {/* Parent Container */}

                    {/* 1. Scrollable Chat List */}
                    <div className="flex-1 overflow-y-auto custom-scrollbar pb-20"> {/* pb-20 prevents button from covering last chat */}
                        {chats.map((chat) => (
                            <div
                                key={chat.roomId}
                                onClick={() => handleChatSelect(chat)}
                            >
                                <ConversationCard
                                    room={chat}
                                    isActive={selectedChat?.roomId === chat.roomId}
                                    lastMessage={chat.lastMessage}
                                />
                            </div>
                        ))}
                    </div>

                    {/* 2. Fixed Floating Plus Button */}
                    <div className="md:hidden absolute bottom-6 right-6 z-30">
                        <button
                            onClick={() => navigate("/search-user")} // Triggers the new page
                            className="w-12 h-12 bg-[#605ee8] rounded-full flex items-center justify-center text-white shadow-2xl shadow-indigo-400 hover:bg-indigo-600 hover:scale-110 active:scale-95 transition-all cursor-pointer group"
                        >
                            <Plus size={24} strokeWidth={2.5} />

                            {/* Tooltip on hover */}
                            <span className="absolute right-16 bg-gray-800 text-white text-[10px] px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none uppercase tracking-widest">
                                New Chat
                            </span>
                        </button>
                    </div>
                </div>
            </div>

            {/* 3. Main Chat Window (Flexible: takes remaining space) */}
            <main className={`flex-1 flex-col min-w-0 bg-gray-50 
                ${roomId ? 'flex' : 'hidden md:flex'}
            `}>
                {selectedChat ? (
                    <ChatWindow
                        activeChat={selectedChat}
                        onSendMessage={onSendMessage}
                        lastMessage={lastMessage}
                    />
                ) : (
                    <div className="hidden md:flex flex-1 flex-col items-center justify-center text-gray-400">
                        <div className="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                            <span className="text-2xl">💬</span>
                        </div>
                        <p className="text-sm font-medium">Select a conversation to start Samvada</p>
                    </div>
                )}
            </main>
        </div>
    );
}


export default DashBoard;