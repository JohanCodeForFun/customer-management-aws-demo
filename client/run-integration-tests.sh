#!/bin/bash

# Integration test runner with proper cleanup
set -e

echo "Starting integration tests..."

# Set up signal handlers for cleanup
cleanup() {
    echo "Cleaning up processes..."
    pkill -f "vitest" || true
    pkill -f "vite" || true
    exit $1
}

trap 'cleanup $?' EXIT
trap 'cleanup 1' INT TERM

# Run the tests
cd "$(dirname "$0")"
npm run test:integration:ci

# Explicit cleanup
cleanup 0