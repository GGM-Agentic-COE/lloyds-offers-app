import React from 'react';
import ReactDOM from 'react-dom/client';
import { App } from './App';
import './index.css';

async function bootstrap() {
  if (import.meta.env.DEV) {
    const { worker } = await import('./mocks/browser');
    await worker.start({ onUnhandledRequest: 'bypass' });
    // Auto-authenticate in dev for easy testing
    const { useAuthStore } = await import('./stores/authStore');
    if (!useAuthStore.getState().isAuthenticated) {
      useAuthStore.getState().setTokens('dev-token', 'dev-refresh', 'dev-user-id');
    }
  }
  ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}

bootstrap();
