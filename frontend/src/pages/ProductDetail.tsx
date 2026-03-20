import { useEffect, useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { productApi, cartApi } from '../api'
import { setCart } from '../store/cartSlice'
import type { RootState } from '../store/store'
import type { Product } from '../types'

function StarRating({ rating, count }: { rating: number; count: number }) {
  const full = Math.floor(rating)
  const half = rating - full >= 0.5
  return (
    <div className="flex items-center gap-2">
      <div className="flex text-[#FF9900] text-lg">
        {Array.from({ length: 5 }).map((_, i) => (
          <span key={i}>{i < full ? '★' : (i === full && half ? '⯨' : '☆')}</span>
        ))}
      </div>
      <span className="text-[#007185] hover:text-[#C7511F] cursor-pointer text-sm">
        {count.toLocaleString()} ratings
      </span>
    </div>
  )
}

export default function ProductDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const { isAuthenticated } = useSelector((state: RootState) => state.auth)

  const [product, setProduct] = useState<Product | null>(null)
  const [quantity, setQuantity] = useState(1)
  const [loading, setLoading] = useState(true)
  const [adding, setAdding] = useState(false)
  const [buyNow, setBuyNow] = useState(false)
  const [selectedImg, setSelectedImg] = useState(0)

  useEffect(() => {
    productApi.getById(Number(id))
      .then((r) => setProduct(r.data))
      .finally(() => setLoading(false))
  }, [id])

  const handleAddToCart = async () => {
    if (!isAuthenticated) { navigate('/login'); return }
    setAdding(true)
    try {
      const res = await cartApi.addItem(product!.id, quantity)
      dispatch(setCart(res.data))
      navigate('/cart')
    } finally {
      setAdding(false)
    }
  }

  const handleBuyNow = async () => {
    if (!isAuthenticated) { navigate('/login'); return }
    setBuyNow(true)
    try {
      const res = await cartApi.addItem(product!.id, quantity)
      dispatch(setCart(res.data))
      navigate('/checkout')
    } finally {
      setBuyNow(false)
    }
  }

  if (loading) return (
    <div className="max-w-[1500px] mx-auto px-4 py-6 animate-pulse">
      <div className="bg-white p-6 rounded-sm flex gap-8">
        <div className="w-80 h-80 bg-gray-200 rounded shrink-0" />
        <div className="flex-1 space-y-4">
          <div className="h-6 bg-gray-200 rounded w-2/3" />
          <div className="h-4 bg-gray-200 rounded w-1/3" />
          <div className="h-8 bg-gray-200 rounded w-1/4" />
        </div>
      </div>
    </div>
  )

  if (!product) return (
    <div className="text-center py-20 text-gray-500">
      <p className="text-lg">Product not found.</p>
      <Link to="/products" className="text-[#007185] hover:underline mt-2 inline-block">← Back to products</Link>
    </div>
  )

  const inStock = product.stock > 0
  const allImages = product.images && product.images.length > 0
    ? product.images
    : [product.imageUrl]

  return (
    <div className="max-w-[1500px] mx-auto px-4 py-4">
      {/* Breadcrumb */}
      <nav className="text-xs text-[#007185] mb-3 flex items-center gap-1">
        <Link to="/" className="hover:text-[#C7511F] hover:underline">Home</Link>
        <span className="text-gray-400">›</span>
        <Link to={`/products?category=${encodeURIComponent(product.category)}`}
          className="hover:text-[#C7511F] hover:underline">{product.category}</Link>
        <span className="text-gray-400">›</span>
        <span className="text-gray-600 truncate max-w-xs">{product.name}</span>
      </nav>

      <div className="flex flex-col lg:flex-row gap-4">
        {/* Image panel with gallery */}
        <div className="bg-white p-4 rounded-sm shadow-sm lg:w-96 shrink-0">
          {/* Main image */}
          <div className="flex items-center justify-center h-72 mb-3">
            <img
              src={allImages[selectedImg]}
              alt={product.name}
              className="max-w-full max-h-72 object-contain"
              onError={(e) => { e.currentTarget.src = `https://placehold.co/400x400?text=${encodeURIComponent(product.name)}` }}
            />
          </div>
          {/* Thumbnails */}
          {allImages.length > 1 && (
            <div className="flex gap-2 justify-center flex-wrap">
              {allImages.map((img, i) => (
                <button
                  key={i}
                  onClick={() => setSelectedImg(i)}
                  className={`w-14 h-14 border-2 rounded p-1 flex items-center justify-center transition-colors ${
                    selectedImg === i ? 'border-[#FF9900]' : 'border-gray-200 hover:border-gray-400'
                  }`}
                >
                  <img
                    src={img}
                    alt={`view ${i + 1}`}
                    className="max-w-full max-h-full object-contain"
                    onError={(e) => { e.currentTarget.style.display = 'none' }}
                  />
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Product info */}
        <div className="bg-white p-6 rounded-sm shadow-sm flex-1">
          <span className="text-xs text-[#007185] hover:text-[#C7511F] cursor-pointer">{product.category}</span>
          <h1 className="text-xl font-medium text-gray-900 mt-1 leading-snug">{product.name}</h1>

          <div className="mt-2">
            <StarRating rating={product.rating} count={product.reviewCount} />
          </div>

          <div className="border-t border-b border-gray-200 my-4 py-4">
            <div className="flex items-baseline gap-1">
              <span className="text-2xl text-gray-700">₹</span>
              <span className="text-4xl font-medium text-gray-900">{Math.floor(product.price).toLocaleString()}</span>
              <span className="text-lg text-gray-700">.{(product.price % 1).toFixed(2).slice(2)}</span>
            </div>
            <p className="text-xs text-gray-500 mt-1">Inclusive of all taxes</p>
            <p className="text-sm text-[#007185] mt-1">FREE Delivery by <strong>Shopzio</strong></p>
          </div>

          <div className="space-y-3">
            <div>
              <span className="text-gray-700 font-medium text-sm">About this item</span>
              <p className="text-sm text-gray-600 mt-1 leading-relaxed">{product.description}</p>
            </div>

            <div className="flex items-center gap-2 text-sm">
              <span className="text-gray-600 w-20">Status:</span>
              <span className={inStock ? 'text-[#007600] font-medium' : 'text-red-600 font-medium'}>
                {inStock ? `In Stock (${product.stock} available)` : 'Out of Stock'}
              </span>
            </div>

            {inStock && product.stock < 10 && (
              <p className="text-red-600 text-sm">Only {product.stock} left in stock - order soon.</p>
            )}
          </div>
        </div>

        {/* Buy box (right panel) */}
        <div className="bg-white p-5 rounded-sm shadow-sm w-full lg:w-64 shrink-0 h-fit">
          <div className="flex items-baseline gap-1 mb-3">
            <span className="text-xl text-gray-700">₹</span>
            <span className="text-3xl font-medium text-gray-900">{Math.floor(product.price).toLocaleString()}</span>
            <span className="text-base text-gray-700">.{(product.price % 1).toFixed(2).slice(2)}</span>
          </div>

          <p className="text-sm text-[#007185] mb-1">FREE Delivery</p>
          <p className="text-sm font-medium mb-3">
            {inStock ? <span className="text-[#007600]">In Stock</span> : <span className="text-red-600">Out of Stock</span>}
          </p>

          {inStock && (
            <>
              {/* Quantity selector */}
              <div className="mb-3">
                <label className="text-xs text-gray-600 block mb-1">Quantity:</label>
                <select value={quantity} onChange={(e) => setQuantity(Number(e.target.value))}
                  className="border border-gray-300 rounded-sm px-2 py-1 text-sm w-full focus:outline-none focus:ring-1 focus:ring-[#FF9900]">
                  {Array.from({ length: Math.min(product.stock, 10) }).map((_, i) => (
                    <option key={i + 1} value={i + 1}>{i + 1}</option>
                  ))}
                </select>
              </div>

              <button onClick={handleAddToCart} disabled={adding}
                className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium py-2 rounded-full text-sm border border-[#FCD200] mb-2 transition-colors disabled:opacity-50">
                {adding ? 'Adding...' : 'Add to Cart'}
              </button>

              <button onClick={handleBuyNow} disabled={buyNow}
                className="w-full bg-[#FF9900] hover:bg-[#FA8900] text-gray-900 font-medium py-2 rounded-full text-sm border border-[#FF8F00] mb-4 transition-colors disabled:opacity-50">
                {buyNow ? 'Please wait...' : 'Buy Now'}
              </button>
            </>
          )}

          <div className="text-xs text-gray-600 space-y-1 border-t pt-3">
            <div className="flex gap-2"><span className="text-gray-500 w-16">Sold by</span><span className="text-[#007185]">Shopzio</span></div>
            <div className="flex gap-2"><span className="text-gray-500 w-16">Ships from</span><span>Shopzio</span></div>
            <div className="flex gap-2"><span className="text-gray-500 w-16">Returns</span><span>Eligible for Return</span></div>
          </div>
        </div>
      </div>
    </div>
  )
}
