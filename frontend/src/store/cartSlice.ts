import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import type { Cart } from '../types'

interface CartState {
  cart: Cart | null
  totalItems: number
}

const initialState: CartState = {
  cart: null,
  totalItems: 0,
}

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    setCart: (state, action: PayloadAction<Cart>) => {
      state.cart = action.payload
      state.totalItems = action.payload.totalItems
    },
    clearCartState: (state) => {
      state.cart = null
      state.totalItems = 0
    },
  },
})

export const { setCart, clearCartState } = cartSlice.actions
export default cartSlice.reducer
