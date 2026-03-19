// ── Auth ──────────────────────────────────────────────────────────────────────
export interface AuthResponse {
  token: string
  type: string
  id: number
  firstName: string
  lastName: string
  email: string
  role: 'CUSTOMER' | 'ADMIN'
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  firstName: string
  lastName: string
  email: string
  password: string
  phone?: string
}

// ── User ──────────────────────────────────────────────────────────────────────
export interface User {
  id: number
  firstName: string
  lastName: string
  email: string
  phone?: string
  address?: string
  role: 'CUSTOMER' | 'ADMIN'
  createdAt: string
}

export interface UpdateProfileRequest {
  firstName: string
  lastName: string
  phone?: string
  address?: string
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
  confirmPassword: string
}

// ── Product ───────────────────────────────────────────────────────────────────
export interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  imageUrl: string
  category: string
  rating: number
  reviewCount: number
  active: boolean
  createdAt: string
}

export interface ProductPage {
  content: Product[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// ── Cart ──────────────────────────────────────────────────────────────────────
export interface CartItem {
  cartItemId: number
  productId: number
  productName: string
  imageUrl: string
  unitPrice: number
  quantity: number
  subtotal: number
}

export interface Cart {
  cartId: number
  items: CartItem[]
  totalItems: number
  total: number
}

// ── Order ─────────────────────────────────────────────────────────────────────
export interface OrderItem {
  orderItemId: number
  productId: number
  productName: string
  imageUrl: string
  quantity: number
  unitPrice: number
  subtotal: number
}

export interface Order {
  id: number
  status: 'PENDING' | 'CONFIRMED' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED'
  totalAmount: number
  shippingAddress: string
  paymentMethod: string
  paymentStatus: string
  items: OrderItem[]
  createdAt: string
}

// ── Payment ───────────────────────────────────────────────────────────────────
export interface RazorpayOrderResponse {
  razorpayOrderId: string   // order_XXXXXXXXXX — passed to Razorpay popup
  keyId: string             // rzp_test_XXXX — used to init Razorpay
  amount: number
  currency: string
  internalOrderId: number
}

export interface PaymentVerificationRequest {
  razorpayOrderId: string
  razorpayPaymentId: string
  razorpaySignature: string
  internalOrderId?: number
}
