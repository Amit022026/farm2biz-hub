import { useEffect, useState } from 'react';
import { userService } from '../services/userService';
import EmptyState from '../components/EmptyState';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

  const load = () => userService.listAll().then(setUsers);
  useEffect(() => { load(); }, []);

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this user?')) return;
    setError('');
    try {
      await userService.remove(id);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Could not delete user.');
    }
  };

  return (
    <div className="container">
      <div className="page-header"><h1>Manage Users</h1></div>
      {error && <div className="alert alert-error">{error}</div>}

      {users.length === 0 ? (
        <EmptyState title="No users found" />
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr><th>Name</th><th>Email</th><th>Role</th><th></th></tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.userId}>
                  <td>
                    <div className="table-user">
                      <span className="avatar avatar-sm">{u.name?.[0]?.toUpperCase()}</span>
                      {u.name}
                    </div>
                  </td>
                  <td>{u.email}</td>
                  <td><span className="chip chip-muted">{u.role}</span></td>
                  <td>
                    <button className="btn btn-sm btn-danger-outline" onClick={() => handleDelete(u.userId)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
