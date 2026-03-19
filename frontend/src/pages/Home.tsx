import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { productApi } from '../api'
import ProductCard from '../components/ProductCard'
import type { Product } from '../types'

const CATEGORY_ICONS: Record<string, string> = {
  'Electronics': '💻',
  'Clothing': '👗',
  'Home & Kitchen': '🏠',
  'Books': '📚',
  'Sports': '⚽',
  'Beauty': '💄',
  'Toys & Games': '🎮',
  'Health & Wellness': '💊',
  'Grocery': '🛒',
}

const BANNERS = [
  { bg: 'from-blue-900 to-blue-700', label: 'Electronics Sale', sub: 'Up to 40% off on top brands', cat: 'Electronics' },
  { bg: 'from-green-800 to-green-600', label: 'Fashion Week', sub: 'New arrivals every day', cat: 'Clothing' },
  { bg: 'from-orange-800 to-orange-600', label: 'Home Essentials', sub: 'Upgrade your home today', cat: 'Home & Kitchen' },
]

export default function Home() {
  const [featured, setFeatured] = useState<Product[]>([])
  const [deals, setDeals] = useState<Product[]>([])
  const [categories, setCategories] = useState<string[]>([])
  const [loading, setLoading] = useState(true)
  const [bannerIdx, setBannerIdx] = useState(0)
  const navigate = useNavigate()

  useEffect(() => {
    const load = async () => {
      try {
        const [productsRes, categoriesRes] = await Promise.all([
          productApi.getAll(0, 12, 'rating', 'desc'),
          productApi.getCategories(),
        ])
        const all = productsRes.data.content as Product[]
        setFeatured(all.slice(0, 8))
        setDeals(all.slice(4, 8))
        setCategories(categoriesRes.data)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  useEffect(() => {
    const timer = setInterval(() => setBannerIdx(i => (i + 1) % BANNERS.length), 4000)
    return () => clearInterval(timer)
  }, [])

  const banner = BANNERS[bannerIdx]

  return (
    <div className="min-h-screen">

      {/* Hero Banner */}
      <div className={`relative bg-gradient-to-r ${banner.bg} text-white overflow-hidden`} style={{ minHeight: 340 }}>
        <div className="max-w-[1500px] mx-auto px-6 flex items-center h-[340px]">
          <div>
            <p className="text-[#FF9900] font-semibold text-sm mb-2 uppercase tracking-widest">{banner.sub}</p>
            <h1 className="text-5xl font-extrabold mb-6 leading-tight">{banner.label}</h1>
            <button
              onClick={() => navigate(`/products?category=${encodeURIComponent(banner.cat)}`)}
              className="bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-bold px-8 py-3 rounded-sm text-sm"
            >
              Shop Now
            </button>
          </div>
        </div>
        {/* Banner dots */}
        <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2">
          {BANNERS.map((_, i) => (
            <button key={i} onClick={() => setBannerIdx(i)}
              className={`w-2 h-2 rounded-full transition-all ${i === bannerIdx ? 'bg-white' : 'bg-white/40'}`} />
          ))}
        </div>
      </div>

      <div className="max-w-[1500px] mx-auto px-4 py-4 space-y-6">

        {/* Category Cards */}
        <section>
          <h2 className="text-lg font-bold text-gray-900 mb-3">Shop by Category</h2>
          <div className="flex flex-wrap gap-3">
            {(loading ? Array.from({ length: 9 }) : categories).map((cat, i) => (
              <Link
                key={i}
                to={cat ? `/products?category=${encodeURIComponent(cat as string)}` : '#'}
                className="bg-white p-4 rounded-sm shadow-sm hover:shadow-md transition-shadow text-center group w-28 flex-shrink-0"
              >
                {loading ? (
                  <div className="animate-pulse space-y-2">
                    <div className="w-10 h-10 bg-gray-200 rounded-full mx-auto" />
                    <div className="h-3 bg-gray-200 rounded w-2/3 mx-auto" />
                  </div>
                ) : (
                  <>
                    <div className="text-3xl mb-2">{CATEGORY_ICONS[cat as string] || '🛍️'}</div>
                    <p className="font-semibold text-gray-900 text-xs group-hover:text-[#C7511F] leading-tight">{cat as string}</p>
                    <p className="text-[#007185] text-xs mt-1">Shop now</p>
                  </>
                )}
              </Link>
            ))}
          </div>
        </section>

        {/* Today's Deals */}
        <section className="bg-white p-4 rounded-sm shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-xl font-bold text-gray-900">Today's Deals</h2>
              <p className="text-xs text-gray-500">Limited time offers</p>
            </div>
            <Link to="/products" className="text-[#007185] hover:text-[#C7511F] hover:underline text-sm font-medium">
              See all deals →
            </Link>
          </div>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            {loading
              ? Array.from({ length: 4 }).map((_, i) => (
                <div key={i} className="animate-pulse space-y-2">
                  <div className="aspect-square bg-gray-200 rounded" />
                  <div className="h-3 bg-gray-200 rounded w-3/4" />
                  <div className="h-3 bg-gray-200 rounded w-1/2" />
                </div>
              ))
              : deals.map((p) => (
                <Link key={p.id} to={`/products/${p.id}`} className="group text-center">
                  <div className="aspect-square bg-gray-50 rounded overflow-hidden mb-2 relative">
                    <img src={p.imageUrl} alt={p.name}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      onError={(e) => { e.currentTarget.onerror = null; e.currentTarget.src = `https://placehold.co/300x300/f3f4f6/9ca3af?text=${encodeURIComponent(p.name.slice(0,10))}` }} />
                    <span className="absolute top-2 left-2 bg-red-600 text-white text-xs font-bold px-2 py-0.5 rounded-sm">
                      Deal
                    </span>
                  </div>
                  <p className="text-[#C7511F] font-bold text-sm">Up to 30% off</p>
                  <p className="text-gray-700 text-xs truncate">{p.name}</p>
                  <p className="text-gray-900 font-bold text-sm">₹{p.price.toFixed(2)}</p>
                </Link>
              ))
            }
          </div>
        </section>

        {/* Top Rated Products */}
        <section className="bg-white p-4 rounded-sm shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-bold text-gray-900">Best Sellers</h2>
            <Link to="/products" className="text-[#007185] hover:text-[#C7511F] hover:underline text-sm font-medium">
              See all →
            </Link>
          </div>
          {loading ? (
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              {Array.from({ length: 8 }).map((_, i) => (
                <div key={i} className="animate-pulse space-y-2">
                  <div className="aspect-square bg-gray-200 rounded" />
                  <div className="h-3 bg-gray-200 rounded" />
                  <div className="h-3 bg-gray-200 rounded w-2/3" />
                </div>
              ))}
            </div>
          ) : (
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              {featured.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          )}
        </section>

        {/* Promo Banner */}
        <section className="bg-[#232F3E] text-white p-8 rounded-sm text-center">
          <h2 className="text-2xl font-bold mb-2">Free Delivery on Your First Order</h2>
          <p className="text-gray-300 mb-4 text-sm">Sign up now and enjoy free shipping on your first purchase</p>
          <Link to="/register" className="bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-bold px-8 py-2 rounded-sm text-sm inline-block">
            Join Now — It's Free
          </Link>
        </section>

      </div>
    </div>
  )
}
