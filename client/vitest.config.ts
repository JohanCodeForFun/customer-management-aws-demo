import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    // Use different environments for different test types
    environment: process.env.VITEST_ENVIRONMENT || 'node',
    globals: true,
    setupFiles: ['./src/test/setup.ts'],
    testTimeout: 30000,
    hookTimeout: 30000,
    teardownTimeout: 30000,
    // Allow tests to run against real backend
    pool: 'forks',
    poolOptions: {
      forks: {
        singleFork: true
      }
    },
    // CI-specific configuration
    ...(process.env.CI && {
      pool: process.env.VITEST_ENVIRONMENT === 'jsdom' ? 'forks' : 'threads',
      poolOptions: {
        threads: {
          singleThread: true
        },
        forks: {
          singleFork: true
        }
      },
      teardownTimeout: 5000,
      testTimeout: 20000,
      // Force exit for integration tests to prevent hanging
      ...(process.env.VITEST_ENVIRONMENT === 'jsdom' && {
        forceRerunTriggers: [],
        watch: false
      })
    })
  },
  define: {
    'import.meta.env.VITE_API_BASE_URL': JSON.stringify(process.env.VITE_API_BASE_URL || 'http://localhost:8080'),
    global: 'globalThis',
  }
});