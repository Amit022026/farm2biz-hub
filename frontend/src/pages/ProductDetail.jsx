import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { productService } from '../services/productService';
import { orderService } from '../services/orderService';
import { useAuth } from '../hooks/useAuth';
import ProductImage from '../components/ProductImage';
import { RupeeIcon, TruckIcon } from '../components/Icons';

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [deliveryAddress, setDeliveryAddress] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [placing, setPlacing] = useState(false);

  useEffect(() => {
    productService.getById(id).then(setProduct).catch(() => setError('Product not found.'));
  }, [id]);

  const step = (delta) => {
    setQuantity((q) => {
      const next = Number(q) + delta;
      const max = Number(product?.quantityAvailable || 0);
      return Math.min(Math.max(next, 0.5), max);
    });
  };

  const handlePlaceOrder = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');

    if (!user) {
      navigate('/login', { state: { from: { pathname: `/products/${id}` } } });
      return;
    }
    if (user.role !== 'BULK_BUYER') {
      setError('Only buyer accounts can place orders.');
      return;
    }

    setPlacing(true);
    try {
      const order = await orderService.place({
        productId: Number(id),
        quantity: Number(quantity),
        deliveryAddress,
      });
      setMessage(`Order ORD-${String(order.orderId).padStart(5, '0')} placed! Redirecting...`);
      setTimeout(() => navigate('/my-orders'), 1200);
    } catch (err) {
      setError(err.response?.data?.message || 'Could not place order.');
    } finally {
      setPlacing(false);
    }
  };

  if (error && !product) {
    return <div className="container"><div className="alert alert-error">{error}</div></div>;
  }
  if (!product) {
    return (
      <div className="container">
        <div className="detail-layout">
          <div className="skeleton skeleton-img" style={{ height: 380, borderRadius: 'var(--radius-lg)' }} />
          <div>
            <div className="skeleton skeleton-line" style={{ width: '30%' }} />
            <div className="skeleton skeleton-line" style={{ width: '60%', height: 28, marginTop: 12 }} />
            <div className="skeleton skeleton-line" style={{ width: '40%', marginTop: 12 }} />
          </div>
        </div>
      </div>
    );
  }

  const outOfStock = Number(product.quantityAvailable) <= 0;

  return (
    <div className="container">
      <Link to="/" className="back-link">← Back to catalog</Link>

      <div className="detail-layout">
        <div className="detail-image-wrap">
          <ProductImage src={product.imageUrl} alt={product.name} seed={product.productId} className="detail-image" />
        </div>

        <div className="detail-info glass-card">
          <span className="chip chip-muted">{product.categoryName || 'Uncategorized'}</span>
          <h1>{product.name}</h1>
          <p className="muted">Sold by <strong>{product.farmerName}</strong></p>
          <p className="price-large"><RupeeIcon size={22} />{product.price} <small>/ {product.unit}</small></p>
          <p className="muted">
            {outOfStock ? 'Out of stock' : `${product.quantityAvailable} ${product.unit} available`}
          </p>

          {error && <div className="alert alert-error">{error}</div>}
          {message && <div className="alert alert-success">{message}</div>}

          {!outOfStock && (
            <form onSubmit={handlePlaceOrder} className="order-form">
              <div className="field">
                <label>Quantity ({product.unit})</label>
                <div className="stepper">
                  <button type="button" onClick={() => step(-1)}>−</button>
                  <input
                    type="number"
                    min="0.5"
                    step="0.5"
                    max={product.quantityAvailable}
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                  />
                  <button type="button" onClick={() => step(1)}>+</button>
                </div>
              </div>
              <div className="field">
                <label><TruckIcon size={14} /> Delivery address</label>
                <input
                  placeholder="Where should this be delivered?"
                  value={deliveryAddress}
                  onChange={(e) => setDeliveryAddress(e.target.value)}
                  required
                />
              </div>
              <div className="order-summary-line">
                <span>Estimated total</span>
                <strong>₹{(quantity * product.price || 0).toFixed(2)}</strong>
              </div>
              <button className="btn btn-accent btn-block" disabled={placing}>
                {placing ? 'Placing order...' : 'Place Order'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
