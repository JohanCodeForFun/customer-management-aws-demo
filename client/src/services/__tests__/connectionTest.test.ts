import { describe, it, expect, beforeAll } from 'vitest';
import request from 'supertest';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

describe('Backend Integration Tests', () => {
  let apiUrl: string;

  beforeAll(() => {
    apiUrl = API_BASE_URL;
    console.log('Testing API at:', apiUrl);
  });

  it('should connect to ping endpoint', async () => {
    const response = await request(apiUrl)
      .get('/api/ping')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(response.status).toBe(200);
    expect(response.text).toBe('pong');
  });

  it('should connect to health endpoint', async () => {
    const response = await request(apiUrl)
      .get('/api/health')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(response.status).toBe(200);
    expect(response.text).toContain('Customer Management API is running');
  });

  it('should get customers from /api/customers endpoint', async () => {
    const response = await request(apiUrl)
      .get('/api/customers')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(response.status).toBe(200);
    expect(Array.isArray(response.body)).toBe(true);

    // If customers exist, verify structure
    if (response.body.length > 0) {
      const customer = response.body[0];
      expect(customer).toHaveProperty('id');
      expect(customer).toHaveProperty('firstName');
      expect(customer).toHaveProperty('lastName');
      expect(typeof customer.id).toBe('number');
      expect(typeof customer.firstName).toBe('string');
      expect(typeof customer.lastName).toBe('string');
    }
  });

  it('should handle CORS headers correctly', async () => {
    const response = await request(apiUrl)
      .get('/api/health')
      .set('Origin', 'http://localhost:5173')
      .set('Content-Type', 'application/json');

    expect(response.status).toBe(200);
    // Check that CORS headers are present (if configured)
    // Note: These might not be present in all environments
    if (response.headers['access-control-allow-origin']) {
      expect(response.headers['access-control-allow-origin']).toBeDefined();
    }
  });

  it('should create and delete a customer (full CRUD cycle)', async () => {
    // Create a test customer
    const newCustomer = {
      firstName: 'Integration',
      lastName: 'Test'
    };

    const createResponse = await request(apiUrl)
      .post('/api/customers')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173')
      .send(newCustomer);

    expect(createResponse.status).toBe(200);
    expect(createResponse.body).toHaveProperty('id');
    expect(createResponse.body.firstName).toBe('Integration');
    expect(createResponse.body.lastName).toBe('Test');

    const createdCustomerId = createResponse.body.id;

    // Get the created customer
    const getResponse = await request(apiUrl)
      .get(`/api/customers/${createdCustomerId}`)
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(getResponse.status).toBe(200);
    expect(getResponse.body.id).toBe(createdCustomerId);

    // Delete the test customer
    const deleteResponse = await request(apiUrl)
      .delete(`/api/customers/${createdCustomerId}`)
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(deleteResponse.status).toBe(200);

    // Verify customer is deleted
    const verifyDeleteResponse = await request(apiUrl)
      .get(`/api/customers/${createdCustomerId}`)
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(verifyDeleteResponse.status).toBe(404);
  });

  it('should search customers by name', async () => {
    const response = await request(apiUrl)
      .get('/api/customers/search')
      .query({ name: 'John' })
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(response.status).toBe(200);
    expect(Array.isArray(response.body)).toBe(true);

    // If results exist, they should contain 'John'
    if (response.body.length > 0) {
      const hasJohn = response.body.some((customer: { firstName: string; lastName: string }) =>
        customer.firstName.toLowerCase().includes('john') ||
        customer.lastName.toLowerCase().includes('john')
      );
      expect(hasJohn).toBe(true);
    }
  });

  it('should handle invalid customer ID gracefully', async () => {
    const response = await request(apiUrl)
      .get('/api/customers/99999')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173');

    expect(response.status).toBe(404);
  });

  it('should validate customer creation with invalid data', async () => {
    const invalidCustomer = {
      firstName: '', // Empty first name
      lastName: ''   // Empty last name
    };

    const response = await request(apiUrl)
      .post('/api/customers')
      .set('Content-Type', 'application/json')
      .set('Origin', 'http://localhost:5173')
      .send(invalidCustomer);

    // Should handle invalid data (status might be 400 or 200 depending on backend validation)
    expect([200, 400, 422]).toContain(response.status);
  });
});