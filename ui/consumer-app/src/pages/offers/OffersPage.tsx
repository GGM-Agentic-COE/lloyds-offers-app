import { useQuery } from '@tanstack/react-query';
import { api } from '../../services/api';
import { Button } from '../../components/atoms/Button';
import { useNavigate } from 'react-router-dom';
import { useLocationStore, startLocationSimulation } from '../../stores/locationStore';

interface Offer {
  id: string;
  title: string;
  merchantName: string;
  discountType: string;
  discountValue: number;
  distanceMeters: number;
  expiresAt: string;
  imageUrl?: string;
}

export function OffersPage() {
  const navigate = useNavigate();
  const locationIndex = useLocationStore((s) => s.index);

  // Start simulation on mount
  startLocationSimulation();

  const { data, isLoading, error } = useQuery({
    queryKey: ['offers', locationIndex],
    queryFn: async () => {
      const res = await api.get('/offers', { params: { sort: 'nearest', pageSize: 20, locationIndex } });
      return res.data;
    },
    refetchInterval: 2000, // Poll every 2s to pick up new offers (traveler) or location changes
  });

  if (isLoading) return <OffersSkeleton />;
  if (error) return <OffersError onRetry={() => window.location.reload()} />;

  const offers = data?.data as Offer[] || [];
  const location = data?.meta?.location;
  const locationDesc = data?.meta?.description;

  if (!offers.length) return <OffersEmpty />;

  return (
    <div className="pb-20">
      <div className="bg-lloyds-green rounded-modal p-6 mx-4 mt-4 mb-4 text-white">
        <p className="text-caption opacity-80">📍 {location ? `You're near ${location}` : 'Near you now'}</p>
        <p className="text-section-heading font-bold">{offers.length} offers available</p>
        <p className="text-caption opacity-70">{locationDesc || 'Within 2 km · Updated just now'}</p>
      </div>
      <div className="flex flex-col gap-3 px-4">
        {offers.map((offer) => {
          const minsLeft = Math.round((new Date(offer.expiresAt).getTime() - Date.now()) / 60000);
          const isUrgent = minsLeft <= 30;
          return (
          <button key={offer.id} onClick={() => navigate(`/offers/${offer.id}`)}
            className={`bg-white border rounded-card overflow-hidden text-left hover:shadow-md transition-shadow ${isUrgent ? 'border-amber-300 ring-1 ring-amber-200' : 'border-border-light'}`}>
            <div className="p-4">
              <div className="flex justify-between items-start">
                <div>
                  <p className="text-body-sm font-bold">{offer.title}</p>
                  <p className="text-caption text-text-secondary mt-0.5">
                    {Math.round(offer.distanceMeters)}m away · {offer.merchantName}
                  </p>
                </div>
                <span className="bg-lloyds-green-light text-lloyds-green-dark text-micro font-bold px-2.5 py-0.5 rounded-pill">
                  {offer.discountType === 'PERCENTAGE' ? `${offer.discountValue}% off` : offer.discountType === 'FIXED' ? `£${offer.discountValue} off` : 'BOGOF'}
                </span>
              </div>
              {isUrgent && (
                <div className="mt-2 bg-amber-50 rounded px-2.5 py-1.5 flex items-center gap-1.5">
                  <span className="text-sm">⏰</span>
                  <span className="text-micro font-bold text-status-pending">Expires in {minsLeft} min — act now!</span>
                </div>
              )}
            </div>
          </button>
          );
        })}
      </div>
    </div>
  );
}

function OffersSkeleton() {
  return (
    <div className="px-4 pt-4 flex flex-col gap-3">
      <div className="h-28 bg-gray-200 rounded-modal animate-pulse" />
      {[1, 2, 3].map((i) => <div key={i} className="h-32 bg-gray-200 rounded-card animate-pulse" />)}
    </div>
  );
}

function OffersEmpty() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] px-6 text-center">
      <div className="w-16 h-16 rounded-2xl bg-lloyds-green-light flex items-center justify-center text-3xl mb-4">📍</div>
      <p className="text-section-heading font-bold mb-2">No offers yet</p>
      <p className="text-body-sm text-text-secondary max-w-[260px]">We're setting up your personalised offers. Check back soon!</p>
    </div>
  );
}

function OffersError({ onRetry }: { onRetry: () => void }) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] px-6 text-center">
      <div className="w-16 h-16 rounded-2xl bg-red-50 flex items-center justify-center text-3xl mb-4">⚠️</div>
      <p className="text-section-heading font-bold mb-2">Something went wrong</p>
      <p className="text-body-sm text-text-secondary max-w-[260px] mb-6">We couldn't load your offers. Please try again.</p>
      <Button onClick={onRetry}>Try again</Button>
    </div>
  );
}
