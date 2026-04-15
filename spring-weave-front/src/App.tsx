import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
// import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
// import Cart from './pages/Cart';
import Login from './pages/Login';
// import VendorDashboard from './pages/VendorDashboard';
// import DriverDashboard from './pages/DriverDashboard';
// import NotFound from './pages/NotFound';

const Navbar = () => (
  <nav style={{ padding: '1rem', borderBottom: '1px solid #ccc', display: 'flex', gap: '15px' }}>
    <Link to="/">Accueil</Link>
    <Link to="/products">Boutique</Link>
    <Link to="/cart">Panier</Link>
    <Link to="/login">Connexion</Link>
    <Link to="VendorDashboard" style={{ color: 'orange' }}>Espace Vendeur</Link>
  </nav>
);

function App() {
  return (
    <Router>
      <Navbar />

      <div style={{ padding: '20px' }}>
        <Routes>
          {/* CORRECTION 1 : Le chemin de l'accueil doit être "/" */}
          <Route path="/" element={<Home />} />

          <Route path="/product/:id" element={<ProductDetail />} />
          <Route path="/login" element={<Login />} />

          {/* CORRECTION 2 : Ces routes sont temporairement commentées car leurs composants ne sont pas importés */}
          {/* <Route path="/products" element={<ProductList />} /> */}
          {/* <Route path="/cart" element={<Cart />} /> */}
          {/* <Route path="/vendor/dashboard" element={<VendorDashboard />} /> */}
          {/* <Route path="/driver/dashboard" element={<DriverDashboard />} /> */}
          {/* <Route path="*" element={<NotFound />} /> */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;