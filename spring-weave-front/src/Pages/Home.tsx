import { Link } from 'react-router-dom';

const Home = () => (
  <div style={{ textAlign: 'center', marginTop: '50px' }}>
    <h1>Bienvenue sur Spring Weave Compagnie </h1>
    <p>La plateforme e-commerce nouvelle génération avec micro-crédits intégrés.</p>
    <Link to="/products">
      <button style={{ padding: '30px 60px',marginTop: '40px cursor: 'pointer' }}>Découvrir nos produits</button>
    </Link>
  </div>
);

export default Home;