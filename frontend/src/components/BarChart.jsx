const COLORS = {
  PENDING: 'var(--warn)',
  ACCEPTED: 'var(--info)',
  REJECTED: 'var(--danger)',
  CANCELLED: 'var(--muted-strong)',
};

// A small, dependency-free bar chart - built with plain SVG rects. Good
// enough for a handful of categories (order statuses) without pulling in
// a full charting library.
export default function BarChart({ data }) {
  const entries = Object.entries(data || {});
  const max = Math.max(1, ...entries.map(([, v]) => v));
  const barWidth = 56;
  const gap = 34;
  const chartHeight = 160;
  const width = entries.length * (barWidth + gap) + gap;

  if (entries.length === 0) {
    return <p className="muted">No order data yet.</p>;
  }

  return (
    <svg width="100%" viewBox={`0 0 ${width} ${chartHeight + 40}`} className="bar-chart">
      {entries.map(([status, value], i) => {
        const barHeight = (value / max) * chartHeight;
        const x = gap + i * (barWidth + gap);
        const y = chartHeight - barHeight;
        return (
          <g key={status}>
            <rect
              x={x}
              y={y}
              width={barWidth}
              height={barHeight}
              rx="8"
              fill={COLORS[status] || 'var(--primary-500)'}
              className="bar-rect"
            />
            <text x={x + barWidth / 2} y={y - 8} textAnchor="middle" className="bar-value">{value}</text>
            <text x={x + barWidth / 2} y={chartHeight + 20} textAnchor="middle" className="bar-label">{status}</text>
          </g>
        );
      })}
    </svg>
  );
}
