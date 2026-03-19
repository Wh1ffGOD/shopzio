import { Link } from 'react-router-dom'

export default function Footer() {
  return (
    <footer className="mt-auto">
      {/* Back to top */}
      <div
        className="bg-[#37475A] hover:bg-[#485769] text-white text-sm text-center py-3 cursor-pointer transition-colors"
        onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
        Back to top
      </div>

      {/* Main footer */}
      <div className="bg-[#232F3E] text-gray-300">
        <div className="max-w-[1500px] mx-auto px-6 py-10 grid grid-cols-2 md:grid-cols-4 gap-8">
          <div>
            <h4 className="text-white font-bold text-sm mb-3">Get to Know Us</h4>
            <ul className="space-y-2 text-xs">
              <li><span className="hover:text-white cursor-pointer hover:underline">About Us</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Careers</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Press Releases</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Shopzio Science</span></li>
            </ul>
          </div>
          <div>
            <h4 className="text-white font-bold text-sm mb-3">Connect with Us</h4>
            <ul className="space-y-2 text-xs">
              <li><span className="hover:text-white cursor-pointer hover:underline">Facebook</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Twitter</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Instagram</span></li>
            </ul>
          </div>
          <div>
            <h4 className="text-white font-bold text-sm mb-3">Make Money with Us</h4>
            <ul className="space-y-2 text-xs">
              <li><span className="hover:text-white cursor-pointer hover:underline">Sell on Shopzio</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Become an Affiliate</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Advertise Your Products</span></li>
            </ul>
          </div>
          <div>
            <h4 className="text-white font-bold text-sm mb-3">Let Us Help You</h4>
            <ul className="space-y-2 text-xs">
              <li><Link to="/profile" className="hover:text-white hover:underline">Your Account</Link></li>
              <li><Link to="/orders" className="hover:text-white hover:underline">Returns Centre</Link></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">100% Purchase Protection</span></li>
              <li><span className="hover:text-white cursor-pointer hover:underline">Help</span></li>
            </ul>
          </div>
        </div>
      </div>

      {/* Bottom bar */}
      <div className="bg-[#131921] text-gray-400 text-xs py-5">
        <div className="max-w-[1500px] mx-auto px-6 flex flex-col md:flex-row items-center justify-between gap-3">
          <div className="flex items-center gap-1">
            <span className="text-white font-extrabold text-base">shop</span>
            <span className="text-[#FF9900] font-extrabold text-base">zio</span>
          </div>
          <div className="flex flex-wrap justify-center gap-4">
            <span className="hover:text-white cursor-pointer hover:underline">Conditions of Use & Sale</span>
            <span className="hover:text-white cursor-pointer hover:underline">Privacy Notice</span>
            <span className="hover:text-white cursor-pointer hover:underline">Interest-Based Ads</span>
          </div>
          <p>© 2026, Shopzio. Built with Spring Boot + React.</p>
        </div>
      </div>
    </footer>
  )
}
