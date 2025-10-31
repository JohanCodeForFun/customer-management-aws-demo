import { useState } from 'react';
import type { Customer } from '../types/Customer';
import { customerService } from '../services/customerService';
import './CustomerForm.css';

interface CustomerFormProps {
  onCustomerAdded: (customer: Customer) => void;
}

const CustomerForm = ({ onCustomerAdded }: CustomerFormProps) => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    if (!firstName.trim() || !lastName.trim()) {
      alert('Please enter both first name and last name');
      return;
    }

    setLoading(true);
    try {
      const newCustomer = await customerService.createCustomer({
        firstName: firstName.trim(),
        lastName: lastName.trim()
      });
      
      onCustomerAdded(newCustomer);
      setFirstName('');
      setLastName('');
    } catch (error) {
      console.error('Error creating customer:', error);
      alert('Failed to create customer');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="customer-form">
      <h2>Add New Customer</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="firstName">First Name:</label>
            <input
              type="text"
              id="firstName"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              disabled={loading}
              placeholder="Enter first name"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="lastName">Last Name:</label>
            <input
              type="text"
              id="lastName"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              disabled={loading}
              placeholder="Enter last name"
              required
            />
          </div>
        </div>
        
        <button type="submit" disabled={loading} className="submit-btn">
          {loading ? 'Adding...' : 'Add Customer'}
        </button>
      </form>
    </div>
  );
};

export default CustomerForm;