import { useEffect, useState } from "react";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import axiosClient from "../api/axiosClient";
import { computePrice, formatINR } from "../lib/pricing";
import toast from "react-hot-toast";

export default function CreateBooking() {
  const { tripId } = useParams();
  const [searchParams] = useSearchParams();
  const passengers = Number(searchParams.get("passengers") || 1);
  
  const navigate = useNavigate();

  const [trip, setTrip] = useState(null);
  const [seats, setSeats] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([
      axiosClient.get(`/findTrip/${tripId}`),
      axiosClient.get(`/trips/${tripId}/seats`)
    ])
      .then(([tripRes, seatsRes]) => {
        setTrip(tripRes.data);
        setSeats(seatsRes.data);
      })
      .catch((err) => {
        setError(err.response?.data?.message || "Failed to load booking details");
      })
      .finally(() => setLoading(false));
  }, [tripId]);

  const toggleSeat = (seat) => {
    if (seat.booked) return;
    
    setSelectedSeats((prev) => {
      const isSelected = prev.some((s) => s.id === seat.id);
      if (isSelected) {
        return prev.filter((s) => s.id !== seat.id);
      }
      if (prev.length >= passengers) {
        toast.error(`You can only select ${passengers} seat(s).`);
        return prev;
      }
      return [...prev, seat];
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (selectedSeats.length !== passengers) {
      toast.error(`Please select exactly ${passengers} seat(s).`);
      return;
    }
    
    setSubmitting(true);
    try {
      const res = await axiosClient.post("/createBooking", {
        tripId: Number(tripId),
        seatIds: selectedSeats.map((s) => s.id),
      });
      toast.success("Booking successful! 🎉");
      navigate("/my-bookings");
    } catch (err) {
      toast.error(err.response?.data?.message || "Booking failed");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="page"><div className="state-msg"><span className="spinner dark" /> Loading bus layout...</div></div>;
  if (error) return <div className="page"><div className="alert alert-error">{error}</div></div>;
  if (!trip) return null;

  const rows = seats.reduce((acc, seat) => {
    const match = seat.seatNumber.match(/\d+/);
    const rowNum = match ? match[0] : seat.seatNumber;
    
    if (!acc[rowNum]) acc[rowNum] = [];
    acc[rowNum].push(seat);
    return acc;
  }, {});

  const { total } = computePrice(trip, selectedSeats.length);

  return (
    <div className="page">
      <div className="page-header">
        <h1>Select Seats</h1>
        <p>{trip.source} → {trip.destination} · {passengers} Passenger(s)</p>
      </div>

      <div className="two-col">
        <div className="card" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          
          <div className="bus-layout">
            <div className="bus-front">
              <span>Front</span>
              <div className="steering-wheel">🛞</div>
            </div>

            {Object.keys(rows).map((rowNum) => {
              const rowSeats = rows[rowNum].sort((a, b) => a.seatNumber.localeCompare(b.seatNumber));
              const leftSeats = rowSeats.slice(0, 2);
              const rightSeats = rowSeats.slice(2, 4);
              
              return (
                <div key={rowNum} className="bus-row">
                  <div className="seat-group">
                    {leftSeats.map(seat => (
                      <button
                        key={seat.id}
                        type="button"
                        disabled={seat.booked}
                        onClick={() => toggleSeat(seat)}
                        className={`seat ${seat.booked ? 'booked' : ''} ${selectedSeats.some(s => s.id === seat.id) ? 'selected' : ''}`}
                      >
                        {seat.seatNumber}
                      </button>
                    ))}
                  </div>
                  
                  <div className="aisle" />
                  
                  <div className="seat-group">
                    {rightSeats.map(seat => (
                      <button
                        key={seat.id}
                        type="button"
                        disabled={seat.booked}
                        onClick={() => toggleSeat(seat)}
                        className={`seat ${seat.booked ? 'booked' : ''} ${selectedSeats.some(s => s.id === seat.id) ? 'selected' : ''}`}
                      >
                        {seat.seatNumber}
                      </button>
                    ))}
                  </div>
                </div>
              );
            })}
            
            <div className="bus-rear">Rear</div>
          </div>

          <div className="seat-legend">
            <span><span className="swatch" /> Available</span>
            <span><span className="swatch selected" /> Selected</span>
            <span><span className="swatch booked" /> Booked</span>
          </div>
        </div>

        <div className="booking-summary">
          <h3>Booking Summary</h3>
          
          <div className="summary-row">
            <span>Seats selected</span>
            <strong>{selectedSeats.length} / {passengers}</strong>
          </div>
          
          <div className="summary-row">
            <span>Selected IDs</span>
            <strong>
              {selectedSeats.length > 0 
                ? selectedSeats.map(s => s.seatNumber).join(", ") 
                : "None"}
            </strong>
          </div>

          <div className="summary-row">
            <span>Price per seat</span>
            <strong>{formatINR(computePrice(trip, 1).base)}</strong>
          </div>

          <div className="summary-row summary-total">
            <span>Total</span>
            <span>{formatINR(total)}</span>
          </div>

          <button 
            onClick={handleSubmit} 
            disabled={submitting || selectedSeats.length !== passengers}
            style={{ width: '100%', marginTop: '1rem' }}
          >
            {submitting ? "Processing..." : "Confirm & Pay"}
          </button>
        </div>
      </div>
    </div>
  );
}