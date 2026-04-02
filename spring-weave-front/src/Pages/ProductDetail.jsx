import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);

  useEffect(() => {
    axios.get(`http://localhost:8080/api/products/${id}`)
      .then(res => setProduct(res.data))
      .catch(err => console.error(err));
  }, [id]);

  if (!product) return <p>Chargement...</p>;

  return (
    <div style={{ display: 'flex', gap: '40px' }}>
      <img src={product.imageUrl} alt={product.titre} style={{ width: '400px' }} />
      <div>
        <h1>{product.titre}</h1>
        <p style={{ fontSize: '1.5rem', color: 'green' }}>{product.prix} €</p>
        <p>{product.description}</p>
        <p>Stock disponible : {product.stock}</p>
        <button onClick={() => alert('Ajouté au panier !')}>Ajouter au panier</button>
      </div>
    </div>
  );
};

export default ProductDetail;