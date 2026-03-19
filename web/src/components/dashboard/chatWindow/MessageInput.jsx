import { Paperclip, Send, Smile } from 'lucide-react';
import { useState } from 'react';

const MessageInput = ({ onSendMessage }) => {
  const [message, setMessage] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (message.trim()) {
      onSendMessage(message);
      setMessage(''); // Clear input after sending
    }
  };

  return (
    <footer className="p-4 md:p-6 border-t border-gray-100 bg-white">
      <form 
        onSubmit={handleSend}
        className="flex items-center gap-2 md:gap-4 bg-gray-50 rounded-2xl px-4 py-1.5 md:py-2 focus-within:ring-2 focus-within:ring-indigo-100 transition-all border border-transparent focus-within:border-indigo-200"
      >
        {/* Attachments */}
        {/* <button type="button" className="text-gray-400 hover:text-[#605ee8] cursor-pointer transition-colors">
          <Paperclip size={20} />
        </button> */}

        {/* Input Field */}
        <input 
          type="text" 
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type a message..." 
          className="flex-1 bg-transparent border-none outline-none py-2 md:py-3 text-sm text-gray-800 placeholder-gray-400"
        />

        {/* Emoji and Send */}
        <div className="flex items-center gap-2">
          {/* <button type="button" className="hidden sm:block text-gray-400 hover:text-yellow-500 cursor-pointer transition-colors">
            <Smile size={20} />
          </button> */}
          
          <button 
            type="submit" 
            disabled={!message.trim()}
            className={`p-2 rounded-xl transition-all duration-200 ${
              message.trim() 
                ? 'text-[#605ee8] hover:scale-110 active:scale-95' 
                : 'text-gray-300 cursor-not-allowed'
            }`}
          >
            <Send size={20} />
          </button>
        </div>
      </form>
    </footer>
  );
};

export default MessageInput;