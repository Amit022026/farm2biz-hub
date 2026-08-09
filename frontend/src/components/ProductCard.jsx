import { Link } from 'react-router-dom';
import ProductImage from './ProductImage';
import { RupeeIcon } from './Icons';

export default function ProductCard({ product }) {
  const outOfStock = Number(product.quantityAvailable) <= 0;
  const lowStock = !outOfStock && Number(product.quantityAvailable) < 10;

  return (
    <Link to={`/products/${product.productId}`} className="product-card">
      <div className="product-card-media">
        <ProductImage src={product.imageUrl} alt={product.name} seed={product.productId} />
        <span className="chip chip-glass">{product.categoryName || 'Uncategorized'}</span>
        {outOfStock && <span className="chip chip-danger stock-flag">Out of stock</span>}
        {lowStock && <span className="chip chip-warning stock-flag">Only {product.quantityAvailable} left</span>}
      </div>
      <div className="product-card-body">
        <h3>{product.name}</h3>
        <p className="muted product-farmer">by {product.farmerName}</p>
        <div className="product-card-footer">
          <span className="price"><RupeeIcon size={14} />{product.price}<small>/{product.unit}</small></span>
          <span className="add-hint">View →</span>
        </div>
      </div>
    </Link>
  );
}
