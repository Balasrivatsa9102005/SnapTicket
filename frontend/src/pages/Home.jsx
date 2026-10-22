import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosClient from "../api/axiosClient";

export default function Home() {
  const navigate = useNavigate();

  const [sources, setSources] = useState([]);
  const [destinations, setDestinations] = useState([]);
  const [form, setForm] = useState({
    source: "",
    destination: "",
    date: "",
    passengers: 1,
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [suggestionsOk, setSuggestionsOk] = useState(true);

  useEffect(() => {
    Promise.allSettled([
      axiosClient.get("/routes/sources"),
      axiosClient.get("/routes/destinations"),
    ]).then(([srcRes, destRes]) => {
      if (srcRes.status === "fulfilled") setSources(srcRes.value.data);
      else setSuggestionsOk(false);
      if (destRes.status === "fulfilled") setDestinations(destRes.value.data);
    });
  }, []);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const swap = () =>
    setForm((f) => ({ ...f, source: f.destination, destination: f.source }));

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");
    if (!form.source || !form.destination) {
      setError("Please select both source and destination.");
      return;
    }
    if (form.source.toLowerCase() === form.destination.toLowerCase()) {
      setError("Source and destination cannot be the same.");
      return;
    }
    const params = new URLSearchParams({
      source: form.source,
      destination: form.destination,
      passengers: String(form.passengers || 1),
    });
    if (form.date) params.set("date", form.date);
    navigate(`/results?${params.toString()}`);
  };

  return (
    <>
      <section className="hero">
        <div className="hero-inner">
          <span className="eyebrow">🚌 Trusted by thousands of travellers</span>
          <h1>
            Planning a trip?{" "}
            <span className="grad">Snap it.</span>
          </h1>
          <p className="hero-sub">
            Search buses, compare fares, and book your seats in seconds —
            all in one place.
          </p>

          <div className="search-card">
            <form onSubmit={handleSubmit}>
              <div className="search-field">
                <label>FROM</label>
                {suggestionsOk && sources.length > 0 ? (
                  <select name="source" value={form.source} onChange={handleChange} required>
                    <option value="">Select source</option>
                    {sources.map((s) => (
                      <option key={s} value={s}>{s}</option>
                    ))}
                  </select>
                ) : (
                  <input
                    name="source"
                    placeholder="e.g. Chennai"
                    value={form.source}
                    onChange={handleChange}
                    required
                  />
                )}
              </div>

              <div className="search-field">
                <label>TO</label>
                {suggestionsOk && destinations.length > 0 ? (
                  <select name="destination" value={form.destination} onChange={handleChange} required>
                    <option value="">Select destination</option>
                    {destinations.map((d) => (
                      <option key={d} value={d}>{d}</option>
                    ))}
                  </select>
                ) : (
                  <input
                    name="destination"
                    placeholder="e.g. Bangalore"
                    value={form.destination}
                    onChange={handleChange}
                    required
                  />
                )}
              </div>

              <div className="search-field">
                <label>DATE</label>
                <input type="date" name="date" value={form.date} onChange={handleChange} />
              </div>

              <div className="search-field">
                <label>SEATS</label>
                <input
                  type="number"
                  name="passengers"
                  min={1}
                  max={10}
                  value={form.passengers}
                  onChange={handleChange}
                />
              </div>

              <button type="submit" className="search-submit" disabled={loading}>
                {loading ? <span className="spinner" /> : "Search"}
              </button>
            </form>
            {error && <div className="alert alert-error mt-1">{error}</div>}
            <p className="search-hint">
              ✨ Pick any route, we'll show real trips sorted by departure time.
            </p>
          </div>
        </div>
      </section>
    </>
  );
}