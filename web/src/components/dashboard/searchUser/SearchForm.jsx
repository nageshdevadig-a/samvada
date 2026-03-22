import { Search } from "lucide-react";

const SeachForm = ({ handleUserSearch, query, setQuery }) => {

    return (
        // <form onSubmit={handleUserSearch} className="relative group">
        //           <input
        //             autoFocus
        //             type="text"
        //             value={query}
        //             onChange={(e) => setQuery(e.target.value)}
        //             placeholder="Search username or email"
        //             className="w-full bg-gray-50 border border-gray-200 rounded-xl py-2.5 pl-3 pr-10 text-sm outline-none focus:border-[#605ee8] focus:ring-1 focus:ring-indigo-100 transition-all"
        //           />
        //           <button type="submit" className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-[#605ee8]">
        //             <Search size={16} />
        //           </button>
        //         </form>

        <form
            onSubmit={handleUserSearch}
            className="flex items-center bg-gray-50 border border-gray-200 rounded-xl focus-within:border-[#605ee8] focus-within:ring-1 focus-within:ring-indigo-100 transition-all overflow-hidden"
        >
            <input
                autoFocus
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search username or email"
                className="flex-grow bg-transparent py-2.5 pl-3 pr-2 text-sm outline-none w-full"
            />

            <button
                type="submit"
                className="px-3 py-2.5 text-gray-400 hover:text-[#605ee8] hover:bg-gray-100 border-l border-gray-200 transition-colors cursor-pointer"
                aria-label="Search"
            >
                <Search size={16} />
            </button>
        </form>
    );
};

export default SeachForm;