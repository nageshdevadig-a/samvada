import { MoreHorizontal } from 'lucide-react';

const ChatHeader = ({ room }) => {
  if (!room) return <div className="h-[73px] border-b border-gray-100" />;

  return (
    <header className="p-4 border-b border-gray-100 flex items-center justify-between bg-white">
      <div className="flex items-center gap-3">
        <div className="relative">
          <div className="w-10 h-10 bg-[#605ee8] rounded-full flex items-center justify-center font-bold text-white text-xl">
          {room.roomName.charAt(0)}
        </div>
          {room.online && (
            <div className="absolute bottom-0 right-0 w-3 h-3 bg-emerald-500 border-2 border-white rounded-full"></div>
          )}
        </div>
        <div>
          <h2 className="font-bold text-gray-800 leading-tight text-sm md:text-base">
            {room.roomName}
          </h2>
          {/* <p className={`text-[10px] md:text-xs font-medium ${room.online ? 'text-emerald-500' : 'text-gray-400'}`}>
            {room.online ? 'Online' : 'Offline'}
          </p> */}
        </div>
      </div>

      <div className="flex items-center gap-2">
        <button className="p-2 text-gray-400 hover:text-gray-600 cursor-pointer transition-colors">
          <MoreHorizontal size={20} />
        </button>
      </div>
    </header>
  );
};

export default ChatHeader;