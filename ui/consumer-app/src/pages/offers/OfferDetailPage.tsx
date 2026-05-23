import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { api } from '../../services/api';
import { Button } from '../../components/atoms/Button';

export function OfferDetailPage() {
  const { offerId } = useParams();
  const navigate = useNavigate();

  const { data: offer, isLoading } = useQuery({
    queryKey: ['offer', offerId],
    queryFn: async () => (await api.get(`/offers/${offerId}`)).data.data,
  });

  if (isLoading) return <div className="p-4"><div className="h-40 bg-gray-200 rounded-card animate-pulse mb-4" /><div className="h-8 bg-gray-200 rounded animate-pulse" /></div>;
  if (!offer) return null;

  return (
    <div className="pb-20">
      <div className="h-[140px] bg-lloyds-green-light rounded-b-[16px] flex items-center justify-center text-6xl">🏷️</div>
      <div className="px-4 -mt-4">
        <div className="flex gap-2 mb-2">
          <span className="bg-lloyds-green-light text-lloyds-green-dark text-micro font-bold px-2.5 py-1 rounded-pill">
            {offer.discountType === 'PERCENTAGE' ? `${offer.discountValue}% off` : `£${offer.discountValue} off`}
          </span>
          <span className="bg-amber-50 text-status-pending text-micro font-bold px-2.5 py-1 rounded-pill">
            Expires {new Date(offer.expiresAt).toLocaleDateString()}
          </span>
        </div>
        <h1 className="text-page-title text-text-primary mt-3">{offer.title}</h1>
        <p className="text-body-sm text-text-secondary mt-1">{offer.merchantName} · {offer.distanceMeters}m away</p>

        {offer.averageRating && (
          <div className="flex items-center gap-1 mt-2">
            <span className="text-lloyds-green">{'★'.repeat(Math.round(offer.averageRating))}</span>
            <span className="text-caption text-text-secondary">{offer.averageRating.toFixed(1)} ({offer.ratingCount} reviews)</span>
          </div>
        )}

        <div className="bg-white border border-border-light rounded-card p-4 mt-5">
          <p className="text-body-sm font-bold mb-2">Terms</p>
          <p className="text-caption text-text-secondary leading-relaxed">{offer.terms || offer.description}</p>
        </div>

        <div className="bg-white border border-border-light rounded-card p-4 mt-3 flex items-center gap-3">
          <span className="text-2xl">📍</span>
          <div>
            <p className="text-body-sm font-bold">{offer.merchantName}</p>
            <p className="text-caption text-text-secondary">{offer.merchantAddress}</p>
          </div>
        </div>

        <Button fullWidth className="mt-5" onClick={() => navigate(`/offers/${offerId}/redeem`)} disabled={offer.alreadyRedeemed}>
          {offer.alreadyRedeemed ? 'Already redeemed' : 'Redeem this offer'}
        </Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => {}}>Share with a friend</Button>
      </div>
    </div>
  );
}
