import { describe, it, expect, beforeEach } from 'vitest';
import { useAuthStore } from '../stores/authStore';

describe('authStore', () => {
  beforeEach(() => {
    useAuthStore.setState({ accessToken: null, refreshToken: null, userId: null, isAuthenticated: false });
  });

  it('sets tokens and marks authenticated', () => {
    useAuthStore.getState().setTokens('access-123', 'refresh-456', 'user-789');
    const state = useAuthStore.getState();
    expect(state.accessToken).toBe('access-123');
    expect(state.refreshToken).toBe('refresh-456');
    expect(state.userId).toBe('user-789');
    expect(state.isAuthenticated).toBe(true);
  });

  it('clears auth state on logout', () => {
    useAuthStore.getState().setTokens('a', 'r', 'u');
    useAuthStore.getState().clearAuth();
    const state = useAuthStore.getState();
    expect(state.accessToken).toBeNull();
    expect(state.isAuthenticated).toBe(false);
  });
});
