import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { authApi } from '../api'
import { setCredentials } from '../store/authSlice'

export default function Login() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authApi.login(form)
      dispatch(setCredentials(res.data))
      navigate('/')
    } catch (err: any) {
      setError(err.response?.data?.message || 'Invalid email or password')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-white flex flex-col items-center pt-6 px-4">
      {/* Logo */}
      <Link to="/" className="mb-5">
        <span className="text-gray-900 font-extrabold text-3xl">shop</span>
        <span className="text-[#FF9900] font-extrabold text-3xl">zio</span>
      </Link>

      <div className="w-full max-w-sm border border-gray-300 rounded-sm p-6">
        <h1 className="text-2xl font-medium text-gray-900 mb-4">Sign in</h1>

        {error && (
          <div className="bg-red-50 border border-red-400 text-red-700 text-sm px-3 py-2 rounded-sm mb-4 flex gap-2">
            <span>⚠</span> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-bold text-gray-900 mb-1">Email</label>
            <input type="email" className="input" value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          </div>
          <div>
            <label className="block text-sm font-bold text-gray-900 mb-1">Password</label>
            <input type="password" className="input" value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          </div>
          <button type="submit" disabled={loading}
            className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium py-2 rounded-sm border border-[#FCD200] text-sm disabled:opacity-50 transition-colors">
            {loading ? 'Signing in...' : 'Continue'}
          </button>
        </form>

        <p className="text-xs text-gray-600 mt-4 leading-relaxed">
          By continuing, you agree to Shopzio's{' '}
          <span className="text-[#007185] hover:text-[#C7511F] hover:underline cursor-pointer">Conditions of Use</span>
          {' '}and{' '}
          <span className="text-[#007185] hover:text-[#C7511F] hover:underline cursor-pointer">Privacy Notice</span>.
        </p>
      </div>

      <div className="w-full max-w-sm mt-6 flex items-center gap-4">
        <div className="h-px flex-1 bg-gray-200" />
        <span className="text-gray-500 text-xs">New to Shopzio?</span>
        <div className="h-px flex-1 bg-gray-200" />
      </div>

      <div className="w-full max-w-sm mt-4">
        <Link to="/register"
          className="block w-full text-center bg-gray-100 hover:bg-gray-200 border border-gray-300 text-gray-900 font-medium py-2 rounded-sm text-sm transition-colors">
          Create your Shopzio account
        </Link>
      </div>
    </div>
  )
}
