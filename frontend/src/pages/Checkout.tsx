import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useSelector, useDispatch } from 'react-redux'
import { orderApi, paymentApi, cartApi } from '../api'
import { setCart } from '../store/cartSlice'
import type { RootState } from '../store/store'

declare global {
  interface Window { Razorpay: any }
}

const STEPS = ['Address', 'Payment', 'Review Order']

export default function Checkout() {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const { cart } = useSelector((state: RootState) => state.cart)
  const { user } = useSelector((state: RootState) => state.auth)
  const [step, setStep] = useState(0)
  const [address, setAddress] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!cart || cart.items.length === 0) navigate('/cart')
  }, [cart])

  const loadRazorpayScript = (): Promise<boolean> => {
    return new Promise((resolve) => {
      if (window.Razorpay) { resolve(true); return }
      const script = document.createElement('script')
      script.src = 'https://checkout.razorpay.com/v1/checkout.js'
      script.onload = () => resolve(true)
      script.onerror = () => resolve(false)
      document.body.appendChild(script)
    })
  }

  const handlePlaceOrder = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!address.trim()) { setError('Shipping address is required'); return }
    setError('')
    setLoading(true)

    try {
      const orderRes = await orderApi.placeOrder(address, 'RAZORPAY')
      const order = orderRes.data
      const razorpayRes = await paymentApi.createOrder(order.totalAmount, 'INR', order.id)
      const { razorpayOrderId, keyId, amount } = razorpayRes.data

      const scriptLoaded = await loadRazorpayScript()
      if (!scriptLoaded) {
        setError('Failed to load Razorpay. Check your internet connection.')
        setLoading(false)
        return
      }

      const options = {
        key: keyId,
        amount: Math.round(amount * 100),
        currency: 'INR',
        name: 'Shopzio',
        description: `Order #${order.id}`,
        order_id: razorpayOrderId,
        prefill: { name: user ? `${user.firstName} ${user.lastName}` : '', email: user?.email || '' },
        theme: { color: '#FF9900' },
        handler: async (response: { razorpay_order_id: string; razorpay_payment_id: string; razorpay_signature: string }) => {
          try {
            await paymentApi.verifyPayment({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
              internalOrderId: order.id,
            })
            const cartRes = await cartApi.getCart()
            dispatch(setCart(cartRes.data))
            navigate('/orders', { state: { success: true, orderId: order.id } })
          } catch {
            setError('Payment verification failed. Contact support with order #' + order.id)
            setLoading(false)
          }
        },
        modal: {
          ondismiss: () => {
            setError('Payment cancelled. Your order is saved — you can pay later from Orders page.')
            setLoading(false)
          },
        },
      }

      new window.Razorpay(options).open()
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to place order. Please try again.')
      setLoading(false)
    }
  }

  if (!cart) return null

  return (
    <div className="min-h-screen bg-[#EAEDED]">
      {/* Checkout header */}
      <div className="bg-[#131921] py-3 px-4 flex items-center justify-between">
        <div className="flex items-center gap-1">
          <span className="text-white font-extrabold text-xl">shop</span>
          <span className="text-[#FF9900] font-extrabold text-xl">zio</span>
        </div>
        <div className="flex items-center gap-2">
          {STEPS.map((s, i) => (
            <div key={s} className="flex items-center gap-2">
              <button onClick={() => i < step && setStep(i)}
                className={`text-xs font-medium px-3 py-1 rounded-sm ${
                  i === step ? 'text-[#FF9900] border-b-2 border-[#FF9900]' : i < step ? 'text-[#007185]' : 'text-gray-400'
                }`}>
                {i + 1}. {s}
              </button>
              {i < STEPS.length - 1 && <span className="text-gray-600">›</span>}
            </div>
          ))}
        </div>
      </div>

      <div className="max-w-5xl mx-auto px-4 py-6">
        <div className="flex flex-col lg:flex-row gap-4 items-start">

          {/* Main form */}
          <div className="flex-1">

            {/* Step 0: Address */}
            <div className={`bg-white rounded-sm shadow-sm mb-3 ${step !== 0 ? 'opacity-60' : ''}`}>
              <div className="flex items-center justify-between p-4 border-b" onClick={() => step > 0 && setStep(0)}>
                <div className="flex items-center gap-3">
                  <span className={`w-7 h-7 rounded-full flex items-center justify-center text-sm font-bold ${step >= 0 ? 'bg-[#FF9900] text-white' : 'bg-gray-200 text-gray-500'}`}>1</span>
                  <h2 className="font-bold text-gray-900">Delivery Address</h2>
                  {step > 0 && <span className="text-xs text-gray-500 ml-2 truncate max-w-xs">{address}</span>}
                </div>
                {step > 0 && <button onClick={() => setStep(0)} className="text-sm text-[#007185] hover:underline">Change</button>}
              </div>

              {step === 0 && (
                <div className="p-6">
                  <textarea
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder="Enter your full name, street address, city, state, PIN code"
                    rows={4}
                    className="input resize-none rounded-sm text-sm"
                    required
                  />
                  <button
                    onClick={() => { if (address.trim()) { setError(''); setStep(1) } else setError('Address is required') }}
                    className="mt-4 bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium px-6 py-2 rounded-full text-sm border border-[#FCD200]">
                    Use this address
                  </button>
                </div>
              )}
            </div>

            {/* Step 1: Payment */}
            <div className={`bg-white rounded-sm shadow-sm mb-3 ${step < 1 ? 'opacity-40 pointer-events-none' : ''}`}>
              <div className="flex items-center justify-between p-4 border-b" onClick={() => step > 1 && setStep(1)}>
                <div className="flex items-center gap-3">
                  <span className={`w-7 h-7 rounded-full flex items-center justify-center text-sm font-bold ${step >= 1 ? 'bg-[#FF9900] text-white' : 'bg-gray-200 text-gray-500'}`}>2</span>
                  <h2 className="font-bold text-gray-900">Payment Method</h2>
                </div>
                {step > 1 && <button onClick={() => setStep(1)} className="text-sm text-[#007185] hover:underline">Change</button>}
              </div>

              {step === 1 && (
                <div className="p-6">
                  <div className="border-2 border-[#FF9900] rounded-sm p-4 flex items-center gap-3 bg-orange-50">
                    <input type="radio" checked readOnly className="accent-[#FF9900]" />
                    <div>
                      <p className="font-medium text-gray-900 text-sm">Razorpay — UPI, Cards, Net Banking & Wallets</p>
                      <p className="text-xs text-gray-500 mt-0.5">Secure payment powered by Razorpay</p>
                    </div>
                  </div>
                  <p className="text-xs text-gray-500 mt-3">
                    Test card: <span className="font-mono bg-gray-100 px-1">4111 1111 1111 1111</span> · Any future date · Any CVV
                  </p>
                  <button onClick={() => setStep(2)}
                    className="mt-4 bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium px-6 py-2 rounded-full text-sm border border-[#FCD200]">
                    Use this payment method
                  </button>
                </div>
              )}
            </div>

            {/* Step 2: Review */}
            <div className={`bg-white rounded-sm shadow-sm ${step < 2 ? 'opacity-40 pointer-events-none' : ''}`}>
              <div className="flex items-center p-4 border-b">
                <span className={`w-7 h-7 rounded-full flex items-center justify-center text-sm font-bold mr-3 ${step >= 2 ? 'bg-[#FF9900] text-white' : 'bg-gray-200 text-gray-500'}`}>3</span>
                <h2 className="font-bold text-gray-900">Review Items and Delivery</h2>
              </div>

              {step === 2 && (
                <form onSubmit={handlePlaceOrder} className="p-6">
                  <div className="space-y-3 mb-4">
                    {cart.items.map((item) => (
                      <div key={item.cartItemId} className="flex gap-3">
                        <img src={item.imageUrl} alt={item.productName}
                          className="w-16 h-16 object-contain bg-gray-50 rounded shrink-0" />
                        <div className="flex-1">
                          <p className="text-sm font-medium text-gray-900">{item.productName}</p>
                          <p className="text-xs text-[#007600]">In Stock · FREE Delivery</p>
                          <p className="text-xs text-gray-500">Qty: {item.quantity}</p>
                        </div>
                        <p className="text-sm font-bold">₹{item.subtotal.toFixed(2)}</p>
                      </div>
                    ))}
                  </div>

                  {error && <div className="bg-red-50 border border-red-200 text-red-600 text-sm px-4 py-3 rounded-sm mb-4">{error}</div>}

                  <button type="submit" disabled={loading}
                    className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-bold py-3 rounded-full text-sm border border-[#FCD200] disabled:opacity-50 transition-colors">
                    {loading ? 'Opening Razorpay...' : `Place your order — ₹${cart.total.toFixed(2)}`}
                  </button>
                  <p className="text-xs text-gray-500 mt-2 text-center">
                    By placing your order, you agree to Shopzio's privacy notice and conditions of use.
                  </p>
                </form>
              )}
            </div>
          </div>

          {/* Order summary */}
          <div className="bg-white rounded-sm shadow-sm p-4 w-full lg:w-72 shrink-0">
            {step === 2 && (
              <button type="submit" form="checkout-form"
                onClick={handlePlaceOrder as any}
                disabled={loading}
                className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-bold py-2.5 rounded-full text-sm border border-[#FCD200] mb-4 disabled:opacity-50">
                {loading ? 'Processing...' : 'Place your order'}
              </button>
            )}

            <h2 className="font-bold text-lg text-gray-900 mb-3">Order Summary</h2>
            <div className="space-y-2 text-sm border-b pb-3 mb-3">
              <div className="flex justify-between">
                <span className="text-gray-600">Items ({cart.totalItems}):</span>
                <span>₹{cart.total.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Delivery:</span>
                <span className="text-[#007600]">FREE</span>
              </div>
            </div>
            <div className="flex justify-between font-bold text-lg text-red-700">
              <span>Order Total:</span>
              <span>₹{cart.total.toFixed(2)}</span>
            </div>

            <div className="mt-4 text-xs text-gray-500 space-y-2">
              {cart.items.slice(0, 3).map((item) => (
                <div key={item.cartItemId} className="flex gap-2">
                  <img src={item.imageUrl} alt={item.productName}
                    className="w-10 h-10 object-contain bg-gray-50 rounded shrink-0" />
                  <p className="truncate">{item.productName}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
