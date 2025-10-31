import axios from 'axios';
import type { Customer } from '../types/Customer';

const API_BASE_URL = 'http://localhost:8080/api';

export const customerService = {
  // Get all customers
  getAllCustomers: async (): Promise<Customer[]> => {
    const response = await axios.get(`${API_BASE_URL}/customers`);
    return response.data;
  },

  // Get customer by ID
  getCustomerById: async (id: number): Promise<Customer> => {
    const response = await axios.get(`${API_BASE_URL}/customers/${id}`);
    return response.data;
  },

  // Create new customer
  createCustomer: async (customer: Omit<Customer, 'id'>): Promise<Customer> => {
    const response = await axios.post(`${API_BASE_URL}/customers`, customer);
    return response.data;
  },

  // Delete customer
  deleteCustomer: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/customers/${id}`);
  },

  // Search customers
  searchCustomers: async (name: string): Promise<Customer[]> => {
    const response = await axios.get(`${API_BASE_URL}/customers/search`, {
      params: { name }
    });
    return response.data;
  },

  // Health check
  healthCheck: async (): Promise<string> => {
    const response = await axios.get(`${API_BASE_URL}/health`);
    return response.data;
  }
};