import { useState } from 'react';
import type { Customer } from '../types/Customer';
import { customerService } from '../services/customerService';
import './CustomerList.css';

interface CustomerListProps {
  customers: Customer[];
  onCustomerDeleted: (id: number) => void;
  onRefresh: () => void;
}

const CustomerList = ({ customers, onCustomerDeleted, onRefresh }: CustomerListProps) => {
  const [loading, setLoading] = useState(false);

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      setLoading(true);
      try {
        await customerService.deleteCustomer(id);
        onCustomerDeleted(id);
      } catch (error) {
        console.error('Error deleting customer:', error);
        alert('Failed to delete customer');
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div className="customer-list">
      <div className="customer-list-header">
        <h2>Customers ({customers.length})</h2>
        <button onClick={onRefresh} className="refresh-btn" disabled={loading}>
          🔄 Refresh
        </button>
      </div>

      {customers.length === 0 ? (
        <div className="empty-state">
          <p>No customers found</p>
        </div>
      ) : (
        <div className="customer-grid">
          {customers.map(customer => (
            <div key={customer.id} className="customer-card">
              <div className="customer-info">
                <h3>{customer.firstName} {customer.lastName}</h3>
                <p>ID: {customer.id}</p>
              </div>
              <button
                onClick={() => handleDelete(customer.id)}
                className="delete-btn"
                disabled={loading}
                title="Delete customer"
              >
                🗑️
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default CustomerList;