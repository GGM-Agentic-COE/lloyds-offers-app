import { type InputHTMLAttributes, forwardRef } from 'react';
import { clsx } from 'clsx';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helper?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, helper, className, id, ...props }, ref) => {
    const inputId = id || label?.toLowerCase().replace(/\s/g, '-');
    return (
      <div className="flex flex-col gap-1.5 mb-5">
        {label && (
          <label htmlFor={inputId} className="text-body-sm font-bold text-text-primary">
            {label}
          </label>
        )}
        <input
          ref={ref}
          id={inputId}
          className={clsx(
            'w-full h-[52px] px-4 border-[1.5px] rounded-button text-body text-text-primary bg-white transition-colors',
            'placeholder:text-text-muted focus:outline-none focus:border-lloyds-green focus:ring-2 focus:ring-lloyds-green/15',
            error ? 'border-text-error ring-2 ring-text-error/10' : 'border-gray-300',
            props.disabled && 'bg-bg-app text-text-muted cursor-not-allowed',
            className
          )}
          aria-invalid={!!error}
          aria-describedby={error ? `${inputId}-error` : helper ? `${inputId}-helper` : undefined}
          {...props}
        />
        {error && (
          <p id={`${inputId}-error`} className="text-caption text-text-error flex items-center gap-1" role="alert">
            <span>⚠</span> {error}
          </p>
        )}
        {helper && !error && (
          <p id={`${inputId}-helper`} className="text-caption text-text-secondary">{helper}</p>
        )}
      </div>
    );
  }
);
Input.displayName = 'Input';
