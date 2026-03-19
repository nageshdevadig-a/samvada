const MessageBubble = ({ msg }) => {

    const myEmail = localStorage.getItem("samvada_user") ? JSON.parse(localStorage.getItem("samvada_user")).email : null;

    return (
        <div className={`flex ${msg.senderEmail === myEmail ? 'justify-end' : 'justify-start'}`}>
            <div className={`flex flex-col max-w-[75%] md:max-w-[60%] ${msg.senderEmail === myEmail ? 'items-end' : 'items-start'}`}>
                <div className={`px-4 py-2.5 rounded-2xl text-sm shadow-sm ${msg.senderEmail === myEmail
                        ? 'bg-[#605ee8] text-white rounded-tr-none'
                        : 'bg-white text-gray-800 border border-gray-100 rounded-tl-none'
                    }`}>
                    {msg.content}
                </div>

                {/* Timestamp - Optional but industry standard */}
                <span className="text-[10px] text-gray-400 mt-1 px-1">
                    {msg.sentAt || 'Just now'}
                </span>
            </div>
        </div>
    );
};

export default MessageBubble;