import { useState } from 'react';

const METHODS = [
  { id: 'CARD', label: 'Card' },
  { id: 'UPI', label: 'UPI' },
  { id: 'NET_BANKING', label: 'Net Banking' },
  { id: 'COD', label: 'Cash on Delivery' },
];

export default function PaymentModal({ order, onClose, onConfirm, submitting }) {
  const [method, setMethod] = useState('UPI');

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <h3>Complete Payment</h3>
        <p className="muted">Order ORD-{String(order.orderId).padStart(5, '0')}</p>
        <div className="modal-amount">₹{order.totalAmount}</div>

        <div className="method-grid">
          {METHODS.map((m) => (
            <button
              key={m.id}
              type="button"
              className={`method-option ${method === m.id ? 'method-selected' : ''}`}
              onClick={() => setMethod(m.id)}
            >
              {m.label}
            </button>
          ))}
        </div>

        <div className="modal-actions">
          <button className="btn btn-outline" onClick={onClose} disabled={submitting}>Cancel</button>
          <button className="btn btn-accent" onClick={() => onConfirm(method)} disabled={submitting}>
            {submitting ? 'Processing...' : `Pay ₹${order.totalAmount}`}
          </button>
        </div>
      </div>
    </div>
  );
}
