import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../../services/api';

export function NotificationPrefsPage() {
  const { data } = useQuery({
    queryKey: ['notif-prefs'],
    queryFn: async () => (await api.get('/users/me/preferences/notifications')).data.data,
  });

  const [mode, setMode] = useState(data?.mode || 'always');
  const [categories, setCategories] = useState<Record<string, boolean>>(data?.categories || { dining: true, retail: true, entertainment: true, health_beauty: true, travel: false });

  const modes = [
    { value: 'always', label: 'Always', desc: 'Push notifications even when app is closed' },
    { value: 'app_open', label: 'App open only', desc: 'In-app notifications only' },
    { value: 'off', label: 'Off', desc: 'No notifications' },
  ];

  const toggleCategory = (key: string) => {
    setCategories((prev) => ({ ...prev, [key]: !prev[key] }));
    api.patch('/users/me/preferences/notifications', { categories: { ...categories, [key]: !categories[key] } });
  };

  const changeMode = (newMode: string) => {
    setMode(newMode);
    api.patch('/users/me/preferences/notifications', { mode: newMode });
  };

  return (
    <div className="min-h-screen bg-bg-app pb-20">
      <div className="bg-white border-b border-border-light px-4 py-3 flex items-center gap-3">
        <a href="/settings" className="text-xl text-text-secondary">‹</a>
        <h1 className="text-card-heading font-bold">Notifications</h1>
      </div>
      <div className="px-4 pt-4">
        <p className="text-body-sm font-bold mb-3">Delivery mode</p>
        <div className="flex flex-col gap-2.5 mb-6">
          {modes.map((m) => (
            <button key={m.value} onClick={() => changeMode(m.value)}
              className={`flex items-center gap-3 p-4 rounded-card border-2 text-left ${mode === m.value ? 'border-lloyds-green bg-lloyds-green-light' : 'border-border-light bg-white'}`}>
              <div className={`w-5 h-5 rounded-full border-2 flex-shrink-0 ${mode === m.value ? 'border-lloyds-green bg-lloyds-green shadow-[inset_0_0_0_3px_white]' : 'border-gray-300'}`} />
              <div>
                <p className="text-body-sm font-semibold">{m.label}</p>
                <p className="text-caption text-text-secondary">{m.desc}</p>
              </div>
            </button>
          ))}
        </div>

        <p className="text-body-sm font-bold mb-3">Quiet hours</p>
        <div className="flex gap-3 mb-6">
          <div className="flex-1">
            <label className="text-micro text-text-secondary">From</label>
            <input type="time" defaultValue="22:00" className="w-full h-11 px-3 border border-gray-300 rounded-button text-body-sm mt-1" />
          </div>
          <div className="flex-1">
            <label className="text-micro text-text-secondary">To</label>
            <input type="time" defaultValue="08:00" className="w-full h-11 px-3 border border-gray-300 rounded-button text-body-sm mt-1" />
          </div>
        </div>

        <p className="text-body-sm font-bold mb-3">Categories</p>
        <div className="bg-white rounded-card border border-border-light">
          {Object.entries({ dining: '🍽️ Dining', retail: '🛍️ Retail', entertainment: '🎬 Entertainment', health_beauty: '💇 Health & Beauty', travel: '✈️ Travel' }).map(([key, label]) => (
            <div key={key} className="flex items-center justify-between px-4 py-3.5 border-b border-border-light last:border-0">
              <span className="text-body-sm">{label}</span>
              <button onClick={() => toggleCategory(key)}
                className={`w-12 h-[26px] rounded-full relative transition-colors ${categories[key] ? 'bg-lloyds-green' : 'bg-gray-300'}`}>
                <div className={`w-5 h-5 bg-white rounded-full absolute top-[3px] transition-transform shadow ${categories[key] ? 'translate-x-[26px]' : 'translate-x-[3px]'}`} />
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
