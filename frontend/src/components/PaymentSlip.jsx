import { CheckIcon, XIcon } from './Icons';

export default function PaymentSlip({ payment }) {
  const success = payment.status === 'SUCCESS';

  return (
    <div className={`payment-card ${success ? 'payment-success' : 'payment-failed'}`}>
      <div className="payment-card-icon">
        {success ? <CheckIcon size={20} /> : <XIcon size={20} />}
      </div>
      <div className="payment-card-body">
        <div className="payment-card-top">
          <span className="mono-id">{payment.method.replace('_', ' ')}</span>
          <strong>₹{payment.amount}</strong>
        </div>
        <p className="muted mono-small">{payment.transactionRef}</p>
        <p className="muted" style={{ fontSize: '0.78rem' }}>
          {payment.paidAt ? new Date(payment.paidAt).toLocaleString() : 'Payment pending'}
        </p>
      </div>
    </div>
  );
}
