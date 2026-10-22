import { useEffect, useState } from "react";
import axiosClient from "../api/axiosClient";
import { useAuth } from "../context/AuthContext";

export default function MyBookings() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const load = () => {
    if (!user?.userId) {
      setError(
        "userId missing from JWT. Add .claim(\"userId\", user.getId()) in JwtService."
      );
      setLoading(false);
      return;
    }
    setLoading(true);
    axiosClient
      .get(`/booking/user/${user.userId}`)
      .then((res) => setBookings(res.data))
      .catch((err) =>
        setError(err.response?.data?.message || "Failed to load bookings")
      )
      .finally(() => setLoading(false));
  };

  useEffect(load, [user]);

  const cancel = async (id) => {
    if (!window.confirm("Cancel this booking?")) return;
    try {
      await axiosClient.delete(`/booking/${id}`);
      load();
    } catch (err) {
      alert(err.response?.data?.message || "Cancel failed");
    }
  };

  if (loading) {
    return (
      <div className="page">
        <div className="state-msg">
          <span className="spinner" /> Loading...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="page">
        <div className="alert alert-error">{error}</div>
      </div>
    );
  }

  if (!bookings.length) {
    return (
      <div className="page">
        <div className="page-header">
          <h1>My Bookings</h1>
        </div>
        <div className="state-msg">No bookings yet.</div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="page-header">
        <h1>My Bookings</h1>
        <p>{bookings.length} booking(s)</p>
      </div>
      {bookings.map((b) => (
        <div className="data-row" key={b.bookingId}>
          <div className="row-main">
            <div className="row-title">Booking #{b.bookingId}</div>
            <div className="row-sub">
              Trip #{b.tripId} · Seats: {b.seatIds?.join(", ")}
            </div>
          </div>
          <div className="row-actions">
            <button
              className="btn-danger btn-sm"
              onClick={() => cancel(b.bookingId)}
            >
              Cancel
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}