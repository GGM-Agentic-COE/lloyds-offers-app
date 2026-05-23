/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        lloyds: {
          green: '#006A4D',
          'green-dark': '#005238',
          'green-light': '#E6F2EE',
          'green-mid': '#00875F',
        },
        surface: '#FFFFFF',
        'bg-app': '#F5F5F5',
        'text-primary': '#1A1A1A',
        'text-secondary': '#595959',
        'text-muted': '#888888',
        'text-error': '#C0392B',
        'border-light': '#E0E0E0',
        'status-positive': '#006A4D',
        'status-negative': '#C0392B',
        'status-pending': '#9C5A00',
        'status-info': '#1565C0',
      },
      borderRadius: { card: '12px', button: '8px', modal: '16px', pill: '9999px' },
      spacing: { 'touch-min': '44px' },
      fontSize: {
        display: ['32px', { lineHeight: '1.2', fontWeight: '700' }],
        'page-title': ['24px', { lineHeight: '1.25', fontWeight: '700' }],
        'section-heading': ['20px', { lineHeight: '1.3', fontWeight: '700' }],
        'card-heading': ['17px', { lineHeight: '1.35', fontWeight: '700' }],
        body: ['16px', { lineHeight: '1.6' }],
        'body-sm': ['14px', { lineHeight: '1.5' }],
        caption: ['13px', { lineHeight: '1.4' }],
        micro: ['12px', { lineHeight: '1.4' }],
      },
    },
  },
  plugins: [],
};
