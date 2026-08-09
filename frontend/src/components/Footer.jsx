import { Link } from 'react-router-dom';
import Logo from './Logo';
import {
  LeafIcon, MailIcon, PhoneIcon,
  InstagramIcon, TwitterIcon, FacebookIcon,
} from './Icons';

export default function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="site-footer">
      <div className="footer-blob" />

      <div className="container footer-inner">
        <div className="footer-grid">
          {/* Brand column */}
          <div className="footer-col footer-brand-col">
            <div className="footer-brand">
              <Logo size={34} />
              <span>Farm2Biz Hub</span>
            </div>
            <p className="footer-tagline">
              Connecting local farmers directly with buyers — fresher produce,
              fairer prices, zero middlemen.
            </p>
            <div className="footer-social">
              <a href="#" aria-label="Instagram"><InstagramIcon size={16} /></a>
              <a href="#" aria-label="Twitter"><TwitterIcon size={16} /></a>
              <a href="#" aria-label="Facebook"><FacebookIcon size={16} /></a>
            </div>
          </div>

          {/* Marketplace column */}
          <div className="footer-col">
            <h4>Marketplace</h4>
            <Link to="/">Browse Products</Link>
            <Link to="/register">Create an Account</Link>
            <Link to="/login">Sign In</Link>
          </div>

          {/* For Farmers column */}
          <div className="footer-col">
            <h4>For Farmers</h4>
            <Link to="/register">Sell on Farm2Biz</Link>
            <a href="#">How Payouts Work</a>
            <a href="#">Farmer Guidelines</a>
          </div>

          {/* Company column */}
          <div className="footer-col">
            <h4>Company</h4>
            <a href="#">About Us</a>
            <a href="#">Terms of Service</a>
            <a href="#">Privacy Policy</a>
          </div>

          {/* Contact column */}
          <div className="footer-col">
            <h4>Get in Touch</h4>
            <a href="mailto:farm2biz@gmail.com"><MailIcon size={14} />farm2biz@gmail.com</a>
            <a href="tel:+911234567890"><PhoneIcon size={14} /> +91 12345 67890</a>
          </div>
        </div>

        <div className="footer-bottom">
          <span>© {year} Farm2Biz Hub. All rights reserved.</span>
          <span className="footer-made-with">Built for farmers, by design 🌾</span>
        </div>
      </div>
    </footer>
  );
}
