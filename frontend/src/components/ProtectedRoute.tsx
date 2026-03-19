import { Navigate, Outlet } from 'react-router-dom'
import { useSelector } from 'react-redux'
import type { RootState } from '../store/store'

export default function ProtectedRoute() {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth)
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />
}
