import { create } from 'zustand';

interface LocationState {
  index: number;
  tick: () => void;
}

export const useLocationStore = create<LocationState>((set) => ({
  index: 0,
  tick: () => set((s) => ({ index: (s.index + 1) % 5 })),
}));

// Start location simulation (every 15s) when imported
let started = false;
export function startLocationSimulation() {
  if (started) return;
  started = true;
  setInterval(() => {
    useLocationStore.getState().tick();
  }, 15000);
}
