import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button } from '../../components/atoms/Button';
import { api } from '../../services/api';

export function RatingPage() {
  const { offerId } = useParams();
  const navigate = useNavigate();
  const [stars, setStars] = useState(0);
  const [review, setReview] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async () => {
    await api.post(`/offers/${offerId}/rate`, { stars, reviewText: review || undefined });
    setSubmitted(true);
  };

  if (submitted) {
    return (
      <div className="min-h-screen bg-bg-app flex flex-col items-center justify-center px-4 text-center">
        <div className="w-16 h-16 rounded-full bg-lloyds-green-light flex items-center justify-center text-3xl mb-4">✓</div>
        <h1 className="text-page-title text-text-primary mb-2">Thanks for your feedback!</h1>
        <p className="text-body-sm text-text-secondary mb-6">Your rating helps improve offers for everyone.</p>
        <Button onClick={() => navigate('/history')}>Back to history</Button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-bg-app flex flex-col items-center px-4 pt-16">
      <div className="w-full max-w-[375px]">
        <h1 className="text-page-title text-text-primary mb-1">How was Pizza Express?</h1>
        <p className="text-body-sm text-text-secondary mb-6">Your feedback helps improve offers for everyone</p>

        <div className="flex gap-2 justify-center mb-6">
          {[1, 2, 3, 4, 5].map((n) => (
            <button key={n} onClick={() => setStars(n)} className="text-[36px] transition-transform hover:scale-110"
              aria-label={`${n} star${n > 1 ? 's' : ''}`}>
              {n <= stars ? <span className="text-lloyds-green">★</span> : <span className="text-gray-300">☆</span>}
            </button>
          ))}
        </div>

        <label className="text-body-sm font-bold mb-1.5 block">Tell us more (optional)</label>
        <textarea
          value={review}
          onChange={(e) => setReview(e.target.value.slice(0, 200))}
          placeholder="What did you think of the offer?"
          className="w-full h-20 px-4 py-3 border border-gray-300 rounded-button text-body-sm resize-none focus:border-lloyds-green focus:ring-2 focus:ring-lloyds-green/15 focus:outline-none"
        />
        <p className="text-micro text-text-muted text-right mb-4">{review.length}/200</p>

        <Button fullWidth onClick={handleSubmit} disabled={stars === 0}>Submit</Button>
        <Button variant="ghost" fullWidth className="mt-2" onClick={() => navigate('/history')}>Skip</Button>
      </div>
    </div>
  );
}
