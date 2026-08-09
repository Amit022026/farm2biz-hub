import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { LeafIcon } from '../components/Icons';

export default function Login() {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [serverError, setServerError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onSubmit = async (data) => {
    setServerError('');
    setSubmitting(true);
    try {
      const user = await login(data);
      const redirectTo = location.state?.from?.pathname
        || (user.role === 'ADMIN' ? '/admin' : user.role === 'FARMER' ? '/dashboard' : '/');
      navigate(redirectTo, { replace: true });
    } catch (err) {
      setServerError(err.response?.data?.message || 'Login failed. Check your credentials.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="form-card glass-card">
        <div className="form-card-icon"><LeafIcon size={22} /></div>
        <h2>Welcome back</h2>
        <p className="muted center-text">Sign in to continue to Farm2Biz Hub</p>
        {serverError && <div className="alert alert-error">{serverError}</div>}

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="field">
            <label>Email</label>
            <input type="email" {...register('email', { required: 'Email is required' })} />
            {errors.email && <div className="error">{errors.email.message}</div>}
          </div>
          <div className="field">
            <label>Password</label>
            <input type="password" {...register('password', { required: 'Password is required' })} />
            {errors.password && <div className="error">{errors.password.message}</div>}
          </div>
          <button className="btn btn-block" disabled={submitting}>
            {submitting ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div className="form-footer">
          New here? <Link to="/register">Create an account</Link>
        </div>
      </div>
    </div>
  );
}
