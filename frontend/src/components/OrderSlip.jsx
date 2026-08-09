import StatusBadge from './StatusBadge';
import ProductImage from './ProductImage';
import { TruckIcon } from './Icons';

export default function OrderSlip({ order, actions }) {
  return (
    <div className={`order-card status-accent-${order.status}`}>
      <div className="order-card-top">
        <ProductImage
          src={order.productImageUrl}
          alt={order.productName}
          seed={order.productId}
          className="order-thumb"
        />
        <div className="order-card-heading">
          <span className="mono-id">ORD-{String(order.orderId).padStart(5, '0')}</span>
          <h3>{order.productName}</h3>
          <p className="muted">Sold by {order.farmerName}</p>
        </div>
        <StatusBadge status={order.status} />
      </div>

      <div className="order-card-details">
        <div className="order-detail-row">
          <span className="muted">Quantity</span>
          <span>{order.quantity}</span>
        </div>
        <div className="order-detail-row">
          <TruckIcon size={14} />
          <span className="truncate">{order.deliveryAddress}</span>
        </div>
        <div className="order-detail-row">
          <span className="muted">Ordered</span>
          <span>{new Date(order.orderDate).toLocaleDateString(undefined, { day: 'numeric', month: 'short', year: 'numeric' })}</span>
        </div>
      </div>

      <div className="order-card-total">
        <span>Total</span>
        <strong>₹{order.totalAmount}</strong>
      </div>

      {actions && <div className="order-card-actions">{actions}</div>}
    </div>
  );
}
