export const RATE_PER_KM = {
  SEATER: 1.2,
  SLEEPER: 2.0,
};

export function computePrice(trip, passengers = 1) {
  if (!trip) return { base: 0, total: 0, rate: 0 };
  const rate = RATE_PER_KM[trip.busType] ?? RATE_PER_KM.SEATER;
  const base = Math.round(trip.distance * rate);
  const total = base * Math.max(1, passengers);
  return { base, total, rate };
}

export function formatINR(amount) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(amount);
}