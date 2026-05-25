import { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';
import { api } from '../../services/api';
import { useAuthStore } from '../../stores/authStore';

export function VerifyOtpPage() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { setTokens } = useAuthStore();
  const [code, setCode] = useState(['', '', '', '', '', '']);
  const [error, setError] = useState('');
  const [timer, setTimer] = useState(90);
  const [loading, setLoading] = useState(false);
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

  useEffect(() => {
    inputRefs.current[0]?.focus();
    const interval = setInterval(() => setTimer((t) => (t > 0 ? t - 1 : 0)), 1000);
    return () => clearInterval(interval);
  }, []);

  const handleInput = (index: number, value: string) => {
    if (!/^\d?$/.test(value)) return;
    const next = [...code];
    next[index] = value;
    setCode(next);
    if (value && index < 5) inputRefs.current[index + 1]?.focus();
  };

  const handleVerify = async () => {
    const otp = code.join('');
    if (otp.length !== 6) return;
    setLoading(true);
    setError('');
    try {
      const res = await api.post('/auth/verify-otp', { userId: state?.userId, code: otp });
      setTokens(res.data.data.accessToken, res.data.data.refreshToken, state?.userId);
      navigate('/onboarding/consent-location');
    } catch (err: any) {
      const detail = err.response?.data;
      if (err.response?.status === 429) setError('Too many attempts. Try again in 30 minutes');
      else setError(detail?.detail || 'Invalid code — please try again');
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    try {
      await api.post('/auth/resend-otp', { userId: state?.userId });
      setTimer(90);
      setCode(['', '', '', '', '', '']);
      setError('');
    } catch { setError('Too many requests — try again in 10 minutes'); }
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-16">
      <div className="w-full max-w-[375px] text-center">
        <h1 className="text-page-title text-text-primary mb-2">Enter your code</h1>
        <p className="text-body-sm text-text-secondary mb-6">
          We sent a 6-digit code to<br /><strong>{state?.identifier || '***'}</strong>
        </p>
        <div className="flex gap-2 justify-center mb-4" role="group" aria-label="OTP input">
          {code.map((digit, i) => (
            <input
              key={i}
              ref={(el) => { inputRefs.current[i] = el; }}
              type="text"
              inputMode="numeric"
              maxLength={1}
              value={digit}
              onChange={(e) => handleInput(i, e.target.value)}
              className="w-12 h-14 border-[1.5px] border-gray-300 rounded-button text-center text-2xl font-bold focus:border-lloyds-green focus:ring-2 focus:ring-lloyds-green/15 focus:outline-none"
              aria-label={`Digit ${i + 1}`}
            />
          ))}
        </div>
        <p className="text-body-sm text-text-secondary mb-4" aria-live="polite">
          {timer > 0 ? `Code expires in ${Math.floor(timer / 60)}:${(timer % 60).toString().padStart(2, '0')}` : 'Code expired'}
        </p>
        {error && <p className="text-caption text-text-error mb-4" role="alert">{error}</p>}
        <Button fullWidth loading={loading} onClick={handleVerify} disabled={code.join('').length !== 6}>
          Verify
        </Button>
        <Button variant="ghost" fullWidth onClick={handleResend} disabled={timer > 0} className="mt-3">
          Resend code
        </Button>
      </div>
    </div>
  );
}
