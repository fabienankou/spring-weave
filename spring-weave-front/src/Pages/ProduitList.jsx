import { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const ProductList = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    // Ajuste l'URL selon ton controller @RequestMapping("/api/products")
    axios.get('http://localhost:8080/api/products')
      .then(res => setProducts(res.data))
      .catch(err => console.error("Erreur API:", err));
  }, []);

  return (
    <div>
      <h2>Catalogue ({products.length} articles)</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '20px' }}>
        {products.map(p => (
          <div key={p.id} style={{ border: '1px solid #ddd', borderRadius: '8px', padding: '10px' }}>
            <img src={p.imageUrl || 'https://via.placeholder.com/150'} alt={p.titre} style={{ width: '100%' }} />
            <h4>{p.titre}</h4>
            <p style={{ fontWeight: 'bold' }}>{p.prix} €</p>
            <Link to={`/product/${p.id}`}>Voir détails</Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;