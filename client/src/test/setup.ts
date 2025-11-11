// Test setup for CI environment compatibility
import { beforeEach } from 'vitest';

// Store original console methods
const originalWarn = console.warn;
const originalError = console.error;

// Suppress webidl-conversions warnings in CI
beforeEach(() => {
  console.warn = (...args: unknown[]) => {
    if (
      args[0] && 
      typeof args[0] === 'string' && 
      (args[0].includes('webidl-conversions') || args[0].includes('whatwg-url'))
    ) {
      return;
    }
    originalWarn.apply(console, args);
  };

  console.error = (...args: unknown[]) => {
    if (
      args[0] && 
      typeof args[0] === 'string' && 
      (args[0].includes('webidl-conversions') || args[0].includes('whatwg-url'))
    ) {
      return;
    }
    originalError.apply(console, args);
  };
});