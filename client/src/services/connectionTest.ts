// Test connection to AWS backend
const testBackendConnection = async () => {
  const apiUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
  
  console.log('Testing connection to:', apiUrl);
  
  try {
    // Test simple ping endpoint
    const pingResponse = await fetch(`${apiUrl}/ping`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (pingResponse.ok) {
      const pingResult = await pingResponse.text();
      console.log('✅ Ping successful:', pingResult);
    } else {
      console.error('❌ Ping failed:', pingResponse.status, pingResponse.statusText);
    }
    
    // Test health endpoint
    const healthResponse = await fetch(`${apiUrl}/health`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (healthResponse.ok) {
      const healthResult = await healthResponse.text();
      console.log('✅ Health check successful:', healthResult);
    } else {
      console.error('❌ Health check failed:', healthResponse.status, healthResponse.statusText);
    }
    
  } catch (error) {
    console.error('❌ Connection error:', error);
  }
};

// Run test when page loads
window.addEventListener('load', testBackendConnection);

export { testBackendConnection };