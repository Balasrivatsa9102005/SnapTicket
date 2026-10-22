import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axiosClient from "../api/axiosClient";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await axiosClient.post("/register", form);
      alert("Registered! Please log in.");
      navigate("/login");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <div className="form-container">
        <h2>Register</h2>
        {error && <div className="alert alert-error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div>
            <label>Name</label>
            <input name="name" value={form.name} onChange={handleChange}
              required placeholder="Your full name" />
          </div>
          <div>
            <label>Email</label>
            <input name="email" type="email" value={form.email}
              onChange={handleChange} required placeholder="you@example.com" />
          </div>
          <div>
            <label>Password (min 8 chars)</label>
            <input name="password" type="password" value={form.password}
              onChange={handleChange} required minLength={8} placeholder="••••••••" />
          </div>
          <div>
            <label>Phone</label>
            <input name="phone" value={form.phone} onChange={handleChange}
              required placeholder="9876543210" />
          </div>
          <button disabled={loading}>
            {loading ? <span className="spinner" /> : "Register"}
          </button>
        </form>
        <p className="form-footer">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}