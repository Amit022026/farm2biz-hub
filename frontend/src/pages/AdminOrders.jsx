import { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import StatusBadge from '../components/StatusBadge';
import EmptyState from '../components/EmptyState';

export default function AdminOrders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => { orderService.allOrders().then(setOrders); }, []);

  return (
    <div className="container">
      <div className="page-header"><h1>All Orders</h1></div>
      {orders.length === 0 ? (
        <EmptyState title="No orders in the system yet" />
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr><th>Order</th><th>Buyer</th><th>Product</th><th>Farmer</th><th>Total</th><th>Status</th><th>Date</th></tr>
            </thead>
            <tbody>
              {orders.map((o) => (
                <tr key={o.orderId}>
                  <td className="mono-small">ORD-{String(o.orderId).padStart(5, '0')}</td>
                  <td>{o.buyerName}</td>
                  <td>{o.productName}</td>
                  <td>{o.farmerName}</td>
                  <td>₹{o.totalAmount}</td>
                  <td><StatusBadge status={o.status} /></td>
                  <td>{new Date(o.orderDate).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
