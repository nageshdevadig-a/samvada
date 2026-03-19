import { Link, useParams, useNavigate } from "react-router";
import { useEffect, useState} from "react";
import SideBar from "../components/dashboard/SideBar";
import SearchBar from "../components/dashboard/chatSideBar/SearchBar";
import ConversationCard from "../components/dashboard/chatSideBar/ConversationCard";
import { getRooms } from "../services/roomService";
import ChatWindow from "../components/dashboard/chatWindow/ChatWindow";


const DashBoard = () => {
    const [chats, setChats] = useState([]);
    const[selectedChat, setSelectedChat] = useState(null);

    const navigate = useNavigate();
    const {roomId} = useParams();

    useEffect(() => {
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


    // return (
    //     <div className="flex flex-col min-h-screen font-sans">
    //         {/* Main Container */}
    //         <div className="flex flex-1 flex-col md:flex-row md:w-1/3">
    //             <SideBar />
    //             <div className="flex flex-1 flex-col border-r border-gray-200">
    //                 <SearchBar messageCount={chats.length} />
    //                 {chats.map((chat, i) => <ConversationCard key={chat.roomId} chat={chat} />)}
    //             </div>
    //         </div>
        

    //     </div>
    // )

    return (
        /* h-screen stops the page from growing; overflow-hidden keeps scrollbars internal */
        <div className="flex h-screen w-full bg-white overflow-hidden font-sans">
            
            {/* 1. Leftmost Navigation (80px) */}
            <SideBar />

            {/* 2. Chat List Sidebar (Fixed width: 350px) */}
            <div className="w-full md:w-[350px] flex flex-col border-r border-gray-100 bg-white">
                <SearchBar messageCount={chats.length} />
                
                <div className="flex-1 overflow-y-auto custom-scrollbar">
                    {chats.map((chat) => (
                        <div 
                            key={chat.roomId} 
                            onClick={() => handleChatSelect(chat)} // Handle selection
                        >
                            <ConversationCard 
                                chat={chat} 
                                isActive={selectedChat?.roomId === chat.roomId} 
                            />
                        </div>
                    ))}
                </div>
            </div>

            {/* 3. Main Chat Window (Flexible: takes remaining space) */}
            <main className="flex-1 flex flex-col min-w-0 bg-gray-50">
                {selectedChat ? (
                    <ChatWindow 
                        activeChat={selectedChat} 
                        onSendMessage={(text) => console.log("Sending:", text)}
                    />
                ) : (
                    <div className="flex-1 flex flex-col items-center justify-center text-gray-400">
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