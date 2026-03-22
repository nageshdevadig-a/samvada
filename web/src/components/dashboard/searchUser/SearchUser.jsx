import SeachForm from "./searchForm";
import SearchResult from "./searchResult";
import { Loader2, ArrowLeft } from "lucide-react";

const SearchUser = ({ handleUserSearch, query, setQuery, loading, error, result, createNewRoom}) => {
    
    // Optional: Function to go back if used as a page
    const handleBack = () => {
        if (window.history.length > 1) {
            window.history.back();
        }
    };

    return (
        /* Wrapper: On desktop (md:), we need relative to anchor the dropdown.
           On mobile, we don't need it because it will be a full-screen fixed element.
        */
        <div className="md:relative w-full">
            
            <div className="
                /* MOBILE / PAGE MODE (Default) */
                fixed inset-0 z-[50] bg-white flex flex-col
                
                /* DESKTOP / DROPDOWN MODE (md: 768px+) */
                md:absolute md:inset-auto md:right-0 md:top-full md:mt-3 
                md:w-80 md:min-h-0 md:h-auto md:bg-white md:rounded-2xl 
                md:shadow-2xl md:border md:border-gray-100 md:p-4 md:z-20
                md:animate-in md:fade-in md:zoom-in md:duration-200 md:origin-top-right
            ">
                
                {/* Header Section */}
                <div className="flex items-center gap-3 p-4 md:p-0 md:mb-3 border-b md:border-none border-gray-100">
                    <button 
                        onClick={handleBack}
                        className="md:hidden p-1 -ml-1 text-gray-500 hover:bg-gray-100 rounded-full"
                    >
                        <ArrowLeft size={20} />
                    </button>
                    <h4 className="text-lg md:text-sm font-bold text-gray-700">
                        {/* Hidden on desktop dropdown, shown on mobile page */}
                        <span className="md:hidden">Find People</span>
                        <span className="hidden md:inline">Add Connection</span>
                    </h4>
                </div>

                {/* Form Section */}
                <div className="p-4 md:p-0">
                    <SeachForm handleUserSearch={handleUserSearch} query={query} setQuery={setQuery} />
                    
                    <div className="mt-4 min-h-[120px] md:min-h-[80px] flex flex-col items-center justify-center bg-gray-50/50 rounded-xl p-4 md:p-2 border border-dashed border-gray-200">
                        {loading && <Loader2 className="animate-spin text-[#605ee8]" size={24} />}
                        
                        {error && (
                            <p className="text-sm md:text-xs text-red-500 font-medium text-center italic">
                                User not found
                            </p>
                        )}
                        
                        {result && (
                            <div className="w-full animate-in slide-in-from-bottom-2 duration-300">
                                <SearchResult result={result} createNewRoom={createNewRoom}/>
                            </div>
                        )}
                        
                        {!loading && !result && !error && (
                            <div className="text-center px-4">
                                <p className="text-sm md:text-[10px] text-gray-400">
                                    Search for friends by username or email
                                </p>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SearchUser;