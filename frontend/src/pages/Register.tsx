import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { authApi } from '../api'
import { setCredentials } from '../store/authSlice'

export default function Register() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', password: '', phone: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authApi.register(form)
      dispatch(setCredentials(res.data))
      navigate('/')
    } catch (err: any) {
      setError(err.response?.data?.message || 'Registration failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const f = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm({ ...form, [field]: e.target.value })

  return (
    <div className="min-h-screen bg-white flex flex-col items-center pt-6 px-4 pb-10">
      {/* Logo */}
      <Link to="/" className="mb-5">
        <span className="text-gray-900 font-extrabold text-3xl">shop</span>
        <span className="text-[#FF9900] font-extrabold text-3xl">zio</span>
      </Link>

      <div className="w-full max-w-sm border border-gray-300 rounded-sm p-6">
        <h1 className="text-2xl font-medium text-gray-900 mb-1">Create account</h1>

        {error && (
          <div className="bg-red-50 border border-red-400 text-red-700 text-sm px-3 py-2 rounded-sm mb-4 flex gap-2">
            <span>⚠</span> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-3 mt-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-bold text-gray-900 mb-1">First name</label>
              <input type="text" className="input" value={form.firstName} onChange={f('firstName')} required />
            </div>
            <div>
              <label className="block text-xs font-bold text-gray-900 mb-1">Last name</label>
              <input type="text" className="input" value={form.lastName} onChange={f('lastName')} required />
            </div>
          </div>
          <div>
            <label className="block text-xs font-bold text-gray-900 mb-1">Email</label>
            <input type="email" className="input" value={form.email} onChange={f('email')} required />
          </div>
          <div>
            <label className="block text-xs font-bold text-gray-900 mb-1">Mobile number (optional)</label>
            <input type="tel" className="input" value={form.phone} onChange={f('phone')} />
          </div>
          <div>
            <label className="block text-xs font-bold text-gray-900 mb-1">Password</label>
            <input type="password" className="input" value={form.password} onChange={f('password')} required minLength={6} />
            <p className="text-xs text-gray-500 mt-1">At least 6 characters</p>
          </div>

          <button type="submit" disabled={loading}
            className="w-full bg-[#FFD814] hover:bg-[#F7CA00] text-gray-900 font-medium py-2 rounded-sm border border-[#FCD200] text-sm disabled:opacity-50 transition-colors mt-2">
            {loading ? 'Creating account...' : 'Continue'}
          </button>
        </form>

        <p className="text-xs text-gray-600 mt-4 leading-relaxed">
          By creating an account, you agree to Shopzio's{' '}
          <span className="text-[#007185] hover:underline cursor-pointer">Conditions of Use</span>
          {' '}and{' '}
          <span className="text-[#007185] hover:underline cursor-pointer">Privacy Notice</span>.
        </p>
      </div>

      <div className="w-full max-w-sm mt-4 text-center text-sm">
        <span className="text-gray-600">Already have an account? </span>
        <Link to="/login" className="text-[#007185] hover:text-[#C7511F] hover:underline font-medium">
          Sign in →
        </Link>
      </div>
    </div>
  )
}
