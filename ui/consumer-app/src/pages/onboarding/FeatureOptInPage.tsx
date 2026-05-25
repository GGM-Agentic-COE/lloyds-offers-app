import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';

export function FeatureOptInPage() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-12">
      <div className="w-full max-w-[375px] text-center">
        <span className="inline-block bg-lloyds-green-light text-lloyds-green-dark text-micro font-bold px-3.5 py-1.5 rounded-pill mb-4">NEW</span>
        <div className="flex justify-center mb-6">
          <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl">🎁</div>
        </div>
        <h1 className="text-page-title text-text-primary mb-2">Location-Based Offers</h1>
        <p className="text-body-sm text-text-secondary mb-6">
          Get personalised deals from nearby merchants as you go about your day. Exclusive to select Lloyds customers.
        </p>
        <div className="bg-white border border-border-light rounded-card p-4 text-left mb-6">
          <p className="text-caption font-bold mb-2">What you'll get:</p>
          <p className="text-caption text-text-secondary">
            ✓ Real-time offers based on your location<br/>
            ✓ Personalised to your spending habits<br/>
            ✓ Redeem instantly with QR code<br/>
            ✓ Full control over privacy settings
          </p>
        </div>
        <Button fullWidth onClick={() => navigate('/onboarding/register')}>Enable this feature</Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => {}}>Maybe later</Button>
        <p className="text-micro text-text-muted mt-4">You can disable this anytime in your Lloyds app settings</p>
      </div>
    </div>
  );
}
