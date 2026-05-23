import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../stores/authStore';
import { api } from '../../services/api';

export function SettingsPage() {
  const navigate = useNavigate();
  const { clearAuth } = useAuthStore();

  const handleLogout = async () => {
    await api.post('/auth/logout', { deviceId: 'web' }).catch(() => {});
    clearAuth();
    navigate('/onboarding/register');
  };

  return (
    <div className="pb-20">
      <Section title="Notifications">
        <ListItem icon="🔔" label="Notification preferences" onClick={() => navigate('/settings/notifications')} />
      </Section>
      <Section title="Privacy">
        <ListItem icon="📍" label="Location tracking" value="Always" onClick={() => navigate('/settings/location')} />
        <ListItem icon="💳" label="Transaction data" value="Allowed" onClick={() => navigate('/settings/transaction')} />
        <ListItem icon="📥" label="Download my data" onClick={() => navigate('/settings/export')} />
      </Section>
      <Section title="Account">
        <ListItem icon="🔐" label="Biometric login" badge="Enabled" onClick={() => {}} />
        <ListItem icon="🚪" label="Sign out" onClick={handleLogout} />
        <ListItem icon="🗑️" label="Delete account" destructive onClick={() => navigate('/settings/delete')} />
      </Section>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="mb-2">
      <p className="px-4 py-2 text-caption font-bold text-text-secondary uppercase tracking-wide">{title}</p>
      <div className="bg-white">{children}</div>
    </div>
  );
}

function ListItem({ icon, label, value, badge, destructive, onClick }: { icon: string; label: string; value?: string; badge?: string; destructive?: boolean; onClick: () => void }) {
  return (
    <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-4 border-b border-border-light text-left hover:bg-bg-app transition-colors">
      <span className={`w-10 h-10 rounded-[10px] flex items-center justify-center text-lg ${destructive ? 'bg-red-50' : 'bg-lloyds-green-light'}`}>{icon}</span>
      <span className={`flex-1 text-body-sm ${destructive ? 'text-text-error' : ''}`}>{label}</span>
      {value && <span className="text-caption text-text-secondary">{value}</span>}
      {badge && <span className="text-micro font-bold text-lloyds-green bg-lloyds-green-light px-2 py-0.5 rounded-pill">{badge}</span>}
      <span className="text-text-muted">›</span>
    </button>
  );
}
