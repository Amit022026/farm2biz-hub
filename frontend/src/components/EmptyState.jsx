// A friendly, hand-drawn-feeling inline SVG illustration - deliberately
// simple/geometric so it renders crisply at any size with zero external
// image dependencies.
function EmptyIllustration() {
  return (
    <svg width="140" height="110" viewBox="0 0 140 110" fill="none">
      <ellipse cx="70" cy="98" rx="55" ry="8" fill="var(--shadow-soft)" />
      <rect x="30" y="30" width="80" height="55" rx="10" fill="var(--surface-2)" stroke="var(--border)" strokeWidth="2" />
      <path d="M30 48h80" stroke="var(--border)" strokeWidth="2" />
      <circle cx="50" cy="39" r="3" fill="var(--accent)" />
      <circle cx="60" cy="39" r="3" fill="var(--primary-400)" />
      <path d="M45 65c5-8 12-8 17 0s12 8 17 0" stroke="var(--primary-500)" strokeWidth="3" strokeLinecap="round" fill="none" />
      <circle cx="70" cy="20" r="10" fill="var(--primary-100)" stroke="var(--primary-400)" strokeWidth="2" />
      <path d="M66 20l3 3 6-6" stroke="var(--primary-600)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" fill="none" />
    </svg>
  );
}

export default function EmptyState({ title, subtitle, action }) {
  return (
    <div className="empty-state">
      <EmptyIllustration />
      <h3>{title}</h3>
      {subtitle && <p className="muted">{subtitle}</p>}
      {action}
    </div>
  );
}
