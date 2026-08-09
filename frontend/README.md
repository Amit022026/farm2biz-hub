# Farm2Biz Hub — Frontend (v2, redesigned)

React 18 + Vite SPA, redesigned with a Material Design 3-inspired look: soft gradients, glassmorphism navbar, floating rounded cards, skeleton loading, empty-state illustrations, a lightweight custom bar chart, and full dark mode support.

## Setup
```bash
cd frontend
npm install
npm run dev
```
Runs at http://localhost:5173. **The backend must have CORS configured for this origin** — see `backend/.../security/SecurityConfig.java`'s `corsConfigurationSource()` bean and `app.cors.allowed-origin` in `application.properties`.

## What's new in this redesign
- **Product images**: `Product.imageUrl` (backend field) + `ProductImage.jsx` (frontend component) — shows the real photo if a URL is set, otherwise a deterministic colored gradient placeholder with a leaf icon (never looks "broken").
- **Dark mode**: `ThemeContext` + toggle button in the navbar. Persisted to `localStorage`, and applied *before* React even mounts (via a small inline script in `index.html`) to avoid a flash of the wrong theme.
- **Design tokens**: every color lives in CSS custom properties in `index.css` (`:root` for light, `[data-theme='dark']` for dark) — change the palette in one place.
- **Skeleton loading**: `Skeleton.jsx` — shimmering placeholder cards/rows shown while data is in flight, instead of a blank screen or spinner.
- **Empty states**: `EmptyState.jsx` — a small inline SVG illustration + friendly copy, used everywhere a list could be empty.
- **Charts**: `BarChart.jsx` — a small dependency-free SVG bar chart for the Admin dashboard's "Orders by Status" — no charting library needed for this scale.
- **Icons**: `Icons.jsx` — a minimal, consistent inline SVG icon set (no icon library dependency).

## Design decisions carried over from before
- Client-side filtering (category/search on Landing, farmer-owned products on the Dashboard) — the backend has no query-param filtering.
- JWT kept in memory + mirrored to `sessionStorage` for refresh survival.
- Register only offers Buyer/Farmer roles (Admin can't self-register).
