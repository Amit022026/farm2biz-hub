import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { productService } from '../services/productService';
import { categoryService } from '../services/categoryService';
import { useAuth } from '../hooks/useAuth';
import ProductImage from '../components/ProductImage';
import EmptyState from '../components/EmptyState';
import { SkeletonGrid } from '../components/Skeleton';
import { RupeeIcon } from '../components/Icons';

export default function FarmerDashboard() {
  const { user } = useAuth();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [editing, setEditing] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const { register, handleSubmit, reset, watch, formState: { errors } } = useForm();
  const previewUrl = watch('imageUrl');

  const load = async () => {
    const [allProducts, cats] = await Promise.all([productService.listAll(), categoryService.listAll()]);
    setProducts(allProducts.filter((p) => p.farmerId === user.userId));
    setCategories(cats);
    setLoading(false);
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const openCreate = () => {
    setEditing(null);
    reset({ name: '', price: '', unit: 'kg', quantityAvailable: '', imageUrl: '', categoryId: categories[0]?.categoryId || '' });
    setShowForm(true);
  };

  const openEdit = (product) => {
    setEditing(product);
    reset(product);
    setShowForm(true);
  };

  const onSubmit = async (data) => {
    setError('');
    try {
      const payload = {
        ...data,
        price: Number(data.price),
        quantityAvailable: Number(data.quantityAvailable),
        categoryId: Number(data.categoryId),
        farmerId: user.userId,
      };
      if (editing) {
        await productService.update(editing.productId, payload);
      } else {
        await productService.create(payload);
      }
      setShowForm(false);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save product.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Remove this product listing?')) return;
    await productService.remove(id);
    load();
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>My Products</h1>
        <button className="btn btn-accent" onClick={openCreate}>+ Add Product</button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {showForm && (
        <div className="card form-inline-card glass-card">
          <h3>{editing ? 'Edit product' : 'New product'}</h3>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="field-row">
              <div className="field" style={{ flex: 2 }}>
                <label>Name</label>
                <input {...register('name', { required: 'Name is required' })} />
                {errors.name && <div className="error">{errors.name.message}</div>}
              </div>
              <div className="field">
                <label>Category</label>
                <select {...register('categoryId', { required: true })}>
                  {categories.map((c) => (
                    <option key={c.categoryId} value={c.categoryId}>{c.name}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="field-row">
              <div className="field" style={{ flex: 2 }}>
                <label>Image URL (optional)</label>
                <input placeholder="https://..." {...register('imageUrl')} />
              </div>
              <div className="image-preview-slot">
                <ProductImage src={previewUrl} alt="preview" seed={editing?.productId || 'new'} />
              </div>
            </div>

            <div className="field-row">
              <div className="field">
                <label>Price (per unit)</label>
                <input type="number" step="0.01" {...register('price', { required: true, min: 0.01 })} />
              </div>
              <div className="field">
                <label>Unit</label>
                <select {...register('unit')}>
                  <option value="kg">kg</option>
                  <option value="ton">ton</option>
                  <option value="dozen">dozen</option>
                  <option value="litre">litre</option>
                </select>
              </div>
              <div className="field">
                <label>Quantity available</label>
                <input type="number" step="0.01" {...register('quantityAvailable', { required: true, min: 0 })} />
              </div>
            </div>

            <button className="btn btn-accent" type="submit">{editing ? 'Save changes' : 'Create product'}</button>{' '}
            <button className="btn btn-outline" type="button" onClick={() => setShowForm(false)}>Cancel</button>
          </form>
        </div>
      )}

      {loading ? (
        <SkeletonGrid count={4} />
      ) : products.length === 0 ? (
        <EmptyState title="No products listed yet" subtitle="Add your first product to start selling." />
      ) : (
        <div className="grid grid-tight">
          {products.map((p) => (
            <div key={p.productId} className="product-card manage-card">
              <div className="product-card-media">
                <ProductImage src={p.imageUrl} alt={p.name} seed={p.productId} />
              </div>
              <div className="product-card-body">
                <h3>{p.name}</h3>
                <p className="muted">{p.categoryName}</p>
                <p className="price"><RupeeIcon size={14} />{p.price}<small>/{p.unit}</small></p>
                <p className="muted">{p.quantityAvailable} {p.unit} in stock</p>
                <div className="manage-card-actions">
                  <button className="btn btn-sm btn-outline" onClick={() => openEdit(p)}>Edit</button>
                  <button className="btn btn-sm btn-danger-outline" onClick={() => handleDelete(p.productId)}>Delete</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
