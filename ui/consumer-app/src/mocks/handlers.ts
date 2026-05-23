import { http, HttpResponse, delay } from 'msw';

/**
 * MOCK SCENARIOS (driven by registration email):
 * 
 * 1. happy@example.com     → Full happy path: offers available, redeemed history, all working
 * 2. network@example.com   → Simulates network errors and timeouts
 * 3. foodie@example.com    → Dining-heavy user in London, dining/cafe offers
 * 4. shopper@example.com   → Retail-heavy user in Manchester, fashion/retail offers
 * 5. traveler@example.com  → Travel user in unsupported city
 * 6. onthemove@example.com → Dynamic: user walking through London, offers change every 15s as location shifts
 * 7. Any other email       → Default happy path
 */

let currentScenario = 'happy';
let mockUserId = 'u-default';

function setScenario(identifier: string) {
  if (identifier.includes('network')) currentScenario = 'network';
  else if (identifier.includes('foodie')) currentScenario = 'foodie';
  else if (identifier.includes('shopper')) currentScenario = 'shopper';
  else if (identifier.includes('traveler')) currentScenario = 'traveler';
  else if (identifier.includes('onthemove')) {
    currentScenario = 'onthemove';
  }
  else currentScenario = 'happy';
}

// Simulated walk through central London — each stop has different nearby merchants
const locationStops = [
  {
    area: 'Oxford Circus',
    description: 'Shopping district — retail & fashion offers',
    offers: [
      { id: 'loc1-1', title: '30% off at Topshop', merchantName: 'Topshop', discountType: 'PERCENTAGE', discountValue: 30, distanceMeters: 80, expiresAt: new Date(Date.now() + 2*3600000).toISOString(), category: 'retail' },
      { id: 'loc1-2', title: '£15 off at Selfridges', merchantName: 'Selfridges', discountType: 'FIXED', discountValue: 15, distanceMeters: 200, expiresAt: new Date(Date.now() + 4*3600000).toISOString(), category: 'retail' },
      { id: 'loc1-3', title: 'Buy 2 get 1 free at Uniqlo', merchantName: 'Uniqlo', discountType: 'BOGOF', discountValue: 0, distanceMeters: 150, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), category: 'retail' },
    ],
  },
  {
    area: 'Soho',
    description: 'Food & nightlife — dining & entertainment offers',
    offers: [
      { id: 'loc2-1', title: '2-for-1 cocktails at Soho House', merchantName: 'Soho House', discountType: 'BOGOF', discountValue: 0, distanceMeters: 100, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), category: 'entertainment' },
      { id: 'loc2-2', title: '25% off at Barrafina', merchantName: 'Barrafina', discountType: 'PERCENTAGE', discountValue: 25, distanceMeters: 180, expiresAt: new Date(Date.now() + 2*3600000).toISOString(), category: 'dining' },
      { id: 'loc2-3', title: 'Free dessert at Yauatcha', merchantName: 'Yauatcha', discountType: 'BOGOF', discountValue: 0, distanceMeters: 250, expiresAt: new Date(Date.now() + 5*3600000).toISOString(), category: 'dining' },
      { id: 'loc2-4', title: '£5 off at Flat Iron', merchantName: 'Flat Iron', discountType: 'FIXED', discountValue: 5, distanceMeters: 120, expiresAt: new Date(Date.now() + 1*3600000).toISOString(), category: 'dining' },
    ],
  },
  {
    area: 'Covent Garden',
    description: 'Theatre & beauty — entertainment & wellness offers',
    offers: [
      { id: 'loc3-1', title: '20% off at Neal\'s Yard', merchantName: 'Neal\'s Yard Remedies', discountType: 'PERCENTAGE', discountValue: 20, distanceMeters: 90, expiresAt: new Date(Date.now() + 6*3600000).toISOString(), category: 'health_beauty' },
      { id: 'loc3-2', title: '£10 off theatre tickets', merchantName: 'TodayTix', discountType: 'FIXED', discountValue: 10, distanceMeters: 300, expiresAt: new Date(Date.now() + 8*3600000).toISOString(), category: 'entertainment' },
      { id: 'loc3-3', title: 'Free coffee at Monmouth', merchantName: 'Monmouth Coffee', discountType: 'BOGOF', discountValue: 0, distanceMeters: 50, expiresAt: new Date(Date.now() + 1*3600000).toISOString(), category: 'dining' },
    ],
  },
  {
    area: 'South Bank',
    description: 'Riverside walk — cafes & cultural offers',
    offers: [
      { id: 'loc4-1', title: '15% off at Tate Modern Shop', merchantName: 'Tate Modern', discountType: 'PERCENTAGE', discountValue: 15, distanceMeters: 200, expiresAt: new Date(Date.now() + 4*3600000).toISOString(), category: 'entertainment' },
      { id: 'loc4-2', title: '£3 off at Gail\'s Bakery', merchantName: 'Gail\'s', discountType: 'FIXED', discountValue: 3, distanceMeters: 100, expiresAt: new Date(Date.now() + 2*3600000).toISOString(), category: 'dining' },
      { id: 'loc4-3', title: 'BOGOF at BFI Southbank', merchantName: 'BFI Southbank', discountType: 'BOGOF', discountValue: 0, distanceMeters: 350, expiresAt: new Date(Date.now() + 6*3600000).toISOString(), category: 'entertainment' },
      { id: 'loc4-4', title: '20% off at Wahaca', merchantName: 'Wahaca', discountType: 'PERCENTAGE', discountValue: 20, distanceMeters: 180, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), category: 'dining' },
    ],
  },
  {
    area: 'Borough Market',
    description: 'Food market — artisan food & drink offers',
    offers: [
      { id: 'loc5-1', title: 'Free tasting at Borough Wines', merchantName: 'Borough Wines', discountType: 'BOGOF', discountValue: 0, distanceMeters: 50, expiresAt: new Date(Date.now() + 1*3600000).toISOString(), category: 'dining' },
      { id: 'loc5-2', title: '£2 off any pie at Pieminister', merchantName: 'Pieminister', discountType: 'FIXED', discountValue: 2, distanceMeters: 80, expiresAt: new Date(Date.now() + 2*3600000).toISOString(), category: 'dining' },
      { id: 'loc5-3', title: '25% off at Padella', merchantName: 'Padella', discountType: 'PERCENTAGE', discountValue: 25, distanceMeters: 120, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), category: 'dining' },
      { id: 'loc5-4', title: 'Free sample at Bread Ahead', merchantName: 'Bread Ahead', discountType: 'BOGOF', discountValue: 0, distanceMeters: 30, expiresAt: new Date(Date.now() + 1*3600000).toISOString(), category: 'dining' },
      { id: 'loc5-5', title: '10% off at The Rooftop', merchantName: 'The Rooftop Bar', discountType: 'PERCENTAGE', discountValue: 10, distanceMeters: 200, expiresAt: new Date(Date.now() + 5*3600000).toISOString(), category: 'entertainment' },
    ],
  },
];

const offersByScenario: Record<string, any[]> = {
  happy: [
    { id: 'o1', title: '20% off at Pizza Express', merchantName: 'Pizza Express', discountType: 'PERCENTAGE', discountValue: 20, distanceMeters: 350, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), category: 'dining' },
    { id: 'o2', title: 'Free coffee with any meal', merchantName: 'Costa Coffee', discountType: 'BOGOF', discountValue: 0, distanceMeters: 500, expiresAt: new Date(Date.now() + 24*3600000).toISOString(), category: 'dining' },
    { id: 'o3', title: '£10 off at JD Sports', merchantName: 'JD Sports', discountType: 'FIXED', discountValue: 10, distanceMeters: 1200, expiresAt: new Date(Date.now() + 5*24*3600000).toISOString(), category: 'retail' },
  ],
  foodie: [
    { id: 'f1', title: '2-for-1 at Wagamama', merchantName: 'Wagamama', discountType: 'BOGOF', discountValue: 0, distanceMeters: 200, expiresAt: new Date(Date.now() + 2*3600000).toISOString(), category: 'dining' },
    { id: 'f2', title: '30% off at Dishoom', merchantName: 'Dishoom', discountType: 'PERCENTAGE', discountValue: 30, distanceMeters: 400, expiresAt: new Date(Date.now() + 4*3600000).toISOString(), category: 'dining' },
    { id: 'f3', title: 'Free dessert at Nando\'s', merchantName: 'Nando\'s', discountType: 'BOGOF', discountValue: 0, distanceMeters: 600, expiresAt: new Date(Date.now() + 6*3600000).toISOString(), category: 'dining' },
    { id: 'f4', title: '£5 off at Pret A Manger', merchantName: 'Pret A Manger', discountType: 'FIXED', discountValue: 5, distanceMeters: 150, expiresAt: new Date(Date.now() + 12*3600000).toISOString(), category: 'dining' },
    { id: 'f5', title: '15% off at The Ivy', merchantName: 'The Ivy', discountType: 'PERCENTAGE', discountValue: 15, distanceMeters: 800, expiresAt: new Date(Date.now() + 48*3600000).toISOString(), category: 'dining' },
  ],
  shopper: [
    { id: 's1', title: '25% off at Zara', merchantName: 'Zara', discountType: 'PERCENTAGE', discountValue: 25, distanceMeters: 300, expiresAt: new Date(Date.now() + 8*3600000).toISOString(), category: 'retail' },
    { id: 's2', title: '£20 off at John Lewis', merchantName: 'John Lewis', discountType: 'FIXED', discountValue: 20, distanceMeters: 700, expiresAt: new Date(Date.now() + 3*24*3600000).toISOString(), category: 'retail' },
    { id: 's3', title: 'Buy 2 get 1 free at H&M', merchantName: 'H&M', discountType: 'BOGOF', discountValue: 0, distanceMeters: 450, expiresAt: new Date(Date.now() + 24*3600000).toISOString(), category: 'retail' },
    { id: 's4', title: '15% off at Boots', merchantName: 'Boots', discountType: 'PERCENTAGE', discountValue: 15, distanceMeters: 250, expiresAt: new Date(Date.now() + 5*3600000).toISOString(), category: 'health_beauty' },
  ],
  traveler: [],
  network: [],
};

const historyByScenario: Record<string, { redeemed: any[]; expired: any[]; totalSaved: number }> = {
  happy: {
    redeemed: [
      { id: 'r1', title: '20% off at Pizza Express', merchantName: 'Pizza Express', status: 'confirmed', savingsAmount: 3.40, confirmedAt: new Date(Date.now() - 2*3600000).toISOString() },
      { id: 'r2', title: 'Free coffee at Costa Coffee', merchantName: 'Costa Coffee', status: 'confirmed', savingsAmount: 3.50, confirmedAt: new Date(Date.now() - 24*3600000).toISOString() },
      { id: 'r3', title: '£10 off at JD Sports', merchantName: 'JD Sports', status: 'confirmed', savingsAmount: 10.00, confirmedAt: new Date(Date.now() - 3*24*3600000).toISOString() },
    ],
    expired: [
      { id: 'e1', title: 'Buy 1 get 1 at Byron', icon: '🍔', expiredAgo: '3 days ago' },
      { id: 'e2', title: '20% off at Boots', icon: '🧴', expiredAgo: '1 week ago' },
      { id: 'e3', title: 'Free trial at PureGym', icon: '🏋️', expiredAgo: '2 weeks ago' },
    ],
    totalSaved: 16.90,
  },
  foodie: {
    redeemed: [
      { id: 'r1', title: '2-for-1 at Wagamama', merchantName: 'Wagamama', status: 'confirmed', savingsAmount: 14.50, confirmedAt: new Date(Date.now() - 3*3600000).toISOString() },
      { id: 'r2', title: '30% off at Dishoom', merchantName: 'Dishoom', status: 'confirmed', savingsAmount: 12.00, confirmedAt: new Date(Date.now() - 2*24*3600000).toISOString() },
      { id: 'r3', title: 'Free dessert at Nando\'s', merchantName: 'Nando\'s', status: 'confirmed', savingsAmount: 5.50, confirmedAt: new Date(Date.now() - 5*24*3600000).toISOString() },
      { id: 'r4', title: '£5 off at Pret', merchantName: 'Pret A Manger', status: 'confirmed', savingsAmount: 5.00, confirmedAt: new Date(Date.now() - 7*24*3600000).toISOString() },
    ],
    expired: [
      { id: 'e1', title: '50% off at Pizza Pilgrims', icon: '🍕', expiredAgo: '2 days ago' },
      { id: 'e2', title: 'Free starter at Hawksmoor', icon: '🥩', expiredAgo: '5 days ago' },
    ],
    totalSaved: 37.00,
  },
  shopper: {
    redeemed: [
      { id: 'r1', title: '25% off at Zara', merchantName: 'Zara', status: 'confirmed', savingsAmount: 22.50, confirmedAt: new Date(Date.now() - 4*3600000).toISOString() },
      { id: 'r2', title: '£20 off at John Lewis', merchantName: 'John Lewis', status: 'confirmed', savingsAmount: 20.00, confirmedAt: new Date(Date.now() - 2*24*3600000).toISOString() },
    ],
    expired: [
      { id: 'e1', title: '30% off at Selfridges', icon: '🛍️', expiredAgo: '1 day ago' },
      { id: 'e2', title: '£15 off at Nike', icon: '👟', expiredAgo: '4 days ago' },
      { id: 'e3', title: 'BOGOF at Lush', icon: '🧴', expiredAgo: '1 week ago' },
      { id: 'e4', title: '20% off at Uniqlo', icon: '👕', expiredAgo: '2 weeks ago' },
    ],
    totalSaved: 42.50,
  },
  traveler: { redeemed: [], expired: [], totalSaved: 0 },
  network: { redeemed: [], expired: [], totalSaved: 0 },
};

export const handlers = [
  // Register — sets scenario based on identifier
  http.post('/api/v1/auth/register', async ({ request }) => {
    const body: any = await request.json();
    setScenario(body.identifier);
    if (currentScenario === 'network') { await delay(12000); return HttpResponse.error(); }
    mockUserId = 'u-' + Math.random().toString(36).slice(2, 10);
    return HttpResponse.json({ data: { userId: mockUserId, verificationRequired: true, expiresIn: 90 } }, { status: 201 });
  }),

  // Verify OTP
  http.post('/api/v1/auth/verify-otp', async ({ request }) => {
    if (currentScenario === 'network') { await delay(10000); return HttpResponse.error(); }
    const body: any = await request.json();
    if (body.code !== '123456') return HttpResponse.json({ detail: 'Invalid code — 4 attempts remaining' }, { status: 401 });
    return HttpResponse.json({ data: { accessToken: 'mock-access-' + Date.now(), refreshToken: 'mock-refresh-' + Date.now(), expiresIn: 900, tokenType: 'Bearer' } });
  }),

  // Resend OTP
  http.post('/api/v1/auth/resend-otp', () => HttpResponse.json({ data: { sent: true, expiresIn: 90 } })),

  // Login
  http.post('/api/v1/auth/login', async ({ request }) => {
    const body: any = await request.json();
    setScenario(body.identifier);
    if (currentScenario === 'network') { await delay(10000); return HttpResponse.error(); }
    return HttpResponse.json({ data: { accessToken: 'mock-access-login', refreshToken: 'mock-refresh-login', expiresIn: 900, tokenType: 'Bearer' } });
  }),

  // Refresh
  http.post('/api/v1/auth/refresh', () => HttpResponse.json({ data: { accessToken: 'mock-refreshed', refreshToken: 'mock-refresh-new', expiresIn: 900, tokenType: 'Bearer' } })),

  // Logout
  http.post('/api/v1/auth/logout', () => HttpResponse.json({ data: { success: true } })),

  // Consent
  http.post('/api/v1/auth/consent', () => HttpResponse.json({ data: { consentId: 'c-' + Math.random().toString(36).slice(2), recordedAt: new Date().toISOString() } }, { status: 201 })),
  http.get('/api/v1/auth/consent', () => HttpResponse.json({ data: { consents: [
    { consentType: 'location', decision: 'always', version: '1.0', recordedAt: new Date().toISOString() },
    { consentType: 'transaction', decision: 'allow', version: '1.0', recordedAt: new Date().toISOString() },
  ]}})),

  // User
  http.get('/api/v1/users/me', () => HttpResponse.json({ data: { id: mockUserId, identifier: currentScenario + '@***', identifierType: 'email', status: 'verified', displayName: currentScenario === 'foodie' ? 'Foodie Fan' : currentScenario === 'shopper' ? 'Style Hunter' : 'Jane', biometricEnabled: true } })),
  http.get('/api/v1/users/me/profile', () => {
    const profiles: Record<string, any> = {
      happy: { categories: ['dining', 'retail', 'entertainment'], budgetSensitivity: 'medium', preferredRadiusMeters: 2000 },
      foodie: { categories: ['dining'], budgetSensitivity: 'high', preferredRadiusMeters: 1000 },
      shopper: { categories: ['retail', 'health_beauty'], budgetSensitivity: 'medium', preferredRadiusMeters: 3000 },
      traveler: { categories: ['travel', 'entertainment'], budgetSensitivity: 'low', preferredRadiusMeters: 5000 },
      network: { categories: ['dining', 'retail'], budgetSensitivity: 'medium', preferredRadiusMeters: 2000 },
    };
    return HttpResponse.json({ data: profiles[currentScenario] || profiles.happy });
  }),
  http.patch('/api/v1/users/me/profile', () => HttpResponse.json({ data: { updated: true } })),
  http.get('/api/v1/users/me/preferences/notifications', () => HttpResponse.json({ data: { mode: 'always', quietHours: { start: '22:00', end: '08:00' }, categories: { dining: true, retail: true, entertainment: true } } })),
  http.patch('/api/v1/users/me/preferences/notifications', () => HttpResponse.json({ data: { updated: true } })),
  http.get('/api/v1/users/me/export', () => HttpResponse.json({ data: { jobId: 'job-123', status: 'processing' } })),
  http.delete('/api/v1/users/me', () => HttpResponse.json({ data: { scheduledAt: new Date(Date.now() + 7*24*3600000).toISOString() } }, { status: 202 })),

  // Offers — scenario-driven
  http.get('/api/v1/offers', async ({ request }) => {
    if (currentScenario === 'network') { await delay(10000); return HttpResponse.error(); }
    if (currentScenario === 'traveler') return HttpResponse.json({ data: [], meta: { page: 1, pageSize: 20, total: 0 } });
    if (currentScenario === 'onthemove') {
      const url = new URL(request.url);
      const idx = parseInt(url.searchParams.get('locationIndex') || '0') % locationStops.length;
      const stop = locationStops[idx];
      return HttpResponse.json({ data: stop.offers, meta: { page: 1, pageSize: 20, total: stop.offers.length, location: stop.area, description: stop.description } });
    }
    const offers = offersByScenario[currentScenario] || offersByScenario.happy;
    return HttpResponse.json({ data: offers, meta: { page: 1, pageSize: 20, total: offers.length } });
  }),

  http.get('/api/v1/offers/:offerId', ({ params }) => {
    // Search all offer sources for the matching ID
    const allOffers = [
      ...offersByScenario.happy, ...offersByScenario.foodie, ...offersByScenario.shopper,
      ...locationStops.flatMap(s => s.offers),
    ];
    const offer = allOffers.find(o => o.id === params.offerId);
    if (!offer) {
      return HttpResponse.json({ data: { id: params.offerId, title: '20% off your bill', description: 'Valid on dine-in orders over £15', terms: 'One per customer.', merchantName: 'Pizza Express', merchantAddress: '123 High Street, London EC2A 4NE', discountType: 'PERCENTAGE', discountValue: 20, distanceMeters: 350, expiresAt: new Date(Date.now() + 3*3600000).toISOString(), averageRating: 4.2, ratingCount: 47, alreadyRedeemed: false } });
    }
    return HttpResponse.json({ data: { ...offer, description: `Exclusive offer at ${offer.merchantName}. Don't miss out!`, terms: 'Valid today only. Cannot be combined with other offers. Show QR code at checkout.', merchantAddress: '123 High Street, London EC2A 4NE', averageRating: 4.0 + Math.random() * 0.9, ratingCount: Math.floor(20 + Math.random() * 80), alreadyRedeemed: false } });
  }),

  http.post('/api/v1/offers/:offerId/redeem', () => HttpResponse.json({ data: { redemptionToken: 'rt-' + Math.random().toString(36).slice(2), manualCode: 'A7K2-M9X4', expiresAt: new Date(Date.now() + 5*60000).toISOString() } })),
  http.post('/api/v1/offers/:offerId/rate', () => HttpResponse.json({ data: { rated: true } })),

  // History — scenario-driven
  http.get('/api/v1/offers/history', async () => {
    if (currentScenario === 'network') { await delay(10000); return HttpResponse.error(); }
    const history = historyByScenario[currentScenario] || historyByScenario.happy;
    return HttpResponse.json({ data: history.redeemed, expired: history.expired, meta: { total: history.redeemed.length }, totalSaved: history.totalSaved });
  }),

  // Health
  http.get('/api/v1/health', () => HttpResponse.json({ status: 'ok' })),
];

