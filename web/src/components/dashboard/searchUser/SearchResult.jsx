import { UserPlus } from "lucide-react";

const SearchResult = ({ result, createNewRoom}) => {

    return(
        <div className="w-full flex items-center justify-between p-2 bg-white border border-gray-100 rounded-lg shadow-sm">
                      <div className="flex items-center gap-2 truncate">
                        <div className="w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center text-[#605ee8] font-bold text-xs uppercase">
                          {result.fullName.charAt(0)}
                        </div>
                        <div className="truncate">
                          <p className="text-[11px] font-bold text-gray-800 truncate leading-none">{result.username}</p>
                          <p className="text-[10px] text-gray-500 truncate">{result.email}</p>
                        </div>
                      </div>
                      <button onClick={() => createNewRoom(result)} className="p-1.5 text-[#605ee8] hover:bg-indigo-50 rounded-lg transition-colors">
                        <UserPlus size={16} />
                      </button>
                    </div>
    );
};

export default SearchResult;