import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import type { RootState } from '../store/store'
import { logout } from '../store/authSlice'
import { clearCartState } from '../store/cartSlice'

export default function Navbar() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const { isAuthenticated, user } = useSelector((state: RootState) => state.auth)
  const { totalItems } = useSelector((state: RootState) => state.cart)
  const [keyword, setKeyword] = useState('')

  const handleLogout = () => {
    dispatch(logout())
    dispatch(clearCartState())
    navigate('/login')
  }

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    if (keyword.trim()) navigate(`/products?keyword=${encodeURIComponent(keyword.trim())}`)
  }

  return (
    <header className="sticky top-0 z-50">
      {/* Main navbar */}
      <div className="bg-[#131921] text-white">
        <div className="max-w-[1500px] mx-auto px-3">

          {/* Top row — always visible */}
          <div className="flex items-center gap-2 h-[60px]">

            {/* Logo */}
            <Link to="/" className="flex items-center border-2 border-transparent hover:border-white rounded px-2 py-1 shrink-0">
              <span className="text-white font-extrabold text-xl tracking-tight">shop</span>
              <span className="text-[#FF9900] font-extrabold text-xl">zio</span>
            </Link>

            {/* Deliver to — desktop only */}
            {isAuthenticated && (
              <div className="hidden lg:flex flex-col text-xs shrink-0 border-2 border-transparent hover:border-white rounded px-1 py-1 cursor-pointer">
                <span className="text-gray-400">Hello, {user?.firstName}</span>
                <span className="font-bold text-sm">Deliver to India</span>
              </div>
            )}

            {/* Search bar — desktop only */}
            <form onSubmit={handleSearch} className="hidden md:flex flex-1 h-10 mx-2">
              <select className="bg-[#F3F3F3] text-gray-700 text-xs px-2 border-r border-gray-300 rounded-l-sm w-16 shrink-0">
                <option>All</option>
              </select>
              <input
                type="text"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                placeholder="Search products..."
                className="flex-1 px-3 text-gray-900 text-sm focus:outline-none focus:ring-2 focus:ring-[#FF9900]"
              />
              <button type="submit" className="bg-[#FF9900] hover:bg-[#FA8900] px-4 rounded-r-sm flex items-center justify-center shrink-0">
                <svg className="w-5 h-5 text-gray-900" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-4.35-4.35M17 11A6 6 0 115 11a6 6 0 0112 0z" />
                </svg>
              </button>
            </form>

            {/* Spacer on mobile */}
            <div className="flex-1 md:hidden" />

            {/* Account — desktop */}
            <div className="hidden md:flex flex-col text-xs border-2 border-transparent hover:border-white rounded px-1 py-1 cursor-pointer shrink-0"
              onClick={() => isAuthenticated ? navigate('/profile') : navigate('/login')}>
              <span className="text-gray-400">{isAuthenticated ? `Hello, ${user?.firstName}` : 'Hello, sign in'}</span>
              <span className="font-bold text-sm flex items-center gap-1">
                Account & Lists
                <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20"><path d="M5.23 7.21a.75.75 0 011.06.02L10 10.94l3.71-3.71a.75.75 0 111.06 1.06l-4.24 4.25a.75.75 0 01-1.06 0L5.21 8.27a.75.75 0 01.02-1.06z" /></svg>
              </span>
            </div>

            {/* Returns & Orders — desktop */}
            <Link to="/orders" className="hidden lg:flex flex-col text-xs border-2 border-transparent hover:border-white rounded px-1 py-1 shrink-0">
              <span className="text-gray-400">Returns</span>
              <span className="font-bold text-sm">& Orders</span>
            </Link>

            {/* Account icon — mobile only */}
            <button
              onClick={() => isAuthenticated ? navigate('/profile') : navigate('/login')}
              className="md:hidden border-2 border-transparent hover:border-white rounded p-1 shrink-0">
              <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z"/>
              </svg>
            </button>

            {/* Cart — always visible */}
            <Link to="/cart" className="flex items-center gap-1 border-2 border-transparent hover:border-white rounded px-2 py-1 shrink-0">
              <div className="relative">
                <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M7 18c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm10 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-14.5-16H1v2h1.5l3.5 7.59L4.25 14c-.16.28-.25.61-.25.96C4 16.1 4.9 17 6 17h14v-2H6.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63H19c.75 0 1.41-.41 1.75-1.03l3.58-6.49A1 1 0 0023.45 4H5.21l-.94-2H2.5z"/>
                </svg>
                {totalItems > 0 && (
                  <span className="absolute -top-1 left-5 bg-[#FF9900] text-gray-900 text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                    {totalItems}
                  </span>
                )}
              </div>
              <span className="hidden md:block font-bold text-sm">Cart</span>
            </Link>

            {/* Sign in / out — desktop */}
            {isAuthenticated ? (
              <button onClick={handleLogout}
                className="hidden md:block text-xs border-2 border-transparent hover:border-white rounded px-2 py-1 font-medium shrink-0">
                Sign Out
              </button>
            ) : (
              <Link to="/register"
                className="hidden md:block text-xs border-2 border-transparent hover:border-white rounded px-2 py-1 font-medium shrink-0">
                Sign Up
              </Link>
            )}
          </div>

          {/* Mobile search bar */}
          <form onSubmit={handleSearch} className="flex md:hidden pb-2">
            <input
              type="text"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              placeholder="Search products..."
              className="flex-1 px-3 h-9 text-gray-900 text-sm focus:outline-none rounded-l-sm"
            />
            <button type="submit" className="bg-[#FF9900] hover:bg-[#FA8900] px-4 rounded-r-sm flex items-center justify-center shrink-0">
              <svg className="w-5 h-5 text-gray-900" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-4.35-4.35M17 11A6 6 0 115 11a6 6 0 0112 0z" />
              </svg>
            </button>
          </form>

        </div>
      </div>

      {/* Secondary nav bar */}
      <div className="bg-[#232F3E] text-white text-sm">
        <div className="max-w-[1500px] mx-auto px-3 flex items-center gap-1 h-[38px] overflow-x-auto whitespace-nowrap scrollbar-hide">
          <Link to="/products" className="px-3 py-1 hover:border hover:border-white rounded text-xs font-medium shrink-0">
            ☰ All
          </Link>
          {['Electronics', 'Clothing', 'Home & Kitchen', 'Books', 'Sports', 'Beauty', 'Toys & Games', 'Health & Wellness', 'Grocery'].map((cat) => (
            <Link key={cat} to={`/products?category=${encodeURIComponent(cat)}`}
              className="px-3 py-1 hover:border hover:border-white rounded text-xs shrink-0">
              {cat}
            </Link>
          ))}
          <Link to="/products" className="px-3 py-1 hover:border hover:border-white rounded text-xs text-[#FF9900] font-medium shrink-0">
            Today's Deals
          </Link>
        </div>
      </div>
    </header>
  )
}
