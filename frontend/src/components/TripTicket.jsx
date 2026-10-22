import { useNavigate } from "react-router-dom";
import { computePrice, formatINR } from "../lib/pricing";

export default function TripTicket({ trip, passengers = 1 }) {
  const navigate = useNavigate();
  const { base, total } = computePrice(trip, passengers);

  return (
    <div className="ticket">
      <div className="ticket-main">
        <div className="ticket-route">
          <div className="ticket-endpoint">
            <span className="time">{trip.departureTime?.slice(0, 5)}</span>
            <span className="city">{trip.source}</span>
            <span className="date">{trip.travelDate}</span>
          </div>
          <div className="ticket-arrow">
            <span className="dot" />
            <span className="line" />
            <span className="dot" />
          </div>
          <div className="ticket-endpoint" style={{ textAlign: "right" }}>
            <span className="time">{trip.arrivalTime?.slice(0, 5)}</span>
            <span className="city">{trip.destination}</span>
            <span className="date">{trip.distance} km</span>
          </div>
        </div>

        <div className="ticket-meta">
          <span>🚌 <strong>{trip.busNumber}</strong> · {trip.operatorName}</span>
          <span className="badge badge-brand">{trip.busType}</span>
          <span>💺 {base ? formatINR(base) : "—"} / seat</span>
          <span>🧾 Trip #{trip.id}</span>
        </div>
      </div>

      <div className="ticket-stub">
        <div className="ticket-price">
          <span className="cur">₹</span>{total}
          <span className="per">
            {passengers > 1 ? `for ${passengers} passengers` : "per seat"}
          </span>
        </div>
        <button onClick={() => navigate(`/book/${trip.id}?passengers=${passengers}`)}>
          Select seats
        </button>
      </div>
    </div>
  );
}