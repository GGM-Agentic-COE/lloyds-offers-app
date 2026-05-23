import { type ButtonHTMLAttributes, forwardRef } from 'react';
import { clsx } from 'clsx';

type Variant = 'primary' | 'secondary' | 'ghost' | 'destructive';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant;
  loading?: boolean;
  fullWidth?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', loading, fullWidth, className, children, disabled, ...props }, ref) => {
    const base = 'inline-flex items-center justify-center gap-2 font-bold text-body rounded-button h-[52px] px-7 transition-colors min-w-[120px] focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-lloyds-green';
    const variants: Record<Variant, string> = {
      primary: 'bg-lloyds-green text-white hover:bg-lloyds-green-dark disabled:bg-gray-300 disabled:text-gray-500',
      secondary: 'bg-transparent text-lloyds-green border-2 border-lloyds-green hover:bg-lloyds-green-light disabled:border-gray-300 disabled:text-gray-300',
      ghost: 'bg-transparent text-lloyds-green underline underline-offset-2 hover:text-lloyds-green-dark border-none min-w-0 px-4',
      destructive: 'bg-text-error text-white hover:bg-red-800 disabled:bg-gray-300',
    };

    return (
      <button
        ref={ref}
        className={clsx(base, variants[variant], fullWidth && 'w-full', className)}
        disabled={disabled || loading}
        aria-busy={loading}
        {...props}
      >
        {loading && <span className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />}
        {children}
      </button>
    );
  }
);
Button.displayName = 'Button';
