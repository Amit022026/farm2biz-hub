export function SkeletonProductCard() {
  return (
    <div className="product-card skeleton-card">
      <div className="skeleton skeleton-img" />
      <div className="product-card-body">
        <div className="skeleton skeleton-line" style={{ width: '40%' }} />
        <div className="skeleton skeleton-line" style={{ width: '70%', height: 18 }} />
        <div className="skeleton skeleton-line" style={{ width: '50%' }} />
        <div className="skeleton skeleton-line" style={{ width: '60%', marginTop: 10 }} />
      </div>
    </div>
  );
}

export function SkeletonGrid({ count = 8 }) {
  return (
    <div className="grid">
      {Array.from({ length: count }).map((_, i) => <SkeletonProductCard key={i} />)}
    </div>
  );
}

export function SkeletonRow() {
  return (
    <div className="skeleton-row">
      <div className="skeleton skeleton-avatar" />
      <div style={{ flex: 1 }}>
        <div className="skeleton skeleton-line" style={{ width: '35%' }} />
        <div className="skeleton skeleton-line" style={{ width: '55%' }} />
      </div>
    </div>
  );
}
