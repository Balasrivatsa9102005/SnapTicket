import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosClient from "../api/axiosClient";

export default function TripDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    axiosClient
      .get(`/findTrip/${id}`)
      .then((res) => setTrip(res.data))
      .catch((err) =>
        setError(err.response?.data?.message || "Failed to load trip")
      );
  }, [id]);

  if (error) {
    return (
      <div className="page page-narrow">
        <div className="alert alert-error">{error}</div>
      </div>
    );
  }

  if (!trip) {
    return (
      <div className="page page-narrow">
        <div className="state-msg">Loading trip...</div>
      </div>
    );
  }

  return (
    <div className="page page-narrow">
      <div className="page-header">
        <h1>Trip #{trip.id}</h1>
        <p>{trip.source} → {trip.destination}</p>
      </div>
      <div className="trip-card">
        <div className="trip-meta">
          <span><strong>Bus:</strong> {trip.busNumber} ({trip.operatorName})</span>
          <span><strong>Type:</strong> {trip.busType}</span>
          <span><strong>Date:</strong> {trip.travelDate}</span>
          <span><strong>Time:</strong> {trip.departureTime} → {trip.arrivalTime}</span>
          <span><strong>Distance:</strong> {trip.distance} km</span>
        </div>
        <div className="trip-actions">
          <button onClick={() => navigate(`/book/${trip.id}`)}>Book now</button>
        </div>
      </div>
    </div>
  );
}