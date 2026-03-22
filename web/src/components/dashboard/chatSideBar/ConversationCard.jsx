

const ConversationCard = ({ room, isActive, lastMessage}) => {
  


  return (
    <div className={`flex gap-4 p-4 mx-4 rounded-2xl cursor-pointer transition-all ${
      isActive ? 'bg-[#f7f7ff] shadow-sm' : 'hover:bg-gray-50'
    }`}>
      {/* Avatar */}
      <div className="relative shrink-0">
        <div className="w-12 h-12 bg-[#605ee8] rounded-full flex items-center justify-center font-bold text-white text-xl">
          {room.roomName.charAt(0).toUpperCase()}
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 min-w-0">
        <div className="flex justify-between items-start mb-1">
          <h3 className="font-semibold text-gray-800 text-sm truncate">{room.roomName}</h3>
          <span className="text-[10px] text-gray-400 font-medium">{"12:00 PM"}</span>
        </div>
        <p className="text-xs text-gray-500 truncate mb-2">{lastMessage?.content || "No new messages yet"}</p>

      </div>
    </div>
  );
};

export default ConversationCard;