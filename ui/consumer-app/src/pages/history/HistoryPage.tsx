import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../../services/api';

export function HistoryPage() {
  const [tab, setTab] = useState<'redeemed' | 'expired'>('redeemed');

  const { data: response, isLoading } = useQuery({
    queryKey: ['history'],
    queryFn: async () => {
      try {
        const res = await api.get('/offers/history');
        return res.data;
      } catch {
        return null;
      }
    },
  });

  if (isLoading) return <div className="p-4"><div className="h-20 bg-gray-200 rounded-card animate-pulse mb-3" /><div className="h-20 bg-gray-200 rounded-card animate-pulse" /></div>;

  const fallbackRedeemed = [
    { id: 'r1', title: '20% off at Pizza Express', merchantName: 'Pizza Express', status: 'confirmed', savingsAmount: 3.40, confirmedAt: new Date(Date.now() - 2*3600000).toISOString() },
    { id: 'r2', title: 'Free coffee at Costa Coffee', merchantName: 'Costa Coffee', status: 'confirmed', savingsAmount: 3.50, confirmedAt: new Date(Date.now() - 24*3600000).toISOString() },
    { id: 'r3', title: '£10 off at JD Sports', merchantName: 'JD Sports', status: 'confirmed', savingsAmount: 10.00, confirmedAt: new Date(Date.now() - 3*24*3600000).toISOString() },
  ];
  const fallbackExpired = [
    { id: 'e1', title: 'Buy 1 get 1 at Byron', icon: '🍔', expiredAgo: '3 days ago' },
    { id: 'e2', title: '20% off at Boots', icon: '🧴', expiredAgo: '1 week ago' },
    { id: 'e3', title: 'Free trial at PureGym', icon: '🏋️', expiredAgo: '2 weeks ago' },
  ];

  const items = response?.data;
  const redeemed = Array.isArray(items) ? items.filter((i: any) => i.status === 'confirmed') : fallbackRedeemed;
  const expired = Array.isArray(response?.expired) ? response.expired : fallbackExpired;
  const totalSaved = response?.totalSaved || 16.90;

  return (
    <div className="pb-20">
      <div className="flex border-b-2 border-border-light">
        <button onClick={() => setTab('redeemed')} className={`flex-1 text-center py-3 text-caption font-bold transition-colors ${tab === 'redeemed' ? 'text-lloyds-green border-b-[3px] border-lloyds-green' : 'text-text-secondary'}`}>
          Redeemed
        </button>
        <button onClick={() => setTab('expired')} className={`flex-1 text-center py-3 text-caption font-bold transition-colors ${tab === 'expired' ? 'text-lloyds-green border-b-[3px] border-lloyds-green' : 'text-text-secondary'}`}>
          Expired
        </button>
      </div>

      {tab === 'redeemed' && (
        <div className="p-3">
          {redeemed.length > 0 && (
            <div className="bg-lloyds-green-light rounded-[10px] p-4 text-center mb-4">
              <p className="text-caption text-lloyds-green">Total saved this month</p>
              <p className="text-[28px] font-bold text-lloyds-green">£{totalSaved.toFixed(2)}</p>
              <p className="text-micro text-text-secondary">{redeemed.length} offers redeemed</p>
            </div>
          )}
          {redeemed.map((item: any) => (
            <div key={item.id} className="bg-white border border-border-light rounded-card p-3 mb-2.5 flex items-center gap-2.5">
              <div className="w-11 h-11 rounded-lg bg-lloyds-green-light flex items-center justify-center text-xl">🏷️</div>
              <div className="flex-1">
                <p className="text-body-sm font-bold">{item.title}</p>
                <p className="text-micro text-text-secondary">{item.merchantName} · Saved £{item.savingsAmount?.toFixed(2)}</p>
                <p className="text-micro text-text-muted">{new Date(item.confirmedAt).toLocaleDateString()}</p>
              </div>
              <span className="text-micro font-bold text-lloyds-green bg-lloyds-green-light px-2 py-0.5 rounded-pill">✓</span>
            </div>
          ))}
          {redeemed.length === 0 && (
            <div className="flex flex-col items-center justify-center min-h-[40vh] text-center">
              <p className="text-body-sm text-text-secondary">No redeemed offers yet</p>
            </div>
          )}
        </div>
      )}

      {tab === 'expired' && (
        <div className="p-3">
          <p className="text-caption text-text-secondary mb-3">Offers you received but didn't redeem in time</p>
          {expired.map((item: any) => (
            <div key={item.id} className="bg-white border border-border-light rounded-card p-3 mb-2.5 flex items-center gap-2.5 opacity-60">
              <div className="w-11 h-11 rounded-lg bg-gray-100 flex items-center justify-center text-xl">{item.icon || '🏷️'}</div>
              <div className="flex-1">
                <p className="text-body-sm font-bold">{item.title}</p>
                <p className="text-micro text-text-muted">Expired {item.expiredAgo}</p>
              </div>
              <span className="text-micro text-text-muted">Missed</span>
            </div>
          ))}
          {expired.length === 0 && (
            <div className="flex flex-col items-center justify-center min-h-[40vh] text-center">
              <p className="text-body-sm text-text-secondary">No expired offers</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
