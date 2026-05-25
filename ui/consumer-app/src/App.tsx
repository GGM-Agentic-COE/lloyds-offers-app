import { createBrowserRouter, RouterProvider, Navigate, Outlet } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuthStore } from './stores/authStore';
import { PushNotificationSimulator } from './components/organisms/PushNotificationSimulator';
import { RegisterPage } from './pages/onboarding/RegisterPage';
import { VerifyOtpPage } from './pages/onboarding/VerifyOtpPage';
import { FeatureOptInPage } from './pages/onboarding/FeatureOptInPage';
import { LocationConsentPage, TransactionConsentPage } from './pages/onboarding/ConsentPage';
import { PushPermissionPage } from './pages/onboarding/PushPermissionPage';
import { OffersPage } from './pages/offers/OffersPage';
import { OfferDetailPage } from './pages/offers/OfferDetailPage';
import { RedeemPage } from './pages/offers/RedeemPage';
import { UnsupportedCityPage } from './pages/offers/UnsupportedCityPage';
import { RatingPage } from './pages/offers/RatingPage';
import { HistoryPage } from './pages/history/HistoryPage';
import { ProfilePage } from './pages/profile/ProfilePage';
import { SettingsPage } from './pages/settings/SettingsPage';
import { NotificationPrefsPage } from './pages/settings/NotificationPrefsPage';
import { LocationSettingsPage, TransactionOptOutPage, DataExportPage, DeleteAccountPage } from './pages/settings/PrivacyPages';

const queryClient = new QueryClient({ defaultOptions: { queries: { retry: 1, staleTime: 30_000 } } });

function AuthGuard() {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  return isAuthenticated ? <Outlet /> : <Navigate to="/onboarding/opt-in" replace />;
}

function AppShell() {
  return (
    <div className="max-w-[430px] mx-auto min-h-screen bg-bg-app relative">
      <PushNotificationSimulator />
      <Outlet />
      <nav className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-[430px] h-[60px] bg-white border-t border-border-light flex" aria-label="Main navigation">
        <NavTab to="/offers" icon="🏷️" label="Offers" />
        <NavTab to="/history" icon="🕐" label="History" />
        <NavTab to="/profile" icon="👤" label="Profile" />
        <NavTab to="/settings" icon="⚙️" label="Settings" />
      </nav>
    </div>
  );
}

function NavTab({ to, icon, label }: { to: string; icon: string; label: string }) {
  return (
    <a href={to} className="flex-1 flex flex-col items-center justify-center gap-0.5 text-text-muted text-[11px] hover:text-lloyds-green">
      <span className="text-xl">{icon}</span>{label}
    </a>
  );
}

const router = createBrowserRouter([
  { path: '/onboarding/opt-in', element: <FeatureOptInPage /> },
  { path: '/onboarding/register', element: <RegisterPage /> },
  { path: '/onboarding/verify', element: <VerifyOtpPage /> },
  { path: '/onboarding/consent-location', element: <LocationConsentPage /> },
  { path: '/onboarding/consent-transaction', element: <TransactionConsentPage /> },
  { path: '/onboarding/push-permission', element: <PushPermissionPage /> },
  {
    element: <AuthGuard />,
    children: [{
      element: <AppShell />,
      children: [
        { path: '/offers', element: <OffersPage /> },
        { path: '/offers/unsupported', element: <UnsupportedCityPage /> },
        { path: '/offers/:offerId', element: <OfferDetailPage /> },
        { path: '/offers/:offerId/redeem', element: <RedeemPage /> },
        { path: '/offers/:offerId/rate', element: <RatingPage /> },
        { path: '/history', element: <HistoryPage /> },
        { path: '/profile', element: <ProfilePage /> },
        { path: '/settings', element: <SettingsPage /> },
        { path: '/settings/notifications', element: <NotificationPrefsPage /> },
        { path: '/settings/location', element: <LocationSettingsPage /> },
        { path: '/settings/transaction', element: <TransactionOptOutPage /> },
        { path: '/settings/export', element: <DataExportPage /> },
        { path: '/settings/delete', element: <DeleteAccountPage /> },
      ],
    }],
  },
  { path: '/', element: <Navigate to="/offers" replace /> },
]);

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  );
}
