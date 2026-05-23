import { useQuery } from '@tanstack/react-query';
import { api } from '../../services/api';

const ALL_CATEGORIES = ['🍽️ Dining', '🛍️ Retail', '🎬 Entertainment', '💇 Health & Beauty', '✈️ Travel', '🔧 Services', '🛒 Grocery'];

export function ProfilePage() {
  const { data: profile } = useQuery({
    queryKey: ['profile'],
    queryFn: async () => (await api.get('/users/me/profile')).data.data,
  });

  const { data: user } = useQuery({
    queryKey: ['user'],
    queryFn: async () => (await api.get('/users/me')).data.data,
  });

  return (
    <div className="pb-20 px-4 pt-4">
      <div className="bg-white border border-border-light rounded-card p-5 text-center mb-4">
        <div className="w-16 h-16 rounded-full bg-lloyds-green-light flex items-center justify-center text-2xl text-lloyds-green mx-auto mb-3">👤</div>
        <p className="text-card-heading font-bold">{user?.displayName || 'User'}</p>
        <p className="text-caption text-text-secondary">{user?.identifier || ''}</p>
      </div>

      <p className="text-body-sm font-bold mb-3">Spending categories</p>
      <div className="flex flex-wrap gap-1.5 mb-5">
        {ALL_CATEGORIES.map((cat) => {
          const key = cat.split(' ')[1]?.toLowerCase();
          const selected = profile?.categories?.includes(key);
          return (
            <span key={cat} className={`inline-block px-3 py-1.5 rounded-pill text-caption border ${selected ? 'bg-lloyds-green-light border-lloyds-green text-lloyds-green-dark' : 'bg-white border-border-light text-text-secondary'}`}>
              {cat}
            </span>
          );
        })}
      </div>

      <p className="text-body-sm font-bold mb-3">Preferences</p>
      <div className="bg-white border border-border-light rounded-card">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <span className="text-body-sm">Budget sensitivity</span>
          <span className="text-caption text-text-secondary capitalize">{profile?.budgetSensitivity || 'Medium'}</span>
        </div>
        <div className="flex items-center justify-between p-4">
          <span className="text-body-sm">Preferred radius</span>
          <span className="text-caption text-text-secondary">{((profile?.preferredRadiusMeters || 2000) / 1000).toFixed(1)} km</span>
        </div>
      </div>
    </div>
  );
}
