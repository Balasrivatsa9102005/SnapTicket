import { useState } from "react";
import axiosClient from "../../api/axiosClient";

const BUS_TYPES = ["SEATER", "SLEEPER"];

function BusForm() {
  const [bus, setBus] = useState({
    busNumber: "",
    operatorName: "",
    busType: "SEATER",
    totalSeats: "",
  });
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setMsg("");
    setError("");
    try {
      const res = await axiosClient.post("/createBus", {
        busNumber: bus.busNumber,
        operatorName: bus.operatorName,
        busType: bus.busType,
        totalSeats: Number(bus.totalSeats),
      });
      setMsg(res.data.message || "Bus created");
      setBus({ busNumber: "", operatorName: "", busType: "SEATER", totalSeats: "" });
    } catch (err) {
      setError(err.response?.data?.message || "Create failed");
    }
  };

  return (
    <div className="card">
      <div className="card-title">Create Bus</div>
      {error && <div className="alert alert-error">{error}</div>}
      {msg && <div className="alert alert-success">{msg}</div>}
      <form onSubmit={submit}>
        <div>
          <label>Bus Number</label>
          <input
            value={bus.busNumber}
            required
            placeholder="TN-01-AB-1234"
            onChange={(e) => setBus({ ...bus, busNumber: e.target.value })}
          />
        </div>
        <div>
          <label>Operator Name</label>
          <input
            value={bus.operatorName}
            required
            placeholder="Volvo Travels"
            onChange={(e) => setBus({ ...bus, operatorName: e.target.value })}
          />
        </div>
        <div>
          <label>Bus Type</label>
          <select
            value={bus.busType}
            onChange={(e) => setBus({ ...bus, busType: e.target.value })}
          >
            {BUS_TYPES.map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
        </div>
        <div>
          <label>Total Seats</label>
          <input
            type="number"
            min={1}
            value={bus.totalSeats}
            required
            placeholder="40"
            onChange={(e) => setBus({ ...bus, totalSeats: e.target.value })}
          />
        </div>
        <button>Create Bus</button>
      </form>
    </div>
  );
}

function RouteForm() {
  const [route, setRoute] = useState({ source: "", destination: "", distance: "" });
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setMsg("");
    setError("");
    try {
      const res = await axiosClient.post("/createRoute", {
        source: route.source,
        destination: route.destination,
        distance: Number(route.distance),
      });
      setMsg(res.data.message || "Route created");
      setRoute({ source: "", destination: "", distance: "" });
    } catch (err) {
      setError(err.response?.data?.message || "Create failed");
    }
  };

  return (
    <div className="card">
      <div className="card-title">Create Route</div>
      {error && <div className="alert alert-error">{error}</div>}
      {msg && <div className="alert alert-success">{msg}</div>}
      <form onSubmit={submit}>
        <div>
          <label>Source</label>
          <input value={route.source} required placeholder="Chennai"
            onChange={(e) => setRoute({ ...route, source: e.target.value })} />
        </div>
        <div>
          <label>Destination</label>
          <input value={route.destination} required placeholder="Bangalore"
            onChange={(e) => setRoute({ ...route, destination: e.target.value })} />
        </div>
        <div>
          <label>Distance (km)</label>
          <input type="number" min={1} value={route.distance} required placeholder="350"
            onChange={(e) => setRoute({ ...route, distance: e.target.value })} />
        </div>
        <button>Create Route</button>
      </form>
    </div>
  );
}

function TripForm() {
  const [trip, setTrip] = useState({
    busId: "",
    routeId: "",
    travelDate: "",
    departureTime: "",
    arrivalTime: "",
  });
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setMsg("");
    setError("");
    try {
      const res = await axiosClient.post("/createTrip", {
        busId: Number(trip.busId),
        routeId: Number(trip.routeId),
        travelDate: trip.travelDate,
        departureTime:
          trip.departureTime.length === 5 ? trip.departureTime + ":00" : trip.departureTime,
        arrivalTime:
          trip.arrivalTime.length === 5 ? trip.arrivalTime + ":00" : trip.arrivalTime,
      });
      setMsg(res.data.message || "Trip created");
      setTrip({ busId: "", routeId: "", travelDate: "", departureTime: "", arrivalTime: "" });
    } catch (err) {
      setError(err.response?.data?.message || "Create failed");
    }
  };

  return (
    <div className="card">
      <div className="card-title">Create Trip</div>
      <div className="alert alert-info">
        Get Bus ID and Route ID from the database or from the list endpoints (to be added).
      </div>
      {error && <div className="alert alert-error">{error}</div>}
      {msg && <div className="alert alert-success">{msg}</div>}
      <form onSubmit={submit}>
        <div>
          <label>Bus ID</label>
          <input type="number" min={1} value={trip.busId} required placeholder="1"
            onChange={(e) => setTrip({ ...trip, busId: e.target.value })} />
        </div>
        <div>
          <label>Route ID</label>
          <input type="number" min={1} value={trip.routeId} required placeholder="1"
            onChange={(e) => setTrip({ ...trip, routeId: e.target.value })} />
        </div>
        <div>
          <label>Travel Date</label>
          <input type="date" value={trip.travelDate} required
            onChange={(e) => setTrip({ ...trip, travelDate: e.target.value })} />
        </div>
        <div>
          <label>Departure Time</label>
          <input type="time" value={trip.departureTime} required
            onChange={(e) => setTrip({ ...trip, departureTime: e.target.value })} />
        </div>
        <div>
          <label>Arrival Time</label>
          <input type="time" value={trip.arrivalTime} required
            onChange={(e) => setTrip({ ...trip, arrivalTime: e.target.value })} />
        </div>
        <button>Create Trip</button>
      </form>
    </div>
  );
}

export default function AdminDashboard() {
  const [tab, setTab] = useState("bus");

  return (
    <div className="page">
      <div className="page-header">
        <h1>Admin Dashboard</h1>
        <p>Manage buses, routes, and trips.</p>
      </div>

      <div className="flex gap-1 mb-2">
        <button className={tab === "bus" ? "" : "btn-ghost"} onClick={() => setTab("bus")}>Buses</button>
        <button className={tab === "route" ? "" : "btn-ghost"} onClick={() => setTab("route")}>Routes</button>
        <button className={tab === "trip" ? "" : "btn-ghost"} onClick={() => setTab("trip")}>Trips</button>
      </div>

      {tab === "bus" && <BusForm />}
      {tab === "route" && <RouteForm />}
      {tab === "trip" && <TripForm />}
    </div>
  );
}