import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { productApi } from '../api'
import ProductCard from '../components/ProductCard'
import type { Product, ProductPage } from '../types'

const SORT_OPTIONS = [
  { label: 'Featured', value: 'rating,desc' },
  { label: 'Price: Low to High', value: 'price,asc' },
  { label: 'Price: High to Low', value: 'price,desc' },
  { label: 'Avg. Customer Review', value: 'rating,desc' },
  { label: 'Newest Arrivals', value: 'createdAt,desc' },
]

export default function Products() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [data, setData] = useState<ProductPage | null>(null)
  const [categories, setCategories] = useState<string[]>([])
  const [loading, setLoading] = useState(true)
  const [sort, setSort] = useState('rating,desc')

  const category = searchParams.get('category') || ''
  const keyword = searchParams.get('keyword') || ''
  const page = parseInt(searchParams.get('page') || '0')

  useEffect(() => {
    productApi.getCategories().then((r) => setCategories(r.data))
  }, [])

  useEffect(() => {
    setLoading(true)
    const [sortBy, sortDir] = sort.split(',')
    const fetch = async () => {
      try {
        let res
        if (keyword) res = await productApi.search(keyword, page)
        else if (category) res = await productApi.getByCategory(category, page)
        else res = await productApi.getAll(page, 12, sortBy, sortDir)
        setData(res.data)
      } finally {
        setLoading(false)
      }
    }
    fetch()
  }, [category, keyword, page, sort])

  const setCategory = (cat: string) => setSearchParams(cat ? { category: cat } : {})

  const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const kw = (e.currentTarget.elements.namedItem('keyword') as HTMLInputElement).value.trim()
    setSearchParams(kw ? { keyword: kw } : {})
  }

  const title = keyword ? `Results for "${keyword}"` : category ? category : 'All Products'

  return (
    <div className="max-w-[1500px] mx-auto px-4 py-4">
      <div className="flex gap-4">

        {/* Sidebar */}
        <aside className="hidden md:block w-56 shrink-0">
          <div className="bg-white p-4 rounded-sm shadow-sm">
            <h3 className="font-bold text-gray-900 mb-3 text-base border-b pb-2">Department</h3>
            <ul className="space-y-1">
              <li>
                <button onClick={() => setCategory('')}
                  className={`w-full text-left px-2 py-1 text-sm transition-colors ${!category ? 'font-bold text-gray-900' : 'text-[#007185] hover:text-[#C7511F] hover:underline'}`}>
                  Any Department
                </button>
              </li>
              {categories.map((cat) => (
                <li key={cat}>
                  <button onClick={() => setCategory(cat)}
                    className={`w-full text-left px-2 py-1 text-sm transition-colors ${category === cat ? 'font-bold text-gray-900' : 'text-[#007185] hover:text-[#C7511F] hover:underline'}`}>
                    {cat}
                  </button>
                </li>
              ))}
            </ul>
          </div>

          {/* Prime filter box */}
          <div className="bg-white p-4 rounded-sm shadow-sm mt-3">
            <h3 className="font-bold text-gray-900 mb-3 text-base border-b pb-2">Avg. Customer Review</h3>
            {[4, 3, 2, 1].map((stars) => (
              <div key={stars} className="flex items-center gap-2 py-1 cursor-pointer group">
                <div className="flex text-[#FF9900] text-xs">
                  {Array.from({ length: 5 }).map((_, i) => (
                    <span key={i}>{i < stars ? '★' : '☆'}</span>
                  ))}
                </div>
                <span className="text-[#007185] text-xs group-hover:text-[#C7511F] group-hover:underline">& Up</span>
              </div>
            ))}
          </div>
        </aside>

        {/* Main content */}
        <div className="flex-1 min-w-0">
            {/* Results header */}
          <div className="bg-white px-4 py-3 rounded-sm shadow-sm mb-3 flex flex-col sm:flex-row sm:items-center justify-between gap-2">
            <div>
              <h1 className="text-lg font-medium text-gray-900">{title}</h1>
              {data && (
                <p className="text-xs text-gray-500">
                  1-{Math.min(data.content.length, data.totalElements)} of {data.totalElements.toLocaleString()} results
                </p>
              )}
            </div>
            <div className="flex items-center gap-2 text-sm">
              <span className="text-gray-600 shrink-0">Sort by:</span>
              <select value={sort} onChange={(e) => setSort(e.target.value)}
                className="border border-gray-300 rounded-sm px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-[#FF9900]">
                {SORT_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
              </select>
            </div>
          </div>

          {/* Product grid */}
          {loading ? (
            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
              {Array.from({ length: 8 }).map((_, i) => (
                <div key={i} className="bg-white animate-pulse">
                  <div className="aspect-square bg-gray-200" />
                  <div className="p-3 space-y-2">
                    <div className="h-3 bg-gray-200 rounded" />
                    <div className="h-3 bg-gray-200 rounded w-2/3" />
                    <div className="h-4 bg-gray-200 rounded w-1/2" />
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <>
              {data?.content.length === 0 ? (
                <div className="bg-white p-10 text-center rounded-sm">
                  <p className="text-gray-500">No results found for <strong>"{keyword || category}"</strong></p>
                  <p className="text-gray-400 text-sm mt-1">Try checking your spelling or use more general terms</p>
                </div>
              ) : (
                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
                  {data?.content.map((p: Product) => <ProductCard key={p.id} product={p} />)}
                </div>
              )}

              {/* Pagination */}
              {data && data.totalPages > 1 && (
                <div className="flex justify-center gap-1 mt-6">
                  {Array.from({ length: data.totalPages }).map((_, i) => (
                    <button key={i}
                      onClick={() => setSearchParams({ ...(category && { category }), ...(keyword && { keyword }), page: String(i) })}
                      className={`w-9 h-9 rounded-sm text-sm border transition-colors ${
                        i === page
                          ? 'bg-[#232F3E] text-white border-[#232F3E]'
                          : 'bg-white border-gray-300 hover:border-[#FF9900] text-[#007185]'
                      }`}>
                      {i + 1}
                    </button>
                  ))}
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}
