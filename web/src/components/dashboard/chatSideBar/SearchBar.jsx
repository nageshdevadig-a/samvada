import { Search, Plus, TextAlignJustify  } from 'lucide-react';

const SearchBar = ({ messageCount }) => {
  return (
    <div className="p-6 space-y-6 ">
      <div className="hidden md:flex items-center justify-between border-b border-gray-200 pb-3 mb-2">
        <div className="flex items-center gap-2">
          <h1 className="text-xl font-bold text-gray-800">Messages</h1>
          {messageCount > 0 && (
            <span className="bg-gray-200 font-medium text-sm mt-1 rounded-full w-5 h-5 flex items-center justify-center">
              {messageCount}
            </span>
          )}
        </div>
        <button className="w-10 h-10 bg-[#605ee8] rounded-full flex items-center justify-center text-white shadow-lg shadow-indigo-200 cursor-pointer hover:bg-indigo-600 transition-all">
          <Plus size={20} />
        </button>
      </div>
          <div className="flex md:hidden items-center justify-between border-b border-gray-200 pb-3 mb-2">
        <div className="flex items-center gap-2">
          <h1 className="text-xl font-bold tracking-widest text-gray-800">SAMVADA</h1>
        </div>
        <button className="w-10 h-10 rounded-full flex items-center justify-center text-[#605ee8] cursor-pointer hover:bg-indigo-600 transition-all">
          <TextAlignJustify size={20} strokeWidth={3} />
        </button>
      </div>


      {/* Actual Search Input */}
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