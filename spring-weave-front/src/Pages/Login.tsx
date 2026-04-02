const Login = () => {
  const handleLogin = (e) => {
    e.preventDefault();
    alert("Connexion simulée. Dans un vrai projet, on appelle l'API /auth/login");
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto' }}>
      <h2>Connexion</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input type="email" placeholder="Email" required />
        <input type="password" placeholder="Mot de passe" required />
        <button type="submit">Se connecter</button>
      </form>
    </div>
  );
};

export default Login;