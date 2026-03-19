import { useEffect, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { orderApi } from '../api'
import type { Order } from '../types'

const STATUS_CONFIG: Record<string, { label: string; color: string; step: number }> = {
  PENDING:    { label: 'Order Placed',  color: 'text-yellow-600', step: 1 },
  CONFIRMED:  { label: 'Confirmed',     color: 'text-blue-600',   step: 2 },
  PROCESSING: { label: 'Processing',    color: 'text-purple-600', step: 2 },
  SHIPPED:    { label: 'Shipped',       color: 'text-indigo-600', step: 3 },
  DELIVERED:  { label: 'Delivered',     color: 'text-green-700',  step: 4 },
  CANCELLED:  { label: 'Cancelled',     color: 'text-red-600',    step: 0 },
}

function ProgressBar({ status }: { status: string }) {
  const cfg = STATUS_CONFIG[status] || { step: 0 }
  if (status === 'CANCELLED') return (
    <div className="flex items-center gap-2 text-red-600 text-xs font-medium">
      <span>✕</span> Order Cancelled
    </div>
  )
  const steps = ['Order Placed', 'Processing', 'Shipped', 'Delivered']
  return (
    <div className="flex items-center gap-1 mt-2">
      {steps.map((s, i) => (
        <div key={s} className="flex items-center">
          <div className={`flex flex-col items-center`}>
            <div className={`w-5 h-5 rounded-full flex items-center justify-center text-xs font-bold ${
              i + 1 <= cfg.step ? 'bg-[#FF9900] text-white' : 'bg-gray-200 text-gray-400'
            }`}>
              {i + 1 <= cfg.step ? '✓' : i + 1}
            </div>
            <span className={`text-xs mt-1 hidden sm:block ${i + 1 <= cfg.step ? 'text-[#FF9900] font-medium' : 'text-gray-400'}`}>
              {s}
            </span>
          </div>
          {i < steps.length - 1 && (
            <div className={`h-0.5 w-8 sm:w-16 mx-1 mb-4 ${i + 1 < cfg.step ? 'bg-[#FF9900]' : 'bg-gray-200'}`} />
          )}
        </div>
      ))}
    </div>
  )
}

export default function Orders() {
  const location = useLocation()
  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)
  const [expanded, setExpanded] = useState<number | null>(null)
  const success = location.state?.success
  const successOrderId = location.state?.orderId

  useEffect(() => {
    orderApi.getMyOrders()
      .then((r) => {
        setOrders(r.data)
        if (successOrderId) setExpanded(successOrderId)
      })
      .finally(() => setLoading(false))
  }, [])

  const handleCancel = async (id: number) => {
    if (!confirm('Cancel this order?')) return
    try {
      const res = await orderApi.cancelOrder(id)
      setOrders((prev) => prev.map((o) => (o.id === id ? res.data : o)))
    } catch (err: any) {
      alert(err.response?.data?.message || 'Cannot cancel this order')
    }
  }

  if (loading) return (
    <div className="max-w-4xl mx-auto px-4 py-10">
      <div className="animate-pulse space-y-4">
        {Array.from({ length: 3 }).map((_, i) => (
          <div key={i} className="bg-white p-4 rounded-sm">
            <div className="h-4 bg-gray-200 rounded w-1/3 mb-2" />
            <div className="h-3 bg-gray-200 rounded w-1/4" />
          </div>
        ))}
      </div>
    </div>
  )

  return (
    <div className="max-w-4xl mx-auto px-4 py-6">
      <h1 className="text-2xl font-medium text-gray-900 mb-4">Your Orders</h1>

      {success && (
        <div className="bg-[#DFF0D8] border border-[#82C462] text-[#3C763D] px-4 py-3 rounded-sm mb-4 flex items-center gap-2">
          <span className="text-lg">✓</span>
          <div>
            <p className="font-medium">Order placed, thank you!</p>
            <p className="text-sm">Confirmation will be sent to your email.</p>
          </div>
        </div>
      )}

      {orders.length === 0 ? (
        <div className="bg-white p-10 rounded-sm text-center">
          <div className="text-5xl mb-4">📦</div>
          <h2 className="text-lg font-medium mb-1">No orders yet</h2>
          <p className="text-gray-500 text-sm mb-4">You haven't placed any orders yet.</p>
          <Link to="/products" className="bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium px-6 py-2 rounded-full text-sm border border-[#FCD200]">
            Start Shopping
          </Link>
        </div>
      ) : (
        <div className="space-y-3">
          {orders.map((order) => {
            const cfg = STATUS_CONFIG[order.status] || { label: order.status, color: 'text-gray-600', step: 0 }
            return (
              <div key={order.id} className="bg-white rounded-sm shadow-sm overflow-hidden border border-gray-200">
                {/* Order header */}
                <div className="bg-gray-50 px-4 py-2 flex flex-wrap items-center justify-between gap-2 border-b border-gray-200">
                  <div className="flex gap-6 text-xs text-gray-600">
                    <div>
                      <p className="uppercase font-medium text-gray-500">Order Placed</p>
                      <p className="font-medium text-gray-900">{new Date(order.createdAt).toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' })}</p>
                    </div>
                    <div>
                      <p className="uppercase font-medium text-gray-500">Total</p>
                      <p className="font-medium text-gray-900">₹{order.totalAmount.toFixed(2)}</p>
                    </div>
                    <div>
                      <p className="uppercase font-medium text-gray-500">Ship To</p>
                      <p className="font-medium text-gray-900 max-w-[120px] truncate">{order.shippingAddress.split(',')[0]}</p>
                    </div>
                  </div>
                  <div className="text-xs text-gray-500">
                    <span>ORDER # {order.id}</span>
                  </div>
                </div>

                {/* Order body */}
                <div className="p-4">
                  <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4">
                    <div className="flex-1">
                      <p className={`text-lg font-bold ${cfg.color} mb-1`}>{cfg.label}</p>
                      <ProgressBar status={order.status} />

                      {/* Items preview */}
                      <div className="flex gap-3 mt-4 flex-wrap">
                        {order.items.slice(0, 3).map((item) => (
                          <Link key={item.orderItemId} to={`/products/${item.productId}`}>
                            <img src={item.imageUrl} alt={item.productName}
                              className="w-16 h-16 object-contain bg-gray-50 border border-gray-200 rounded hover:border-[#FF9900]"
                              onError={(e) => (e.currentTarget.src = 'https://via.placeholder.com/100?text=No+Image')} />
                          </Link>
                        ))}
                        {order.items.length > 3 && (
                          <div className="w-16 h-16 bg-gray-100 border border-gray-200 rounded flex items-center justify-center text-xs text-gray-500">
                            +{order.items.length - 3} more
                          </div>
                        )}
                      </div>
                    </div>

                    {/* Actions */}
                    <div className="flex flex-col gap-2 shrink-0">
                      <button
                        onClick={() => setExpanded(expanded === order.id ? null : order.id)}
                        className="text-sm text-[#007185] hover:text-[#C7511F] hover:underline text-left">
                        {expanded === order.id ? 'Hide details ▲' : 'View order details ▼'}
                      </button>
                      {(order.status === 'PENDING' || order.status === 'PROCESSING') && (
                        <button onClick={() => handleCancel(order.id)}
                          className="text-sm text-red-600 hover:underline text-left">
                          Cancel order
                        </button>
                      )}
                      <Link to="/products" className="text-sm text-[#007185] hover:text-[#C7511F] hover:underline">
                        Buy it again
                      </Link>
                    </div>
                  </div>

                  {/* Expanded details */}
                  {expanded === order.id && (
                    <div className="border-t border-gray-100 mt-4 pt-4 space-y-3">
                      {order.items.map((item) => (
                        <div key={item.orderItemId} className="flex gap-3 items-center">
                          <img src={item.imageUrl} alt={item.productName}
                            className="w-12 h-12 object-contain bg-gray-50 border border-gray-200 rounded shrink-0"
                            onError={(e) => (e.currentTarget.src = 'https://via.placeholder.com/100?text=No+Image')} />
                          <div className="flex-1 min-w-0">
                            <p className="text-sm font-medium text-gray-900 truncate">{item.productName}</p>
                            <p className="text-xs text-gray-500">Qty: {item.quantity} · ₹{item.unitPrice.toFixed(2)} each</p>
                          </div>
                          <span className="text-sm font-bold shrink-0">₹{item.subtotal.toFixed(2)}</span>
                        </div>
                      ))}

                      <div className="border-t border-gray-100 pt-3 text-sm text-gray-600 space-y-1">
                        <p><span className="font-medium">Shipping to:</span> {order.shippingAddress}</p>
                        <p><span className="font-medium">Payment:</span> {order.paymentMethod} · <span className={order.paymentStatus === 'PAID' ? 'text-green-600' : 'text-yellow-600'}>{order.paymentStatus}</span></p>
                        <p className="font-bold text-gray-900 text-base mt-2">Order Total: ₹{order.totalAmount.toFixed(2)}</p>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
