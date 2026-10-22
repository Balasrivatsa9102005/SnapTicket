import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home";
import Results from "./pages/Results";
import TripDetails from "./pages/TripDetails";
import CreateBooking from "./pages/CreateBooking";
import MyBookings from "./pages/MyBookings";
import AdminDashboard from "./pages/admin/AdminDashboard";

function App() {
  return (
    <>
      <Navbar />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/results" element={<Results />} />
          <Route path="/trip/:id" element={<TripDetails />} />
          <Route
            path="/book/:tripId"
            element={
              <ProtectedRoute>
                <CreateBooking />
              </ProtectedRoute>
            }
          />
          <Route
            path="/my-bookings"
            element={
              <ProtectedRoute>
                <MyBookings />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <AdminRoute>
                <AdminDashboard />
              </AdminRoute>
            }
          />
          <Route
            path="*"
            element={
              <div className="page">
                <div className="state-msg">
                  <span className="big-emoji">🔍</span>
                  <h2>Page not found</h2>
                  <a href="/">Go back home</a>
                </div>
              </div>
            }
          />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

export default App;