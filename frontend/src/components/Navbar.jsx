import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const initials = user?.email ? user.email.substring(0, 2).toUpperCase() : "?";

  return (
    <nav className="navbar">
      <Link to="/" className="brand">
        <span className="brand-emoji">🎫</span> SnapTicket
      </Link>
      <NavLink to="/" end>Home</NavLink>
      {user && <NavLink to="/my-bookings">My Bookings</NavLink>}
      {isAdmin() && <NavLink to="/admin">Admin</NavLink>}

      <div className="user-area">
        {user ? (
          <>
            <span className="user-avatar">{initials}</span>
            <span className="user-name">{user.email}</span>
            <button className="btn-sm btn-plain" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="btn-sm btn-plain">Login</Link>
            <Link to="/register" className="btn-sm">Sign up</Link>
          </>
        )}
      </div>
    </nav>
  );
}