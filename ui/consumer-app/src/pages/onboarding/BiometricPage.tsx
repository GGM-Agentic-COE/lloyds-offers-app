import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';

export function BiometricPage() {
  const navigate = useNavigate();

  const handleEnable = () => {
    // In production: trigger OS biometric prompt via native bridge
    navigate('/onboarding/consent-location');
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-16">
      <div className="w-full max-w-[375px] text-center">
        <div className="flex justify-center mb-6">
          <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl">🔐</div>
        </div>
        <h1 className="text-page-title text-text-primary mb-2">Enable Face ID</h1>
        <p className="text-body-sm text-text-secondary mb-8">
          Use Face ID to sign in quickly and securely. You can change this later in Settings.
        </p>
        <Button fullWidth onClick={handleEnable}>Enable Face ID</Button>
        <Button variant="secondary" fullWidth onClick={() => navigate('/onboarding/consent-location')} className="mt-3">
          Skip for now
        </Button>
      </div>
    </div>
  );
}
