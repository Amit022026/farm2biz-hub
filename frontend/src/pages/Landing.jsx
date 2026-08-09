import { useEffect, useMemo, useState } from 'react';
import { productService } from '../services/productService';
import { categoryService } from '../services/categoryService';
import ProductCard from '../components/ProductCard';
import CategoryChips from '../components/CategoryChips';
import { SkeletonGrid } from '../components/Skeleton';
import EmptyState from '../components/EmptyState';
import { SearchIcon, LeafIcon, TruckIcon, CheckIcon } from '../components/Icons';

export default function Landing() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([productService.listAll(), categoryService.listAll()])
      .then(([p, c]) => { setProducts(p); setCategories(c); })
      .catch(() => setError('Could not load the catalog. Is the backend running?'))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    return products.filter((p) => {
      const matchesKeyword = keyword.trim() === '' || p.name.toLowerCase().includes(keyword.toLowerCase());
      const matchesCategory = categoryId === '' || String(p.categoryId) === categoryId;
      return matchesKeyword && matchesCategory;
    });
  }, [products, keyword, categoryId]);

  return (
    <div>
      <section className="hero">
        <div className="hero-blob hero-blob-1" />
        <div className="hero-blob hero-blob-2" />
        <div className="container hero-inner">
          <span className="hero-eyebrow"><LeafIcon size={14} /> Farm-direct marketplace</span>
          <h1>Fresh produce,<br />straight from the source.</h1>
          <p>Buy directly from verified local farmers — better prices, zero middlemen, guaranteed freshness.</p>

          <div className="hero-search">
            <SearchIcon size={19} />
            <input
              placeholder="Search for tomatoes, rice, mangoes..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
          </div>

          <div className="hero-trust">
            <span><CheckIcon size={15} /> Verified farmers</span>
            <span><TruckIcon size={15} /> Direct delivery</span>
            <span><CheckIcon size={15} /> Fair pricing</span>
          </div>
        </div>
      </section>

      <div className="container">
        {!loading && categories.length > 0 && (
          <CategoryChips categories={categories} selected={categoryId} onSelect={setCategoryId} />
        )}

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <SkeletonGrid count={8} />
        ) : filtered.length === 0 ? (
          <EmptyState
            title="No products match your search"
            subtitle="Try a different keyword or clear the category filter."
          />
        ) : (
          <>
            <div className="section-heading">
              <h2>{categoryId ? categories.find((c) => String(c.categoryId) === categoryId)?.name : 'All produce'}</h2>
              <span className="muted">{filtered.length} items</span>
            </div>
            <div className="grid">
              {filtered.map((p) => <ProductCard key={p.productId} product={p} />)}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
