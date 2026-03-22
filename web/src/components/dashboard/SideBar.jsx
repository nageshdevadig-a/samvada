import { MessageSquare, Search, Settings } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router';

const SideBar = () => {

  const [activeIndex, setActiveIndex] = useState(0);
  const profileLetter = sessionStorage.getItem("samvada_user") ? JSON.parse(sessionStorage.getItem("samvada_user")).fullName.charAt(0).toUpperCase() : "U";
  const navItems = [
    { icon: <MessageSquare size={22} />, label: 'Messages' },
    { icon: <Search size={22} />, label: 'Search' }
  ];

  const navigate = useNavigate();

  return (
    <div className="w-20 h-screen  hidden md:flex flex-col items-center py-8 justify-between text-black">
      <div className="flex flex-col items-center space-y-10">
        {/* Logo / Brand Icon */}
        <div className="w-10 h-10 bg-[#605ee8] rounded-xl flex items-center justify-center font-bold text-white text-xl">
          {profileLetter}
        </div>

        {/* Navigation Icons */}
        <nav className="flex flex-col space-y-8">
          {navItems.map((item, index) => (
            <button onClick={() => setActiveIndex(index)}
              key={index}
              className={`
        flex items-center justify-center 
        w-10 h-10 rounded-xl cursor-pointer transition-all duration-200
        ${activeIndex === index
                  ? 'bg-gray-100 text-[#605ee8]'
                  : 'text-gray-500 hover:bg-gray-100 hover:text-[#605ee8]'
                }
      `}
            >
              {item.icon}
            </button>
          ))}
        </nav>
      </div>

      {/* Settings at bottom */}
      <button onClick={() => navigate('/logout')} className="cursor-pointer hover:text-[#605ee8] transition-colors">
        <Settings size={22} />
      </button>
    </div>
  );
};

export default SideBar;