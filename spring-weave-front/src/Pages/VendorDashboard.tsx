const VendorDashboard = () => {
  return (
    <div>
      <h1 style={{ color: 'orange' }}>Tableau de bord Vendeur</h1>
      <div style={{ display: 'flex', gap: '20px', marginBottom: '30px' }}>
        <div style={{ background: '#f4f4f4', padding: '20px', flex: 1 }}>
          <h3>Solde Portefeuille</h3>
          <p style={{ fontSize: '24px' }}>1 250,50 €</p>
        </div>
        <div style={{ background: '#f4f4f4', padding: '20px', flex: 1 }}>
          <h3>Crédits en cours</h3>
          <p>1 demande approuvée</p>
        </div>
      </div>

      <h3>Mes Actions</h3>
      <ul>
        <li>Ajouter un nouveau produit</li>
        <li>Demander un micro-crédit</li>
        <li>Gérer mes litiges</li>
      </ul>
    </div>
  );
};


export default VendorDashboard;