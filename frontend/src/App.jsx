import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ProtectedRoute from './routes/ProtectedRoute';

import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import ProductDetail from './pages/ProductDetail';
import MyOrders from './pages/MyOrders';
import FarmerDashboard from './pages/FarmerDashboard';
import IncomingOrders from './pages/IncomingOrders';
import AdminDashboard from './pages/AdminDashboard';
import AdminCategories from './pages/AdminCategories';
import AdminUsers from './pages/AdminUsers';
import AdminOrders from './pages/AdminOrders';

export default function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="app-main">
        <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/products/:id" element={<ProductDetail />} />

        <Route path="/my-orders" element={
          <ProtectedRoute roles={['BULK_BUYER']}><MyOrders /></ProtectedRoute>
        } />

        <Route path="/dashboard" element={
          <ProtectedRoute roles={['FARMER']}><FarmerDashboard /></ProtectedRoute>
        } />
        <Route path="/incoming-orders" element={
          <ProtectedRoute roles={['FARMER']}><IncomingOrders /></ProtectedRoute>
        } />

        <Route path="/admin" element={
          <ProtectedRoute roles={['ADMIN']}><AdminDashboard /></ProtectedRoute>
        } />
        <Route path="/admin/categories" element={
          <ProtectedRoute roles={['ADMIN']}><AdminCategories /></ProtectedRoute>
        } />
        <Route path="/admin/users" element={
          <ProtectedRoute roles={['ADMIN']}><AdminUsers /></ProtectedRoute>
        } />
        <Route path="/admin/orders" element={
          <ProtectedRoute roles={['ADMIN']}><AdminOrders /></ProtectedRoute>
        } />

        <Route path="*" element={<div className="container">Page not found.</div>} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}
