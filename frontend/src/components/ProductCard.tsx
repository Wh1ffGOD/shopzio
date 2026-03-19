import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { cartApi } from '../api'
import { setCart } from '../store/cartSlice'
import type { RootState } from '../store/store'
import type { Product } from '../types'

interface Props {
  product: Product
}

function StarRating({ rating, count }: { rating: number; count: number }) {
  const full = Math.floor(rating)
  const half = rating - full >= 0.5
  return (
    <div className="flex items-center gap-1">
      <div className="flex text-[#FF9900] text-sm">
        {Array.from({ length: 5 }).map((_, i) => (
          <span key={i}>
            {i < full ? '★' : (i === full && half ? '⯨' : '☆')}
          </span>
        ))}
      </div>
      <span className="text-[#007185] text-xs hover:text-[#C7511F] cursor-pointer">{count.toLocaleString()}</span>
    </div>
  )
}

export default function ProductCard({ product }: Props) {
  const dispatch = useDispatch()
  const { isAuthenticated } = useSelector((state: RootState) => state.auth)
  const [adding, setAdding] = useState(false)
  const [added, setAdded] = useState(false)

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (!isAuthenticated) { window.location.href = '/login'; return }
    setAdding(true)
    try {
      const res = await cartApi.addItem(product.id, 1)
      dispatch(setCart(res.data))
      setAdded(true)
      setTimeout(() => setAdded(false), 2000)
    } catch (err) {
      console.error('Failed to add to cart', err)
    } finally {
      setAdding(false)
    }
  }

  return (
    <div className="bg-white group hover:shadow-lg transition-shadow duration-200 flex flex-col h-full">
      <Link to={`/products/${product.id}`} className="flex flex-col h-full">
        {/* Image */}
        <div className="aspect-square overflow-hidden bg-white p-3 flex items-center justify-center">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="max-w-full max-h-full object-contain group-hover:scale-105 transition-transform duration-300"
            onError={(e) => (e.currentTarget.src = 'https://via.placeholder.com/300?text=No+Image')}
          />
        </div>

        {/* Info */}
        <div className="px-3 pb-2 flex-1 flex flex-col">
          <h3 className="text-sm text-gray-900 hover:text-[#C7511F] line-clamp-2 leading-snug mb-1">
            {product.name}
          </h3>

          <StarRating rating={product.rating} count={product.reviewCount} />

          <div className="mt-1">
            <span className="text-lg font-medium text-gray-900">₹</span>
            <span className="text-xl font-medium text-gray-900">{Math.floor(product.price)}</span>
            <span className="text-sm text-gray-900">.{(product.price % 1).toFixed(2).slice(2)}</span>
          </div>

          <p className="text-xs text-[#007185] mt-0.5">
            {product.stock > 0 ? 'FREE Delivery by Shopzio' : <span className="text-red-600">Out of Stock</span>}
          </p>

          {product.stock > 0 && product.stock < 10 && (
            <p className="text-xs text-red-600 mt-0.5">Only {product.stock} left in stock</p>
          )}
        </div>
      </Link>

      {/* Add to cart button */}
      <div className="px-3 pb-3 mt-auto">
        <button
          onClick={handleAddToCart}
          disabled={product.stock === 0 || adding}
          className={`w-full py-1.5 text-xs font-medium rounded-sm border transition-colors ${
            added
              ? 'bg-[#34A853] text-white border-[#2d8f47]'
              : product.stock === 0
              ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed'
              : 'bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 border-[#FCD200]'
          }`}
        >
          {added ? '✓ Added to Cart' : adding ? 'Adding...' : product.stock === 0 ? 'Out of Stock' : 'Add to Cart'}
        </button>
      </div>
    </div>
  )
}
