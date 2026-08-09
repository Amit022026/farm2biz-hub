import { useEffect, useState } from 'react';
import { reportService } from '../services/reportService';
import StatCard from '../components/StatCard';
import BarChart from '../components/BarChart';
import { SkeletonRow } from '../components/Skeleton';
import { LeafIcon, CartIcon, BoxIcon, TagIcon, UsersIcon, RupeeIcon } from '../components/Icons';

export default function AdminDashboard() {
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    reportService.getSummary().then(setSummary).catch(() => setError('Could not load reports.'));
  }, []);

  if (error) return <div className="container"><div className="alert alert-error">{error}</div></div>;
  if (!summary) {
    return (
      <div className="container">
        <div className="stack-gap">{[1, 2, 3].map((i) => <SkeletonRow key={i} />)}</div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header"><h1>Platform Overview</h1></div>

      <div className="stat-grid">
        <StatCard icon={<LeafIcon size={20} />} label="Farmers" value={summary.totalFarmers} tone="green" />
        <StatCard icon={<UsersIcon size={20} />} label="Bulk Buyers" value={summary.totalBulkBuyers} tone="blue" />
        <StatCard icon={<BoxIcon size={20} />} label="Products" value={summary.totalProducts} tone="amber" />
        <StatCard icon={<TagIcon size={20} />} label="Categories" value={summary.totalCategories} tone="default" />
        <StatCard icon={<CartIcon size={20} />} label="Total Orders" value={summary.totalOrders} tone="default" />
        <StatCard icon={<RupeeIcon size={20} />} label="Revenue (accepted)" value={`₹${summary.totalRevenue}`} tone="green" />
      </div>

      <div className="card glass-card" style={{ marginTop: 28 }}>
        <h3>Orders by Status</h3>
        <BarChart data={summary.ordersByStatus} />
      </div>
    </div>
  );
}
