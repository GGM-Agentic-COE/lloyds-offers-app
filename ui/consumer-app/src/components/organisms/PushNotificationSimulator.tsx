import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLocationStore } from '../../stores/locationStore';

declare global { interface Window { __OFFERS_SCENARIO?: string; } }

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

const travelerNotifications: PushNotification[] = [
  { id: 'tn1', title: '🥃 20% off Johnnie Walker Blue!', body: 'World Duty Free — just 80m ahead. Exclusive airport price!', offerId: 't1' },
  { id: 'tn2', title: '💐 £30 off Chanel No.5', body: 'Heathrow Boutiques on your left. Perfect last-minute gift!', offerId: 't2' },
  { id: 'tn3', title: '🍾 Buy 2 get 1 free — all spirits!', body: 'World Duty Free mega deal. Stock up before you fly!', offerId: 't3' },
  { id: 'tn4', title: '🧳 40% off Samsonite — only 3 left!', body: 'Terminal 5 Shop, 200m ahead. Grab it before boarding!', offerId: 't4' },
  { id: 'tn5', title: '🌸 15% off Jo Malone gift sets', body: 'Jo Malone London boutique. Treat yourself or someone special.', offerId: 't5' },
  { id: 'tn6', title: '🍫 £10 off Toblerone mega pack', body: 'WHSmith Travel — right next to you! Classic airport buy.', offerId: 't6' },
  { id: 'tn7', title: '🔥 Hendricks Gin + free tonics — 20 min!', body: 'World Duty Free exclusive bundle. Hurry, offer expiring!', offerId: 't7' },
];

export function PushNotificationSimulator() {
  const navigate = useNavigate();
  const locationIndex = useLocationStore((s) => s.index);
  const [visible, setVisible] = useState(false);
  const [prevIndex, setPrevIndex] = useState(0);
  const [travelerIndex, setTravelerIndex] = useState(-1);
  const [currentNotif, setCurrentNotif] = useState<PushNotification | null>(null);

  // Detect if traveler scenario
  const isTraveler = window.__OFFERS_SCENARIO === 'traveler';

  // Location-based mode (onthemove): fires on location change
  useEffect(() => {
    if (isTraveler) return;
    if (locationIndex !== prevIndex) {
      setPrevIndex(locationIndex);
      setCurrentNotif(notificationsByLocation[locationIndex]);
      setVisible(true);
      const timer = setTimeout(() => setVisible(false), 6000);
      return () => clearTimeout(timer);
    }
  }, [locationIndex]);

  // Traveler mode: rapid-fire every 2 seconds
  useEffect(() => {
    if (!isTraveler) return;
    const interval = setInterval(() => {
      setTravelerIndex((prev) => {
        const next = prev + 1;
        if (next >= travelerNotifications.length) { clearInterval(interval); return prev; }
        setCurrentNotif(travelerNotifications[next]);
        setVisible(true);
        setTimeout(() => setVisible(false), 1800); // Dismiss slightly before next one
        return next;
      });
    }, 2000);
    // Show first one immediately after 1s
    const initial = setTimeout(() => {
      setTravelerIndex(0);
      setCurrentNotif(travelerNotifications[0]);
      setVisible(true);
      setTimeout(() => setVisible(false), 1800);
    }, 1000);
    return () => { clearInterval(interval); clearTimeout(initial); };
  }, [isTraveler]);

  const handleTap = () => {
    if (currentNotif) {
      setVisible(false);
      navigate(`/offers/${currentNotif.offerId}`);
    }
  };

  if (!visible || !currentNotif) return null;

  return (
    <div onClick={handleTap} className="fixed top-4 left-4 right-4 max-w-[400px] mx-auto z-50 animate-slide-down cursor-pointer">
      <div className="bg-white rounded-2xl shadow-[0_8px_40px_rgba(0,0,0,0.15)] p-4 border border-border-light">
        <div className="flex items-start gap-3">
          <div className="w-9 h-9 rounded-lg bg-lloyds-green flex items-center justify-center text-white text-sm font-bold flex-shrink-0">♞</div>
          <div className="flex-1 min-w-0">
            <div className="flex items-center justify-between mb-1">
              <p className="text-micro text-text-secondary">{isTraveler ? 'Lloyds Offers · Heathrow T5' : 'Lloyds Offers · now'}</p>
              <button onClick={(e) => { e.stopPropagation(); setVisible(false); }} className="text-text-muted text-lg leading-none hover:text-text-primary">×</button>
            </div>
            <p className="text-body-sm font-bold text-text-primary">{currentNotif.title}</p>
            <p className="text-caption text-text-secondary mt-0.5">{currentNotif.body}</p>
          </div>
        </div>
        <p className="text-micro text-text-muted text-center mt-2">Tap to view offer</p>
      </div>
    </div>
  );
}
