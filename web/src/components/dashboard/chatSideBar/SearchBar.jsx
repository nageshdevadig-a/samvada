import { Search, Plus, TextAlignJustify, X, UserPlus, Loader2 } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router';
import { searchUsers } from '../../../services/userService';
import { createRoom } from '../../../services/roomService';
import SearchUser from '../searchUser/SearchUser';
import { set } from 'zod';

const SearchBar = ({ messageCount, setNewRoom}) => {

  const navigate = useNavigate();
  const location = useLocation();

  const [showPopup, setShowPopup] = useState(false);
  const [query, setQuery] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);

  const isUserSearchPage = location.pathname === "/search-user";

  const closePopup = () => {
    setShowPopup(false);
    if (isUserSearchPage) {
      navigate("/", { replace: true }); // Go back to the previous page
    }

  };

  const handleUserSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setError(false);
    setResult(null);

    try {
      // Calling your backend search endpoint
      const res = await searchUsers(query);
      setResult(res.data);
    } catch (err) {
      setError(true);
    } finally {
      setLoading(false);
    }
  };

  const createNewRoom = async (user) => {
    try {
      const res = await createRoom(user.email);
      // Optionally, you can navigate to the new room or update the chat list here
      console.log("New room created:", res.data);
      setNewRoom((prevRooms) => {
        const exists = prevRooms.some(room => room.roomId === res.data.roomId);
        if (exists) return prevRooms; // Avoid duplicates
        return [res.data, ...prevRooms]; // Add new room to the top of the list
      });
      setShowPopup(false);
      if (isUserSearchPage) {
        navigate(`/room/${res.data.roomId}`, { replace: true });
      }
    } catch (err) {
      console.error("Error creating room:", err.response ? err.response.data.detail : err);
    }
  };

  useEffect(() => {
    // If we navigate to /search-user directly, show the popup
    if (isUserSearchPage) {
      setShowPopup(true);
    }
    else {
      setShowPopup(false);
    }
  }, [isUserSearchPage]);

  return (
    <div className="p-6 space-y-6">
      {/* Desktop Header */}
      <div className="hidden md:flex items-center justify-between border-b border-gray-200 pb-3 mb-2">
        <div className="flex items-center gap-2">
          <h1 className="text-xl font-bold text-gray-800">Messages</h1>
          {messageCount > 0 && (
            <span className="bg-gray-200 font-medium text-sm mt-1 rounded-full w-5 h-5 flex items-center justify-center">
              {messageCount}
            </span>
          )}
        </div>

        {/* --- PLUS BUTTON & FLOATING WINDOW --- */}

        <div className="relative">
          <button
            onClick={() => isUserSearchPage ? navigate("/") : navigate("/search-user")}
            className={`w-10 h-10 flex items-center justify-center text-white rounded-full shadow-lg transition-all cursor-pointer z-20 relative ${showPopup ? 'bg-[#605ef9] rotate-45' : 'bg-[#605ee8] shadow-indigo-200 hover:bg-indigo-600'
              }`}
          >
            <Plus size={20} />
          </button>

          {showPopup && (
            <>
              <div className="fixed inset-0 z-10" onClick={() => closePopup()}></div>
              <SearchUser
                handleUserSearch={handleUserSearch}
                query={query}
                setQuery={setQuery}
                loading={loading}
                error={error}
                result={result}
                createNewRoom={createNewRoom}
              />
            </>
          )}
        </div>
      </div>

      {/* Mobile Header */}
      <div className="flex md:hidden items-center justify-between border-b border-gray-200 pb-3 mb-2">
        <h1 className="text-xl font-bold tracking-widest text-gray-800 uppercase">Samvada</h1>
        <button className="w-10 h-10 flex items-center justify-center text-[#605ee8] cursor-pointer">
          <TextAlignJustify size={24} strokeWidth={2.5} />
        </button>
      </div>
      {showPopup && (
        <div className="md:hidden">
          <SearchUser
            handleUserSearch={handleUserSearch}
            query={query} setQuery={setQuery}
            loading={loading} error={error}
            result={result} createNewRoom={createNewRoom}
          />
        </div>
      )}

      {/* Main Page Search */}
      <div className="relative group">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-[#605ee8] transition-colors" size={18} />
        <input
          type="text"
          placeholder="Search messages"
          className="w-full bg-gray-100 border-none rounded-xl py-3 pl-10 pr-4 text-sm outline-none focus:ring-2 focus:ring-indigo-100 transition-all"
        />
      </div>
    </div>
  );
};

export default SearchBar;