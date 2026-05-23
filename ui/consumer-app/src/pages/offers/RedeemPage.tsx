import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { api } from '../../services/api';
import { Button } from '../../components/atoms/Button';

export function RedeemPage() {
  const { offerId } = useParams();
  const navigate = useNavigate();
  const [redemption, setRedemption] = useState<{ redemptionToken: string; manualCode: string; expiresAt: string } | null>(null);
  const [confirmed, setConfirmed] = useState(false);
  const [timer, setTimer] = useState(300);

  const redeemMutation = useMutation({
    mutationFn: async () => (await api.post(`/offers/${offerId}/redeem`)).data.data,
    onSuccess: (data) => setRedemption(data),
  });

  useEffect(() => {
    redeemMutation.mutate();
  }, []);

  useEffect(() => {
    if (!redemption || confirmed) return;
    const interval = setInterval(() => {
      setTimer((t) => {
        if (t <= 1) { clearInterval(interval); return 0; }
        return t - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, [redemption, confirmed]);

  // Simulate merchant confirmation after 5 seconds (for demo)
  useEffect(() => {
    if (!redemption) return;
    const timeout = setTimeout(() => setConfirmed(true), 5000);
    return () => clearTimeout(timeout);
  }, [redemption]);

  if (confirmed) {
    return (
      <div className="min-h-screen bg-bg-app flex flex-col items-center justify-center px-4 text-center">
        <div className="w-20 h-20 rounded-full bg-lloyds-green-light flex items-center justify-center text-4xl mb-5">✓</div>
        <h1 className="text-page-title text-text-primary mb-2">Offer redeemed!</h1>
        <p className="text-[24px] font-bold text-lloyds-green mb-2">You saved £3.40</p>
        <p className="text-body-sm text-text-secondary mb-8">20% off at Pizza Express<br/>Redeemed just now</p>
        <Button fullWidth onClick={() => navigate('/offers')}>Back to offers</Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => navigate('/history')}>View in history</Button>
      </div>
    );
  }

  if (!redemption) {
    return <div className="flex items-center justify-center min-h-screen"><div className="w-8 h-8 border-2 border-lloyds-green/30 border-t-lloyds-green rounded-full animate-spin" /></div>;
  }

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-12 text-center">
      <p className="text-body-sm text-text-secondary mb-5">Show this to the cashier</p>
      <div className="w-[220px] h-[220px] bg-white border-2 border-border-light rounded-card flex items-center justify-center mb-4">
        <div className="w-[180px] h-[180px] bg-[repeating-conic-gradient(#1A1A1A_0%_25%,#fff_0%_50%)] bg-[length:20px_20px] rounded opacity-80" />
      </div>
      <p className="text-caption text-text-secondary mb-1">Or enter code manually:</p>
      <p className="text-[20px] font-bold tracking-widest mb-5">{redemption.manualCode}</p>
      <div className={`rounded-button px-4 py-3 flex items-center gap-2 ${timer > 60 ? 'bg-amber-50' : 'bg-red-50'}`}>
        <span className="text-lg">⏱️</span>
        <span className={`text-body-sm font-bold ${timer > 60 ? 'text-status-pending' : 'text-text-error'}`}>
          Expires in {Math.floor(timer / 60)}:{(timer % 60).toString().padStart(2, '0')}
        </span>
      </div>
      <p className="text-body-sm font-bold mt-5">20% off at Pizza Express</p>
      <p className="text-caption text-text-secondary">High Street branch</p>
      <p className="text-caption text-text-muted mt-8 animate-pulse">Waiting for merchant to scan...</p>
    </div>
  );
}
