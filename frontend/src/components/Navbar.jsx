import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { useTheme } from '../hooks/useTheme';
import Logo from './Logo';
import { SunIcon, MoonIcon, CartIcon, BoxIcon, UsersIcon, ChartIcon, TagIcon } from './Icons';

export default function Navbar() {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="brand">
          <Logo size={38} />
          Farm2Biz Hub
        </Link>

        <nav>
          {!user && (
            <>
              <Link to="/login" className="nav-link">Login</Link>
              <Link to="/register" className="btn-nav">Get Started</Link>
            </>
          )}

          {user?.role === 'BULK_BUYER' && (
            <Link to="/my-orders" className="nav-link"><CartIcon size={16} /> My Orders</Link>
          )}

          {user?.role === 'FARMER' && (
            <>
              <Link to="/dashboard" className="nav-link"><BoxIcon size={16} /> My Products</Link>
              <Link to="/incoming-orders" className="nav-link"><CartIcon size={16} /> Incoming Orders</Link>
            </>
          )}

          {user?.role === 'ADMIN' && (
            <>
              <Link to="/admin" className="nav-link"><ChartIcon size={16} /> Dashboard</Link>
              <Link to="/admin/categories" className="nav-link"><TagIcon size={16} /> Categories</Link>
              <Link to="/admin/users" className="nav-link"><UsersIcon size={16} /> Users</Link>
              <Link to="/admin/orders" className="nav-link"><CartIcon size={16} /> Orders</Link>
            </>
          )}

          <button className="theme-toggle" onClick={toggleTheme} aria-label="Toggle dark mode" title="Toggle theme">
            {theme === 'light' ? <MoonIcon size={17} /> : <SunIcon size={17} />}
          </button>

          {user && (
            <div className="nav-user">
              <span className="avatar">{user.name?.[0]?.toUpperCase()}</span>
              <div className="nav-user-meta">
                <span className="nav-name">{user.name}</span>
                <span className="role-chip">{user.role.replace('_', ' ')}</span>
              </div>
              <button className="link-btn" onClick={handleLogout}>Logout</button>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
}
