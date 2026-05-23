import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';
import { useAuthStore } from '../../stores/authStore';

export function PushPermissionPage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();

  const handleEnable = () => {
    // In production: trigger OS push permission dialog
    // On grant: register device token via POST /api/v1/devices
    navigate('/offers');
  };

  const handleSkip = () => {
    navigate('/offers');
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-16">
      <div className="w-full max-w-[375px] text-center">
        <div className="flex justify-center mb-6">
          <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl">🔔</div>
        </div>
        <h1 className="text-page-title text-text-primary mb-2">Don't miss a deal</h1>
        <p className="text-body-sm text-text-secondary mb-6">
          Enable notifications to get alerted when there's a great offer near you. We'll never spam — max 5 per day.
        </p>
        <div className="bg-white border border-border-light rounded-card p-4 text-left mb-6">
          <p className="text-caption text-text-primary mb-2">✓ Offers within your preferred radius</p>
          <p className="text-caption text-text-primary mb-2">✓ Max 5 notifications per day</p>
          <p className="text-caption text-text-primary">✓ Quiet hours respected</p>
        </div>
        <Button fullWidth onClick={handleEnable}>Enable notifications</Button>
        <Button variant="ghost" fullWidth onClick={handleSkip} className="mt-3">Maybe later</Button>
      </div>
    </div>
  );
}
