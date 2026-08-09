import { useState } from 'react';
import { LeafIcon } from './Icons';

// Deterministic fallback gradient per product, so the same product
// always gets the same placeholder color instead of a random one on
// every render - feels intentional rather than broken.
const GRADIENTS = [
  'linear-gradient(135deg, #a8e6a1, #4caf50)',
  'linear-gradient(135deg, #ffe0a3, #f9a825)',
  'linear-gradient(135deg, #b3e5fc, #0288d1)',
  'linear-gradient(135deg, #ffccbc, #e64a19)',
  'linear-gradient(135deg, #d1c4e9, #7e57c2)',
];

function gradientFor(seed) {
  const n = String(seed || '').split('').reduce((a, c) => a + c.charCodeAt(0), 0);
  return GRADIENTS[n % GRADIENTS.length];
}

export default function ProductImage({ src, alt, seed, className = '' }) {
  const [broken, setBroken] = useState(false);
  const showFallback = !src || broken;

  if (showFallback) {
    return (
      <div className={`product-img-fallback ${className}`} style={{ background: gradientFor(seed || alt) }}>
        <LeafIcon size={30} />
      </div>
    );
  }

  return (
    <img
      src={src}
      alt={alt}
      className={`product-img ${className}`}
      loading="lazy"
      onError={() => setBroken(true)}
    />
  );
}
