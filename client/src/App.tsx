import { useState, useEffect } from 'react';
import type { Customer } from './types/Customer';
import { customerService } from './services/customerService';
import CustomerList from './components/CustomerList';
import CustomerForm from './components/CustomerForm';
import './App.css';

function App() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [serverStatus, setServerStatus] = useState<string>('');

  // Load customers on component mount
  useEffect(() => {
    loadCustomers();
    checkServerHealth();
  }, []);

  const checkServerHealth = async () => {
    try {
      const status = await customerService.healthCheck();
      setServerStatus(status);
    } catch (error) {
      setServerStatus('Server not responding');
      console.error('Health check failed:', error);
    }
  };

  const loadCustomers = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (error) {
      setError('Failed to load customers. Make sure the server is running.');
      console.error('Error loading customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCustomerAdded = (newCustomer: Customer) => {
    setCustomers(prev => [...prev, newCustomer]);
  };

  const handleCustomerDeleted = (deletedId: number) => {
    setCustomers(prev => prev.filter(customer => customer.id !== deletedId));
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Customer Management System</h1>
        <div className="server-status">
          <span className={serverStatus.includes('running') ? 'status-online' : 'status-offline'}>
            {serverStatus || 'Checking server...'}
          </span>
        </div>
      </header>

      <main className="app-main">
        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={loadCustomers} className="retry-btn">
              Retry
            </button>
          </div>
        )}

        {!error && (
          <>
            <CustomerForm onCustomerAdded={handleCustomerAdded} />

            {loading ? (
              <div className="loading">Loading customers...</div>
            ) : (
              <CustomerList
                customers={customers}
                onCustomerDeleted={handleCustomerDeleted}
                onRefresh={loadCustomers}
              />
            )}
          </>
        )}
      </main>

      <footer className="app-footer">
        <p>
          Backend: Spring Boot + PostgreSQL |
          Frontend: React + TypeScript |
          <a href="http://localhost:8080/api/health" target="_blank" rel="noopener noreferrer">
            API Health Check
          </a>
        </p>
      </footer>
    </div>
  );
}

export default App;
