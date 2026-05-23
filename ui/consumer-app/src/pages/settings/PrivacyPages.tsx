import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';
import { api } from '../../services/api';
import { useAuthStore } from '../../stores/authStore';

export function LocationSettingsPage() {
  const [selected, setSelected] = useState('always');

  const handleSave = async () => {
    await api.post('/auth/consent', { consentType: 'location', decision: selected, version: '1.0' });
    window.history.back();
  };

  const options = [
    { value: 'always', label: 'Always', desc: 'Get notified even when app is closed' },
    { value: 'app_open', label: 'Only when using the app', desc: 'Offers shown while browsing' },
    { value: 'off', label: 'Off', desc: 'No location tracking' },
  ];

  return (
    <div className="min-h-screen bg-bg-app pb-20">
      <div className="bg-white border-b border-border-light px-4 py-3 flex items-center gap-3">
        <a href="/settings" className="text-xl text-text-secondary">‹</a>
        <h1 className="text-card-heading font-bold">Location tracking</h1>
      </div>
      <div className="px-4 pt-4">
        <p className="text-body-sm text-text-secondary mb-4">Choose how we use your location to show nearby offers.</p>
        <div className="flex flex-col gap-2.5 mb-6">
          {options.map((o) => (
            <button key={o.value} onClick={() => setSelected(o.value)}
              className={`flex items-center gap-3 p-4 rounded-card border-2 text-left ${selected === o.value ? 'border-lloyds-green bg-lloyds-green-light' : 'border-border-light bg-white'}`}>
              <div className={`w-5 h-5 rounded-full border-2 flex-shrink-0 ${selected === o.value ? 'border-lloyds-green bg-lloyds-green shadow-[inset_0_0_0_3px_white]' : 'border-gray-300'}`} />
              <div><p className="text-body-sm font-semibold">{o.label}</p><p className="text-caption text-text-secondary">{o.desc}</p></div>
            </button>
          ))}
        </div>
        <Button fullWidth onClick={handleSave}>Save</Button>
      </div>
    </div>
  );
}

export function TransactionOptOutPage() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleOptOut = async () => {
    setLoading(true);
    await api.post('/auth/consent', { consentType: 'transaction', decision: 'deny', version: '1.0' });
    setLoading(false);
    navigate('/settings');
  };

  return (
    <div className="min-h-screen bg-bg-app pb-20">
      <div className="bg-white border-b border-border-light px-4 py-3 flex items-center gap-3">
        <a href="/settings" className="text-xl text-text-secondary">‹</a>
        <h1 className="text-card-heading font-bold">Transaction data</h1>
      </div>
      <div className="px-4 pt-4">
        <div className="bg-lloyds-green rounded-modal p-4 mb-4 text-white">
          <p className="text-caption">Currently</p>
          <p className="text-card-heading font-bold">Sharing enabled</p>
          <p className="text-caption opacity-80 mt-1">Offers personalised based on spending</p>
        </div>
        <div className="bg-white border border-border-light rounded-card p-4 mb-6">
          <p className="text-body-sm font-bold mb-2">If you opt out:</p>
          <p className="text-caption text-text-secondary">• Stop syncing transaction data<br/>• Spending profile deleted<br/>• Category-only matching<br/>• Can opt back in anytime</p>
        </div>
        <Button variant="destructive" fullWidth loading={loading} onClick={handleOptOut}>Stop sharing</Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => navigate('/settings')}>Keep sharing</Button>
      </div>
    </div>
  );
}

export function DataExportPage() {
  const [requested, setRequested] = useState(false);

  const handleExport = async () => {
    await api.get('/users/me/export');
    setRequested(true);
  };

  if (requested) {
    return (
      <div className="min-h-screen bg-bg-app flex flex-col items-center justify-center px-4 text-center">
        <div className="w-16 h-16 rounded-full bg-lloyds-green-light flex items-center justify-center text-3xl mb-4">✓</div>
        <h1 className="text-page-title text-text-primary mb-2">Export requested</h1>
        <p className="text-body-sm text-text-secondary max-w-[280px] mb-6">We'll notify you when your data is ready to download. Usually takes less than 5 minutes.</p>
        <Button onClick={() => window.history.back()}>Back to settings</Button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-bg-app pb-20">
      <div className="bg-white border-b border-border-light px-4 py-3 flex items-center gap-3">
        <a href="/settings" className="text-xl text-text-secondary">‹</a>
        <h1 className="text-card-heading font-bold">Download my data</h1>
      </div>
      <div className="px-4 pt-4 text-center">
        <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl mx-auto mb-6">📥</div>
        <h2 className="text-section-heading font-bold mb-2">Export your data</h2>
        <p className="text-body-sm text-text-secondary mb-6">We'll prepare a JSON file with all your personal data. Usually takes less than 5 minutes.</p>
        <div className="bg-white border border-border-light rounded-card p-4 text-left mb-6">
          <p className="text-caption font-bold mb-2">Included:</p>
          <p className="text-caption text-text-secondary">✓ Profile · ✓ Consents · ✓ Offer history<br/>✓ Redemptions · ✓ Preferences</p>
        </div>
        <Button fullWidth onClick={handleExport}>Generate export</Button>
        <p className="text-micro text-text-muted mt-3">Link valid 24h · Max 1 per 7 days</p>
      </div>
    </div>
  );
}

export function DeleteAccountPage() {
  const navigate = useNavigate();
  const { clearAuth } = useAuthStore();
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleDelete = async () => {
    setLoading(true);
    await api.delete('/users/me');
    setLoading(false);
    clearAuth();
    navigate('/onboarding/register');
  };

  return (
    <div className="min-h-screen bg-bg-app pb-20">
      <div className="bg-white border-b border-border-light px-4 py-3 flex items-center gap-3">
        <a href="/settings" className="text-xl text-text-secondary">‹</a>
        <h1 className="text-card-heading font-bold">Delete account</h1>
      </div>
      <div className="px-4 pt-4">
        <div className="bg-red-50 border border-red-200 rounded-[10px] p-4 mb-5">
          <p className="text-body-sm font-bold text-text-error mb-1">⚠️ This cannot be undone</p>
          <p className="text-caption text-text-error">After 7-day cooling-off, all data permanently deleted within 30 days.</p>
        </div>
        <p className="text-body-sm font-bold mb-2">What will be deleted:</p>
        <p className="text-caption text-text-secondary mb-4">• Profile and preferences<br/>• All consent records<br/>• Offer and redemption history<br/>• Device tokens and sessions</p>
        <p className="text-body-sm font-bold mb-2">7-day cooling-off:</p>
        <p className="text-caption text-text-secondary mb-5">Cancel by logging back in within 7 days.</p>
        <label className="text-body-sm font-bold mb-1.5 block">Confirm with password</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter password"
          className="w-full h-[52px] px-4 border border-gray-300 rounded-button text-body mb-4 focus:border-lloyds-green focus:ring-2 focus:ring-lloyds-green/15 focus:outline-none" />
        <Button variant="destructive" fullWidth loading={loading} onClick={handleDelete} disabled={!password}>Delete my account</Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => navigate('/settings')}>Cancel</Button>
      </div>
    </div>
  );
}
