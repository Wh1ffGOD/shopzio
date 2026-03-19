import { useEffect, useState } from 'react'
import { userApi } from '../api'
import type { User } from '../types'

export default function Profile() {
  const [user, setUser] = useState<User | null>(null)
  const [form, setForm] = useState({ firstName: '', lastName: '', phone: '', address: '' })
  const [pwForm, setPwForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' })
  const [profileMsg, setProfileMsg] = useState('')
  const [pwMsg, setPwMsg] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    userApi.getProfile().then((r) => {
      setUser(r.data)
      setForm({
        firstName: r.data.firstName,
        lastName: r.data.lastName,
        phone: r.data.phone || '',
        address: r.data.address || '',
      })
    })
  }, [])

  const updateProfile = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      const res = await userApi.updateProfile(form)
      setUser(res.data)
      setProfileMsg('Profile updated successfully!')
      setTimeout(() => setProfileMsg(''), 3000)
    } catch {
      setProfileMsg('Failed to update profile.')
    } finally {
      setLoading(false)
    }
  }

  const changePassword = async (e: React.FormEvent) => {
    e.preventDefault()
    if (pwForm.newPassword !== pwForm.confirmPassword) {
      setPwMsg('Passwords do not match')
      return
    }
    setLoading(true)
    try {
      await userApi.changePassword(pwForm)
      setPwMsg('Password changed successfully!')
      setPwForm({ currentPassword: '', newPassword: '', confirmPassword: '' })
      setTimeout(() => setPwMsg(''), 3000)
    } catch (err: any) {
      setPwMsg(err.response?.data?.message || 'Failed to change password.')
    } finally {
      setLoading(false)
    }
  }

  if (!user) return <div className="text-center py-20 text-gray-500">Loading profile...</div>

  return (
    <div className="max-w-2xl mx-auto px-4 py-10 sm:px-6">
      <h1 className="text-2xl font-bold text-gray-900 mb-8">My Profile</h1>

      {/* Profile Info */}
      <div className="card p-6 mb-6">
        <h2 className="font-semibold text-gray-900 mb-4">Personal Information</h2>
        <form onSubmit={updateProfile} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
              <input className="input" value={form.firstName}
                onChange={(e) => setForm({ ...form, firstName: e.target.value })} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
              <input className="input" value={form.lastName}
                onChange={(e) => setForm({ ...form, lastName: e.target.value })} required />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input className="input bg-gray-50" value={user.email} disabled />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input className="input" value={form.phone}
              onChange={(e) => setForm({ ...form, phone: e.target.value })} />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Address</label>
            <input className="input" value={form.address}
              onChange={(e) => setForm({ ...form, address: e.target.value })} />
          </div>

          {profileMsg && (
            <p className={`text-sm ${profileMsg.includes('success') ? 'text-green-600' : 'text-red-500'}`}>
              {profileMsg}
            </p>
          )}

          <button type="submit" disabled={loading} className="btn-primary">
            Save Changes
          </button>
        </form>
      </div>

      {/* Change Password */}
      <div className="card p-6">
        <h2 className="font-semibold text-gray-900 mb-4">Change Password</h2>
        <form onSubmit={changePassword} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
            <input type="password" className="input" value={pwForm.currentPassword}
              onChange={(e) => setPwForm({ ...pwForm, currentPassword: e.target.value })} required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">New Password</label>
            <input type="password" className="input" value={pwForm.newPassword}
              onChange={(e) => setPwForm({ ...pwForm, newPassword: e.target.value })} required minLength={6} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
            <input type="password" className="input" value={pwForm.confirmPassword}
              onChange={(e) => setPwForm({ ...pwForm, confirmPassword: e.target.value })} required />
          </div>

          {pwMsg && (
            <p className={`text-sm ${pwMsg.includes('success') ? 'text-green-600' : 'text-red-500'}`}>
              {pwMsg}
            </p>
          )}

          <button type="submit" disabled={loading} className="btn-primary">
            Change Password
          </button>
        </form>
      </div>
    </div>
  )
}
