import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { customerService } from '../customerService';

// Mock axios
vi.mock('axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn(),
  },
}));

const mockedAxios = axios as any;

describe('Customer Service', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should fetch all customers', async () => {
    const mockCustomers = [
      { id: 1, firstName: 'John', lastName: 'Doe' },
      { id: 2, firstName: 'Jane', lastName: 'Smith' }
    ];

    mockedAxios.get.mockResolvedValue({ data: mockCustomers });

    const result = await customerService.getAllCustomers();

    expect(mockedAxios.get).toHaveBeenCalledWith(expect.stringContaining('/customers'));
    expect(result).toEqual(mockCustomers);
  });

  it('should create a new customer', async () => {
    const newCustomer = { firstName: 'Test', lastName: 'User' };
    const createdCustomer = { id: 3, ...newCustomer };

    mockedAxios.post.mockResolvedValue({ data: createdCustomer });

    const result = await customerService.createCustomer(newCustomer);

    expect(mockedAxios.post).toHaveBeenCalledWith(
      expect.stringContaining('/customers'),
      newCustomer
    );
    expect(result).toEqual(createdCustomer);
  });

  it('should delete a customer', async () => {
    const customerId = 1;

    mockedAxios.delete.mockResolvedValue({});

    await customerService.deleteCustomer(customerId);

    expect(mockedAxios.delete).toHaveBeenCalledWith(
      expect.stringContaining(`/customers/${customerId}`)
    );
  });

  it('should search customers by name', async () => {
    const searchName = 'John';
    const mockResults = [{ id: 1, firstName: 'John', lastName: 'Doe' }];

    mockedAxios.get.mockResolvedValue({ data: mockResults });

    const result = await customerService.searchCustomers(searchName);

    expect(mockedAxios.get).toHaveBeenCalledWith(
      expect.stringContaining('/customers/search'),
      expect.objectContaining({
        params: { name: searchName }
      })
    );
    expect(result).toEqual(mockResults);
  });

  it('should perform health check', async () => {
    const healthMessage = 'Customer Management API is running!';

    mockedAxios.get.mockResolvedValue({ data: healthMessage });

    const result = await customerService.healthCheck();

    expect(mockedAxios.get).toHaveBeenCalledWith(expect.stringContaining('/health'));
    expect(result).toBe(healthMessage);
  });
});