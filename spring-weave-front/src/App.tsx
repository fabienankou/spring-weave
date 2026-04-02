import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
//import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
//import Cart from './pages/Cart';
//import Login from './pages/Login';
//import VendorDashboard from './pages/VendorDashboard';
//import DriverDashboard from './pages/DriverDashboard';
//import NotFound from './pages/NotFound';

// Un composant Navbar simple pour tester la navigation
const Navbar = () => (
  <nav style={{ padding: '1rem', borderBottom: '1px solid #ccc', display: 'flex', gap: '15px' }}>
    <Link to="/">Accueil</Link>
    <Link to="/products">Boutique</Link>
    <Link to="/cart">Panier</Link>
    <Link to="/login">Connexion</Link>
    <Link to="/vendor/dashboard" style={{ color: 'orange' }}>Espace Vendeur</Link>
  </nav>
);

function App() {
  return (
    <Router>
      <Navbar />

      <div style={{ padding: '20px' }}>
        <Routes>
          {/* --- ROUTES PUBLIQUES --- */}
          <Route path="/" element={<Home />} />
          <Route path="/products" element={<ProductList />} />
          <Route path="/product/:id" element={<ProductDetail />} />
          <Route path="/login" element={<Login />} />
          <Route path="/cart" element={<Cart />} />

          {/* --- ROUTES VENDEURS (Back-office) --- */}
          <Route path="/vendor/dashboard" element={<VendorDashboard />} />
          {/* Tu pourras ajouter /vendor/products, /vendor/credits ici */}

          {/* --- ROUTES LIVREURS --- */}
          <Route path="/driver/dashboard" element={<DriverDashboard />} />

          {/* --- PAGE 404 --- */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;