import { createBrowserRouter, RouterProvider, Outlet, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

function DashboardLayout() {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="h-16 bg-white border-b border-gray-200 flex items-center px-8">
        <span className="text-lg font-bold text-[#006A4D]">♞ Lloyds Offers — Merchant Portal</span>
      </header>
      <div className="flex">
        <nav className="w-60 bg-white border-r border-gray-200 min-h-[calc(100vh-64px)] p-4">
          <a href="/campaigns" className="block px-4 py-2 rounded-lg text-sm hover:bg-gray-100">Campaigns</a>
          <a href="/analytics" className="block px-4 py-2 rounded-lg text-sm hover:bg-gray-100">Analytics</a>
          <a href="/settings" className="block px-4 py-2 rounded-lg text-sm hover:bg-gray-100">Settings</a>
        </nav>
        <main className="flex-1 p-8"><Outlet /></main>
      </div>
    </div>
  );
}

function CampaignsPage() {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Campaigns</h1>
        <button className="bg-[#006A4D] text-white px-6 py-3 rounded-lg font-bold">+ New Campaign</button>
      </div>
      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <p className="text-gray-500">No campaigns yet. Create your first campaign to start reaching customers.</p>
      </div>
    </div>
  );
}

function AnalyticsPage() {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Analytics</h1>
      <div className="grid grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-xl border p-6 text-center">
          <p className="text-3xl font-bold text-[#006A4D]">0</p><p className="text-sm text-gray-500">Impressions</p>
        </div>
        <div className="bg-white rounded-xl border p-6 text-center">
          <p className="text-3xl font-bold text-[#006A4D]">0</p><p className="text-sm text-gray-500">Redemptions</p>
        </div>
        <div className="bg-white rounded-xl border p-6 text-center">
          <p className="text-3xl font-bold text-[#006A4D]">0%</p><p className="text-sm text-gray-500">Conversion</p>
        </div>
      </div>
    </div>
  );
}

const router = createBrowserRouter([
  { path: '/login', element: <div className="flex items-center justify-center min-h-screen"><p>Merchant Login</p></div> },
  { element: <DashboardLayout />, children: [
    { path: '/campaigns', element: <CampaignsPage /> },
    { path: '/analytics', element: <AnalyticsPage /> },
    { path: '/', element: <Navigate to="/campaigns" replace /> },
  ]},
]);

export function App() {
  return <QueryClientProvider client={queryClient}><RouterProvider router={router} /></QueryClientProvider>;
}
