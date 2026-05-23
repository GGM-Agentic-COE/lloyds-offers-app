import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';
import { api } from '../../services/api';

interface Option { value: string; label: string; description: string; }

const locationOptions: Option[] = [
  { value: 'always', label: 'Always', description: 'Get notified about deals even when the app is closed' },
  { value: 'app_open', label: 'Only when using the app', description: 'Offers shown only while you\'re browsing' },
  { value: 'off', label: 'Not now', description: 'You\'ll see all offers in your city without proximity sorting' },
];

export function LocationConsentPage() {
  const navigate = useNavigate();
  const [selected, setSelected] = useState('always');
  const [loading, setLoading] = useState(false);

  const handleContinue = async () => {
    setLoading(true);
    await api.post('/auth/consent', { consentType: 'location', decision: selected, version: '1.0' });
    setLoading(false);
    navigate('/onboarding/consent-transaction');
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-12">
      <div className="w-full max-w-[375px]">
        <h1 className="text-page-title text-text-primary mb-2">Location access</h1>
        <p className="text-body-sm text-text-secondary mb-6">
          We use your location to show offers from nearby merchants. Choose how you'd like this to work:
        </p>
        <div className="flex flex-col gap-3 mb-6">
          {locationOptions.map((opt) => (
            <button
              key={opt.value}
              onClick={() => setSelected(opt.value)}
              className={`flex items-center gap-3 p-4 rounded-card border-2 text-left transition-colors ${
                selected === opt.value ? 'border-lloyds-green bg-lloyds-green-light' : 'border-border-light bg-white'
              }`}
              role="radio"
              aria-checked={selected === opt.value}
            >
              <div className={`w-5 h-5 rounded-full border-2 flex-shrink-0 ${
                selected === opt.value ? 'border-lloyds-green bg-lloyds-green shadow-[inset_0_0_0_3px_white]' : 'border-gray-300'
              }`} />
              <div>
                <p className="text-body-sm font-semibold">{opt.label}</p>
                <p className="text-caption text-text-secondary">{opt.description}</p>
              </div>
            </button>
          ))}
        </div>
        <Button fullWidth loading={loading} onClick={handleContinue}>Continue</Button>
        <p className="text-micro text-text-muted text-center mt-3">
          You can change this anytime in Settings. <a href="#" className="text-lloyds-green">Privacy policy</a>
        </p>
      </div>
    </div>
  );
}

export function TransactionConsentPage() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleConsent = async (decision: 'allow' | 'deny') => {
    setLoading(true);
    await api.post('/auth/consent', { consentType: 'transaction', decision, version: '1.0' });
    setLoading(false);
    navigate('/onboarding/push-permission');
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-12">
      <div className="w-full max-w-[375px]">
        <div className="flex justify-center mb-6">
          <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl">💳</div>
        </div>
        <h1 className="text-page-title text-text-primary text-center mb-2">Personalise your offers</h1>
        <p className="text-body-sm text-text-secondary text-center mb-6">
          Allow us to analyse your spending patterns to show you more relevant deals.
        </p>
        <div className="bg-white border border-border-light rounded-card p-4 mb-3">
          <p className="text-caption font-bold mb-2">What we use:</p>
          <p className="text-caption text-text-secondary">✓ Spending categories<br/>✓ Average amounts<br/>✓ Frequency patterns</p>
        </div>
        <div className="bg-white border border-border-light rounded-card p-4 mb-6">
          <p className="text-caption font-bold mb-2">What we never access:</p>
          <p className="text-caption text-text-secondary">✗ Card numbers<br/>✗ Specific merchants<br/>✗ Account balance</p>
        </div>
        <Button fullWidth loading={loading} onClick={() => handleConsent('allow')}>Allow</Button>
        <Button variant="ghost" fullWidth onClick={() => handleConsent('deny')} className="mt-2">No thanks</Button>
      </div>
    </div>
  );
}
