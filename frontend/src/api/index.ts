import api from './axios'
import type {
  LoginRequest, RegisterRequest, UpdateProfileRequest,
  ChangePasswordRequest, RazorpayOrderResponse, PaymentVerificationRequest
} from '../types'

// ── Auth ──────────────────────────────────────────────────────────────────────
export const authApi = {
  login: (data: LoginRequest) => api.post('/auth/login', data),
  register: (data: RegisterRequest) => api.post('/auth/register', data),
}

// ── Products ──────────────────────────────────────────────────────────────────
export const productApi = {
  getAll: (page = 0, size = 12, sortBy = 'createdAt', sortDir = 'desc') =>
    api.get(`/products?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
  getById: (id: number) => api.get(`/products/${id}`),
  getByCategory: (category: string, page = 0) =>
    api.get(`/products/category/${encodeURIComponent(category)}?page=${page}`),
  search: (keyword: string, page = 0) =>
    api.get(`/products/search?keyword=${encodeURIComponent(keyword)}&page=${page}`),
  getCategories: () => api.get('/products/categories'),
}

// ── Cart ──────────────────────────────────────────────────────────────────────
export const cartApi = {
  getCart: () => api.get('/cart'),
  addItem: (productId: number, quantity: number) =>
    api.post('/cart/add', { productId, quantity }),
  updateItem: (cartItemId: number, productId: number, quantity: number) =>
    api.put(`/cart/update/${cartItemId}`, { productId, quantity }),
  removeItem: (cartItemId: number) => api.delete(`/cart/remove/${cartItemId}`),
  clearCart: () => api.delete('/cart/clear'),
}

// ── Orders ────────────────────────────────────────────────────────────────────
export const orderApi = {
  placeOrder: (shippingAddress: string, paymentMethod: string) =>
    api.post('/orders', { shippingAddress, paymentMethod }),
  getMyOrders: () => api.get('/orders'),
  getOrderById: (id: number) => api.get(`/orders/${id}`),
  cancelOrder: (id: number) => api.put(`/orders/${id}/cancel`),
}

// ── Payments ──────────────────────────────────────────────────────────────────
export const paymentApi = {
  createOrder: (amount: number, currency = 'INR', orderId?: number) =>
    api.post<RazorpayOrderResponse>('/payments/create-order', { amount, currency, orderId }),
  verifyPayment: (data: PaymentVerificationRequest) =>
    api.post<string>('/payments/verify', data),
}

// ── Users ─────────────────────────────────────────────────────────────────────
export const userApi = {
  getProfile: () => api.get('/users/profile'),
  updateProfile: (data: UpdateProfileRequest) => api.put('/users/profile', data),
  changePassword: (data: ChangePasswordRequest) => api.put('/users/password', data),
}
