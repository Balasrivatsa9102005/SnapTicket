export default function Footer() {
  return (
    <footer className="footer">
      <div>
        <span className="footer-brand">SnapTicket</span> · Book smarter. Travel lighter.
      </div>
      <div className="text-muted" style={{ marginTop: "0.35rem" }}>
        © {new Date().getFullYear()} SnapTicket. All rights reserved.
      </div>
    </footer>
  );
}