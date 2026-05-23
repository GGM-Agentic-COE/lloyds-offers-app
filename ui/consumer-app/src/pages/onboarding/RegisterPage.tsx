import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '../../components/atoms/Button';
import { Input } from '../../components/atoms/Input';
import { api } from '../../services/api';

const schema = z.object({
  identifier: z.string().min(1, 'Required').refine(
    (val) => /^\+\d{10,15}$/.test(val) || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val),
    'Please enter a valid phone number or email address'
  ),
});

type FormData = z.infer<typeof schema>;

export function RegisterPage() {
  const navigate = useNavigate();
  const [serverError, setServerError] = useState('');
  const [loading, setLoading] = useState(false);

  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: FormData) => {
    setLoading(true);
    setServerError('');
    try {
      const identifierType = data.identifier.startsWith('+') ? 'phone' : 'email';
      const res = await api.post('/auth/register', {
        identifier: data.identifier,
        identifierType,
        deviceId: 'web-' + crypto.randomUUID().slice(0, 8),
      });
      navigate('/onboarding/verify', { state: { userId: res.data.data.userId, identifier: data.identifier } });
    } catch (err: any) {
      if (err.response?.status === 409) setServerError('An account with this already exists — sign in instead');
      else if (err.response?.status === 429) setServerError('Too many attempts — please try again later');
      else setServerError('Something went wrong — please try again');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-16">
      <div className="w-full max-w-[375px]">
        <div className="flex justify-center mb-6">
          <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl">📍</div>
        </div>
        <h1 className="text-page-title text-text-primary text-center mb-2">Get offers near you</h1>
        <p className="text-body-sm text-text-secondary text-center mb-8">
          Create an account to receive personalised deals from local merchants based on your location and preferences.
        </p>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Input
            label="Phone number or email"
            placeholder="+44 7700 900000 or email@example.com"
            error={errors.identifier?.message}
            helper="We'll send a verification code"
            {...register('identifier')}
          />
          {serverError && (
            <p className="text-caption text-text-error mb-4" role="alert">{serverError}</p>
          )}
          <Button type="submit" fullWidth loading={loading}>Continue</Button>
        </form>
        <p className="text-center text-caption text-text-secondary mt-4">
          Already have an account? <a href="/login" className="text-lloyds-green font-bold">Sign in</a>
        </p>
      </div>
    </div>
  );
}
