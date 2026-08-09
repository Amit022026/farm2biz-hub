import { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import OrderSlip from '../components/OrderSlip';
import EmptyState from '../components/EmptyState';
import { SkeletonRow } from '../components/Skeleton';

export default function IncomingOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    const data = await orderService.farmerOrders();
    setOrders(data);
    setLoading(false);
  };

  useEffect(() => { load(); }, []);

  const handleAccept = async (id) => {
    setError('');
    try {
      await orderService.accept(id);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not accept order.');
    }
  };

  const handleReject = async (id) => {
    if (!window.confirm('Reject this order? Stock will be restored.')) return;
    setError('');
    try {
      await orderService.reject(id);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not reject order.');
    }
  };

  return (
    <div className="container">
      <div className="page-header"><h1>Incoming Orders</h1></div>
      {error && <div className="alert alert-error">{error}</div>}

      {loading ? (
        <div className="stack-gap">{[1, 2, 3].map((i) => <SkeletonRow key={i} />)}</div>
      ) : orders.length === 0 ? (
        <EmptyState title="No orders yet" subtitle="Orders for your products will show up here." />
      ) : (
        <div className="slip-grid">
          {orders.map((o) => (
            <OrderSlip
              key={o.orderId}
              order={o}
              actions={
                o.status === 'PENDING' ? (
                  <>
                    <button className="btn btn-sm btn-accent" onClick={() => handleAccept(o.orderId)}>Accept</button>
                    <button className="btn btn-sm btn-danger-outline" onClick={() => handleReject(o.orderId)}>Reject</button>
                  </>
                ) : null
              }
            />
          ))}
        </div>
      )}
    </div>
  );
}
