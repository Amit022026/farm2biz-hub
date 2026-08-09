import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { categoryService } from '../services/categoryService';
import EmptyState from '../components/EmptyState';
import { TagIcon } from '../components/Icons';

export default function AdminCategories() {
  const [categories, setCategories] = useState([]);
  const [editing, setEditing] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const { register, handleSubmit, reset, formState: { errors } } = useForm();

  const load = () => categoryService.listAll().then(setCategories);

  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); reset({ name: '', description: '' }); setShowForm(true); };
  const openEdit = (cat) => { setEditing(cat); reset(cat); setShowForm(true); };

  const onSubmit = async (data) => {
    setError('');
    try {
      if (editing) await categoryService.update(editing.categoryId, data);
      else await categoryService.create(data);
      setShowForm(false);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save category.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this category? Products using it may be affected.')) return;
    setError('');
    try {
      await categoryService.remove(id);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not delete category.');
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>Manage Categories</h1>
        <button className="btn btn-accent" onClick={openCreate}>+ Add Category</button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {showForm && (
        <div className="card form-inline-card glass-card">
          <h3>{editing ? 'Edit category' : 'New category'}</h3>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="field">
              <label>Name</label>
              <input {...register('name', { required: 'Name is required' })} />
              {errors.name && <div className="error">{errors.name.message}</div>}
            </div>
            <div className="field">
              <label>Description</label>
              <input {...register('description')} />
            </div>
            <button className="btn btn-accent" type="submit">{editing ? 'Save changes' : 'Create'}</button>{' '}
            <button className="btn btn-outline" type="button" onClick={() => setShowForm(false)}>Cancel</button>
          </form>
        </div>
      )}

      {categories.length === 0 ? (
        <EmptyState title="No categories yet" subtitle="Create one so farmers can start listing products." />
      ) : (
        <div className="grid grid-tight">
          {categories.map((c) => (
            <div key={c.categoryId} className="card category-card glass-card">
              <div className="category-icon"><TagIcon size={18} /></div>
              <h3>{c.name}</h3>
              <p className="muted">{c.description}</p>
              <div className="manage-card-actions">
                <button className="btn btn-sm btn-outline" onClick={() => openEdit(c)}>Edit</button>
                <button className="btn btn-sm btn-danger-outline" onClick={() => handleDelete(c.categoryId)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
