import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { cartApi } from '../api'
import { setCart, clearCartState } from '../store/cartSlice'
import type { Cart as CartType } from '../types'

export default function Cart() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [cart, setLocalCart] = useState<CartType | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    cartApi.getCart().then((r) => {
      setLocalCart(r.data)
      dispatch(setCart(r.data))
    }).finally(() => setLoading(false))
  }, [])

  const updateQty = async (cartItemId: number, productId: number, quantity: number) => {
    const res = await cartApi.updateItem(cartItemId, productId, quantity)
    setLocalCart(res.data)
    dispatch(setCart(res.data))
  }

  const removeItem = async (cartItemId: number) => {
    const res = await cartApi.removeItem(cartItemId)
    setLocalCart(res.data)
    dispatch(setCart(res.data))
  }

  const clearAll = async () => {
    await cartApi.clearCart()
    const empty = { ...cart!, items: [], totalItems: 0, total: 0 }
    setLocalCart(empty)
    dispatch(clearCartState())
  }

  if (loading) return (
    <div className="max-w-[1500px] mx-auto px-4 py-6">
      <div className="animate-pulse space-y-3">
        <div className="h-8 bg-gray-200 rounded w-48" />
        {Array.from({ length: 3 }).map((_, i) => (
          <div key={i} className="bg-white p-4 flex gap-4">
            <div className="w-24 h-24 bg-gray-200 rounded" />
            <div className="flex-1 space-y-2">
              <div className="h-4 bg-gray-200 rounded w-2/3" />
              <div className="h-3 bg-gray-200 rounded w-1/3" />
            </div>
          </div>
        ))}
      </div>
    </div>
  )

  if (!cart || cart.items.length === 0) return (
    <div className="max-w-[1500px] mx-auto px-4 py-6">
      <div className="bg-white p-10 rounded-sm shadow-sm text-center">
        <div className="text-6xl mb-4">🛒</div>
        <h2 className="text-2xl font-medium text-gray-900 mb-2">Your Shopzio Cart is empty</h2>
        <p className="text-gray-500 mb-6 text-sm">Your shopping cart lives here. Add products you like — items stay here for 30 days.</p>
        <Link to="/products" className="bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium px-8 py-2 rounded-full text-sm border border-[#FCD200]">
          Shop today's deals
        </Link>
      </div>
    </div>
  )

  return (
    <div className="max-w-[1500px] mx-auto px-4 py-4">
      <div className="flex flex-col lg:flex-row gap-4 items-start">

        {/* Cart items */}
        <div className="flex-1 bg-white rounded-sm shadow-sm p-4">
          <div className="flex items-center justify-between border-b pb-3 mb-4">
            <h1 className="text-2xl font-medium text-gray-900">Shopping Cart</h1>
            <span className="text-gray-500 text-sm">Price</span>
          </div>

          <div className="divide-y divide-gray-100">
            {cart.items.map((item) => (
              <div key={item.cartItemId} className="py-4 flex gap-4">
                {/* Product image */}
                <Link to={`/products/${item.productId}`} className="shrink-0">
                  <img src={item.imageUrl} alt={item.productName}
                    className="w-28 h-28 object-contain bg-gray-50 rounded" />
                </Link>

                {/* Details */}
                <div className="flex-1 min-w-0">
                  <Link to={`/products/${item.productId}`}
                    className="text-base text-gray-900 hover:text-[#C7511F] hover:underline font-medium line-clamp-2">
                    {item.productName}
                  </Link>
                  <p className="text-sm text-[#007600] font-medium mt-1">In Stock</p>
                  <p className="text-xs text-gray-500 mt-0.5">Eligible for FREE Shipping</p>

                  {/* Quantity + Actions */}
                  <div className="flex items-center gap-3 mt-3">
                    <div className="flex items-center border border-gray-300 rounded-sm overflow-hidden">
                      <button onClick={() => item.quantity > 1
                        ? updateQty(item.cartItemId, item.productId, item.quantity - 1)
                        : removeItem(item.cartItemId)}
                        className="px-3 py-1.5 hover:bg-gray-100 text-sm bg-gray-50 border-r border-gray-300">
                        {item.quantity === 1 ? '🗑' : '−'}
                      </button>
                      <span className="px-4 py-1.5 text-sm font-medium bg-white">{item.quantity}</span>
                      <button onClick={() => updateQty(item.cartItemId, item.productId, item.quantity + 1)}
                        className="px-3 py-1.5 hover:bg-gray-100 text-sm bg-gray-50 border-l border-gray-300">
                        +
                      </button>
                    </div>
                    <span className="text-gray-300">|</span>
                    <button onClick={() => removeItem(item.cartItemId)}
                      className="text-sm text-[#007185] hover:text-[#C7511F] hover:underline">Delete</button>
                    <span className="text-gray-300">|</span>
                    <button className="text-sm text-[#007185] hover:text-[#C7511F] hover:underline">Save for later</button>
                  </div>
                </div>

                {/* Price */}
                <div className="text-right shrink-0">
                  <span className="text-lg font-bold text-gray-900">₹{item.subtotal.toFixed(2)}</span>
                  {item.quantity > 1 && (
                    <p className="text-xs text-gray-500">₹{item.unitPrice.toFixed(2)} each</p>
                  )}
                </div>
              </div>
            ))}
          </div>

          {/* Subtotal */}
          <div className="border-t pt-3 text-right">
            <span className="text-lg">
              Subtotal ({cart.totalItems} items):{' '}
              <span className="font-bold text-gray-900">₹{cart.total.toFixed(2)}</span>
            </span>
          </div>

          {/* Clear cart */}
          <div className="mt-3">
            <button onClick={clearAll} className="text-sm text-[#007185] hover:text-[#C7511F] hover:underline">
              Clear entire cart
            </button>
          </div>
        </div>

        {/* Order summary */}
        <div className="bg-white rounded-sm shadow-sm p-4 w-full lg:w-72 shrink-0">
          <p className="text-sm text-[#007600] font-medium mb-2">
            ✓ Your order qualifies for FREE Delivery
          </p>
          <p className="text-lg mb-3">
            Subtotal ({cart.totalItems} items):{' '}
            <span className="font-bold">₹{cart.total.toFixed(2)}</span>
          </p>

          <div className="flex items-center gap-2 mb-3">
            <input type="checkbox" id="gift" className="accent-[#FF9900]" />
            <label htmlFor="gift" className="text-sm text-gray-700">This order contains a gift</label>
          </div>

          <button onClick={() => navigate('/checkout')}
            className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium py-2.5 rounded-full text-sm border border-[#FCD200] mb-2 transition-colors">
            Proceed to Buy
          </button>

          <div className="text-xs text-gray-500 mt-3 space-y-1">
            <p>EMI available on select cards. <span className="text-[#007185] cursor-pointer hover:underline">EMI options</span></p>
            <p>No cost EMI available. <span className="text-[#007185] cursor-pointer hover:underline">Know more</span></p>
          </div>
        </div>
      </div>
    </div>
  )
}
