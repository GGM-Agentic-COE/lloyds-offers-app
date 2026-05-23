import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLocationStore } from '../../stores/locationStore';

interface PushNotification {
  id: string;
  title: string;
  body: string;
  offerId: string;
}

const notificationsByLocation: PushNotification[] = [
  { id: 'n1', title: '🛍️ 30% off near you!', body: 'Topshop on Oxford Street has 30% off everything. Just 80m away!', offerId: 'loc1-1' },
  { id: 'n2', title: '🍸 2-for-1 cocktails nearby!', body: 'Soho House has 2-for-1 cocktails this evening. 100m from you.', offerId: 'loc2-1' },
  { id: 'n3', title: '☕ Free coffee 50m away!', body: 'Monmouth Coffee is offering a free coffee with any pastry. Walk in now!', offerId: 'loc3-3' },
  { id: 'n4', title: '🎬 BOGOF at BFI tonight!', body: 'Buy one get one free on any screening at BFI Southbank. 350m away.', offerId: 'loc4-3' },
  { id: 'n5', title: '🥧 25% off at Padella!', body: 'Fresh pasta at Padella with 25% off. Only 120m — but it fills up fast!', offerId: 'loc5-3' },
];

export function PushNotificationSimulator() {
  const navigate = useNavigate();
  const locationIndex = useLocationStore((s) => s.index);
  const [visible, setVisible] = useState(false);
  const [prevIndex, setPrevIndex] = useState(0);

  useEffect(() => {
    if (locationIndex !== prevIndex) {
      setPrevIndex(locationIndex);
      setVisible(true);
      const timer = setTimeout(() => setVisible(false), 6000);
      return () => clearTimeout(timer);
    }
  }, [locationIndex]);

  const notification = notificationsByLocation[locationIndex];

  const handleTap = () => {
    setVisible(false);
    navigate(`/offers/${notification.offerId}`);
  };

  if (!visible || !notification) return null;

  return (
    <div onClick={handleTap} className="fixed top-4 left-4 right-4 max-w-[400px] mx-auto z-50 animate-slide-down cursor-pointer">
      <div className="bg-white rounded-2xl shadow-[0_8px_40px_rgba(0,0,0,0.15)] p-4 border border-border-light">
        <div className="flex items-start gap-3">
          <div className="w-9 h-9 rounded-lg bg-lloyds-green flex items-center justify-center text-white text-sm font-bold flex-shrink-0">♞</div>
          <div className="flex-1 min-w-0">
            <div className="flex items-center justify-between mb-1">
              <p className="text-micro text-text-secondary">Lloyds Offers · now</p>
              <button onClick={(e) => { e.stopPropagation(); setVisible(false); }} className="text-text-muted text-lg leading-none hover:text-text-primary">×</button>
            </div>
            <p className="text-body-sm font-bold text-text-primary">{notification.title}</p>
            <p className="text-caption text-text-secondary mt-0.5">{notification.body}</p>
          </div>
        </div>
        <p className="text-micro text-text-muted text-center mt-2">Tap to view offer</p>
      </div>
    </div>
  );
}
