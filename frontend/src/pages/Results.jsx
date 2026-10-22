import { useEffect, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import axiosClient from "../api/axiosClient";
import TripTicket from "../components/TripTicket";

export default function Results() {
  const [params] = useSearchParams();
  const source = params.get("source") || "";
  const destination = params.get("destination") || "";
  const date = params.get("date") || "";
  const passengers = Number(params.get("passengers") || 1);

  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    setError("");
    const q = new URLSearchParams({ source, destination });
    if (date) q.set("date", date);
    axiosClient
      .get(`/trips/search?${q.toString()}`)
      .then((res) => setTrips(res.data))
      .catch((err) =>
        setError(err.response?.data?.message || "Failed to load trips")
      )
      .finally(() => setLoading(false));
  }, [source, destination, date]);

  return (
    <div className="page">
      <div className="page-header">
        <h1>
          {source} <span style={{ color: "var(--ink-400)" }}>→</span> {destination}
        </h1>
        <p>
          {date ? `On ${date}` : "All dates"} · {passengers}{" "}
          {passengers === 1 ? "passenger" : "passengers"}
        </p>
      </div>

      <div style={{ display: "flex", gap: "0.75rem", marginBottom: "1.5rem" }}>
        <Link to="/" className="btn-sm btn-plain">← New search</Link>
      </div>

      {loading && (
        <div className="state-msg">
          <span className="spinner dark" /> Finding your trips...
        </div>
      )}

      {error && <div className="alert alert-error">{error}</div>}

      {!loading && !error && trips.length === 0 && (
        <div className="state-msg">
          <span className="big-emoji">😕</span>
          <h3>No trips found</h3>
          <p className="text-muted mt-1">
            Try a different date or check back later.
          </p>
        </div>
      )}

      {!loading && trips.length > 0 && (
        <div className="results-section">
          <div className="results-header">
            <h2>Available trips</h2>
            <span className="results-count">
              {trips.length} {trips.length === 1 ? "trip" : "trips"} found
            </span>
          </div>
          <div className="trip-list">
            {trips.map((trip) => (
              <TripTicket
                key={trip.id}
                trip={trip}
                passengers={passengers}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}