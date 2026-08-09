import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { LeafIcon, CartIcon } from '../components/Icons';

export default function Register() {
  const { register, handleSubmit, watch, formState: { errors } } = useForm({
    defaultValues: { role: 'BULK_BUYER' },
  });
  const { register: doRegister } = useAuth();
  const navigate = useNavigate();
  const [serverError, setServerError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const password = watch('password');
  const role = watch('role');

  const onSubmit = async (data) => {
    setServerError('');
    setSubmitting(true);
    try {
      const user = await doRegister(data);
      navigate(user.role === 'FARMER' ? '/dashboard' : '/', { replace: true });
    } catch (err) {
      setServerError(err.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="form-card glass-card">
        <div className="form-card-icon"><LeafIcon size={22} /></div>
        <h2>Join Farm2Biz Hub</h2>
        <p className="muted center-text">Create your account to get started</p>
        {serverError && <div className="alert alert-error">{serverError}</div>}

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="field">
            <label>I am a...</label>
            <div className="role-toggle">
              <label className={`role-option ${role === 'BULK_BUYER' ? 'role-active' : ''}`}>
                <input type="radio" value="BULK_BUYER" {...register('role')} />
                <CartIcon size={18} /> Buyer
              </label>
              <label className={`role-option ${role === 'FARMER' ? 'role-active' : ''}`}>
                <input type="radio" value="FARMER" {...register('role')} />
                <LeafIcon size={18} /> Farmer
              </label>
            </div>
          </div>

          <div className="field">
            <label>Full name</label>
            <input {...register('name', { required: 'Name is required' })} />
            {errors.name && <div className="error">{errors.name.message}</div>}
          </div>

          <div className="field">
            <label>Email</label>
            <input type="email" {...register('email', { required: 'Email is required' })} />
            {errors.email && <div className="error">{errors.email.message}</div>}
          </div>

          <div className="field">
            <label>Password</label>
            <input
              type="password"
              {...register('password', {
                required: 'Password is required',
                minLength: { value: 8, message: 'At least 8 characters' },
              })}
            />
            {errors.password && <div className="error">{errors.password.message}</div>}
          </div>

          <div className="field">
            <label>Confirm password</label>
            <input
              type="password"
              {...register('confirmPassword', {
                required: 'Please confirm your password',
                validate: (v) => v === password || 'Passwords do not match',
              })}
            />
            {errors.confirmPassword && <div className="error">{errors.confirmPassword.message}</div>}
          </div>

          <button className="btn btn-block" disabled={submitting}>
            {submitting ? 'Creating account...' : 'Create account'}
          </button>
        </form>

        <div className="form-footer">
          Already have an account? <Link to="/login">Sign in</Link>
        </div>
      </div>
    </div>
  );
}
