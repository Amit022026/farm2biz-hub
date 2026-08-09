import { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import { paymentService } from '../services/paymentService';
import OrderSlip from '../components/OrderSlip';
import PaymentSlip from '../components/PaymentSlip';
import PaymentModal from '../components/PaymentModal';
import EmptyState from '../components/EmptyState';
import { SkeletonRow } from '../components/Skeleton';
import { Link } from 'react-router-dom';

export default function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [payments, setPayments] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [payingOrder, setPayingOrder] = useState(null);
  const [submittingPayment, setSubmittingPayment] = useState(false);

  const load = async () => {
    const data = await orderService.myOrders();
    setOrders(data);

    const paid = {};
    await Promise.all(
      data.filter((o) => o.status === 'ACCEPTED').map((o) =>
        paymentService.getForOrder(o.orderId).then((p) => { paid[o.orderId] = p; }).catch(() => {})
      )
    );
    setPayments(paid);
    setLoading(false);
  };

  useEffect(() => { load(); }, []);

  const handleCancel = async (orderId) => {
    if (!window.confirm(`Cancel order ORD-${String(orderId).padStart(5, '0')}?`)) return;
    setError('');
    try {
      await orderService.cancel(orderId);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not cancel order.');
    }
  };

  const handleConfirmPayment = async (method) => {
    setSubmittingPayment(true);
    setError('');
    try {
      await paymentService.pay({ orderId: payingOrder.orderId, method });
      setPayingOrder(null);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Payment failed.');
    } finally {
      setSubmittingPayment(false);
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>My Orders</h1>
      </div>
      {error && <div className="alert alert-error">{error}</div>}

      {loading ? (
        <div className="stack-gap">{[1, 2, 3].map((i) => <SkeletonRow key={i} />)}</div>
      ) : orders.length === 0 ? (
        <EmptyState
          title="No orders yet"
          subtitle="Browse the catalog and place your first order."
          action={<Link to="/" className="btn btn-accent">Browse Catalog</Link>}
        />
      ) : (
        <div className="slip-grid">
          {orders.map((o) => {
            const payment = payments[o.orderId];
            return (
              <div key={o.orderId} className="stack-gap-sm">
                <OrderSlip
                  order={o}
                  actions={
                    <>
                      {o.status === 'PENDING' && (
                        <button className="btn btn-sm btn-danger-outline" onClick={() => handleCancel(o.orderId)}>
                          Cancel
                        </button>
                      )}
                      {o.status === 'ACCEPTED' && !payment && (
                        <button className="btn btn-sm btn-accent" onClick={() => setPayingOrder(o)}>
                          Pay Now
                        </button>
                      )}
                    </>
                  }
                />
                {payment && <PaymentSlip payment={payment} />}
              </div>
            );
          })}
        </div>
      )}

      {payingOrder && (
        <PaymentModal
          order={payingOrder}
          submitting={submittingPayment}
          onClose={() => setPayingOrder(null)}
          onConfirm={handleConfirmPayment}
        />
      )}
    </div>
  );
}
