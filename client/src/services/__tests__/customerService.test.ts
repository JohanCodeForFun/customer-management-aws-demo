import { describe, it, expect, vi, beforeEach } from 'vitest';
import { customerService } from '../customerService';

vi.mock('axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn(),
  },
}));

// Get references to the mocked functions
import axios from 'axios';
const mockGet = vi.mocked(axios.get);
const mockPost = vi.mocked(axios.post);
const mockDelete = vi.mocked(axios.delete);

describe('Customer Service', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should fetch all customers', async () => {
    const mockCustomers = [
      { id: 1, firstName: 'John', lastName: 'Doe' },
      { id: 2, firstName: 'Jane', lastName: 'Smith' }
    ];

    mockGet.mockResolvedValue({ data: mockCustomers });

    const result = await customerService.getAllCustomers();

    expect(mockGet).toHaveBeenCalledWith(expect.stringContaining('/customers'));
    expect(result).toEqual(mockCustomers);
  });

  it('should create a new customer', async () => {
    const newCustomer = { firstName: 'Test', lastName: 'User' };
    const createdCustomer = { id: 3, ...newCustomer };

    mockPost.mockResolvedValue({ data: createdCustomer });

    const result = await customerService.createCustomer(newCustomer);

    expect(mockPost).toHaveBeenCalledWith(
      expect.stringContaining('/customers'),
      newCustomer
    );
    expect(result).toEqual(createdCustomer);
  });

  it('should delete a customer', async () => {
    const customerId = 1;

    mockDelete.mockResolvedValue({});

    await customerService.deleteCustomer(customerId);

    expect(mockDelete).toHaveBeenCalledWith(
      expect.stringContaining(`/customers/${customerId}`)
    );
  });

  it('should search customers by name', async () => {
    const searchName = 'John';
    const mockResults = [{ id: 1, firstName: 'John', lastName: 'Doe' }];

    mockGet.mockResolvedValue({ data: mockResults });

    const result = await customerService.searchCustomers(searchName);

    expect(mockGet).toHaveBeenCalledWith(
      expect.stringContaining('/customers/search'),
      expect.objectContaining({
        params: { name: searchName }
      })
    );
    expect(result).toEqual(mockResults);
  });

  it('should perform health check', async () => {
    const healthMessage = 'Customer Management API is running!';

    mockGet.mockResolvedValue({ data: healthMessage });

    const result = await customerService.healthCheck();

    expect(mockGet).toHaveBeenCalledWith(expect.stringContaining('/health'));
    expect(result).toBe(healthMessage);
  });
});