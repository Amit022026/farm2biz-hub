// The Farm2Biz brand mark: a bold "F" monogram (Farm2Biz) with a single
// orange leaf perched on the top-right stroke - the leaf is the only
// accent-colored element, so it reads as a deliberate signature detail
// rather than clutter. Same fixed-hex-value approach as before (not CSS
// vars) so this stays visually identical to public/favicon.svg, which
// the browser renders outside our page's CSS entirely.
export default function Logo({ size = 36 }) {
  const gradId = 'f2bLogoGrad';

  return (
    <svg width={size} height={size} viewBox="0 0 40 40" fill="none" aria-hidden="true">
      <defs>
        <linearGradient id={gradId} x1="4" y1="2" x2="36" y2="38" gradientUnits="userSpaceOnUse">
          <stop offset="0" stopColor="#2e7d32" />
          <stop offset="1" stopColor="#0f3313" />
        </linearGradient>
      </defs>

      <rect x="2" y="2" width="36" height="36" rx="11" fill={`url(#${gradId})`} />

      {/* The "F" */}
      <path d="M13 30V10h13" stroke="#f2fbf0" strokeWidth="4.5" strokeLinecap="round" strokeLinejoin="round" fill="none" />
      <path d="M13 20h9" stroke="#f2fbf0" strokeWidth="4.5" strokeLinecap="round" fill="none" />

      {/* Signature leaf accent */}
      <ellipse cx="28.5" cy="9.5" rx="3.6" ry="5.6" transform="rotate(28 28.5 9.5)" fill="#fb8c00" />
    </svg>
  );
}
