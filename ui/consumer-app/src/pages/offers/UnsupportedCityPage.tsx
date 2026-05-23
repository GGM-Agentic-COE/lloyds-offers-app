import { Button } from '../../components/atoms/Button';

export function UnsupportedCityPage() {
  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center justify-center px-4 text-center pb-20">
      <div className="w-[120px] h-[120px] rounded-full bg-lloyds-green-light flex items-center justify-center text-5xl mb-6">🌍</div>
      <h1 className="text-page-title text-text-primary mb-2">Coming soon to your area</h1>
      <p className="text-body-sm text-text-secondary max-w-[280px] mb-6">
        We're not in your city yet, but we're growing fast! Get notified when we launch near you.
      </p>
      <Button onClick={() => alert('You\'ll be notified when we launch in your area!')}>Notify me when available</Button>
      <Button variant="ghost" className="mt-3" onClick={() => window.location.href = '/offers'}>Browse all offers</Button>
    </div>
  );
}
