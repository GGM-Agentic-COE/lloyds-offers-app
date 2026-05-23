import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { Input } from '../components/atoms/Input';

describe('Input', () => {
  it('renders with label', () => {
    render(<Input label="Email" />);
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
  });

  it('shows error message with alert role', () => {
    render(<Input label="Phone" error="Invalid phone number" />);
    expect(screen.getByRole('alert')).toHaveTextContent('Invalid phone number');
  });

  it('shows helper text when no error', () => {
    render(<Input label="Code" helper="We'll send a code" />);
    expect(screen.getByText("We'll send a code")).toBeInTheDocument();
  });

  it('hides helper when error is shown', () => {
    render(<Input label="Code" helper="Helper" error="Error" />);
    expect(screen.queryByText('Helper')).not.toBeInTheDocument();
    expect(screen.getByRole('alert')).toHaveTextContent('Error');
  });

  it('sets aria-invalid when error exists', () => {
    render(<Input label="Field" error="Bad" />);
    expect(screen.getByLabelText('Field')).toHaveAttribute('aria-invalid', 'true');
  });

  it('applies disabled styles', () => {
    render(<Input label="Disabled" disabled />);
    expect(screen.getByLabelText('Disabled')).toBeDisabled();
  });
});
