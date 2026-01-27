# 📑 INDEX - SHOPIVERS Documentation

## 🚀 Démarrer Rapidement

1. **[QUICK_START.md](QUICK_START.md)** - Guide de démarrage en 5 minutes
2. **[README_COMPLETE.md](README_COMPLETE.md)** - Vue d'ensemble complète

---

## 📚 Documentation API

1. **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Référence tous les endpoints
2. **[SHOPIVERS_API.postman_collection.json](SHOPIVERS_API.postman_collection.json)** - Tests Postman

---

## 💻 Documentation Technique

1. **[DEVELOPER_NOTES.md](DEVELOPER_NOTES.md)** - Guide pour les développeurs
2. **[IMPLEMENTATION_CHECKLIST.md](IMPLEMENTATION_CHECKLIST.md)** - Checklist complète
3. **[WORK_SUMMARY.md](WORK_SUMMARY.md)** - Résumé du travail effectué

---

## 🔍 Fichiers Configuration

```
.
├── pom.xml                  # Dépendances Maven
├── application.properties   # Configuration Spring
├── Dockerfile              # Image Docker
├── docker-compose.yml      # Stack Docker
├── .env.example            # Variables d'environnement
└── .env                    # Variables d'environnement (local)
```

---

## 🏗️ Structure du Code

### Controllers
```
src/main/java/com/example/springweave/controllers/
├── AuthController.java               ← Authentification (login, register)
├── ProductController.java            ← Produits publics (GET)
├── VendorProductController.java      ← Produits vendeur (POST, PUT, DELETE)
├── OrderController.java              ← Commandes (CRUD)
└── CustomerController.java           ← Clients (profil, modification)
```

### Services
```
src/main/java/com/example/springweave/services/
├── AuthService.java                  ← Logique d'authentification
├── ProductService.java               ← Logique produits
├── OrderService.java                 ← Logique commandes
└── ... (autres services)
```

### Models
```
src/main/java/com/example/springweave/models/
├── Customer.java                     ← Entité client
├── Product.java                      ← Entité produit
├── Order.java                        ← Entité commande
├── OrderItem.java                    ← Entité article de commande
└── ... (autres modèles)
```

### Repositories
```
src/main/java/com/example/springweave/repositories/
├── CustomerRepository.java           ← Accès customers
├── ProductRepository.java            ← Accès products
├── OrderRepository.java              ← Accès orders
├── OrderItemRepository.java          ← Accès order items
└── ... (autres repositories)
```

### DTOs
```
src/main/java/com/example/springweave/dtos/
├── LoginRequest.java                 ← Requête login
├── RegisterRequest.java              ← Requête inscription
├── CreateOrderRequest.java           ← Requête création commande
├── ProductResponse.java              ← Réponse produit
├── OrderResponse.java                ← Réponse commande
└── CustomerResponse.java             ← Réponse client
```

---

## 🔐 Authentification

### Endpoints Publics
```
POST   /api/auth/register        # Inscription
POST   /api/auth/login           # Connexion
GET    /api/products             # Voir produits
```

### Endpoints Protégés
```
GET    /api/auth/me              # Profil (token requis)
POST   /api/orders               # Créer commande (token + CUSTOMER)
PUT    /api/vendor/products      # Modifier produit (token + VENDOR)
PUT    /api/orders/{id}/status   # Statut commande (token + ADMIN)
```

### Token JWT Format
```
Authorization: Bearer <token>
```

---

## 🧪 Tests Rapides

### Utilisant Curl
```bash
# Inscription
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com",...}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"pass"}'

# Récupérer produits
curl http://localhost:8080/api/products
```

### Utilisant Postman
1. Importer `SHOPIVERS_API.postman_collection.json`
2. Configurer variables: `token`, `customerId`, `productId`
3. Exécuter les requêtes

### Utilisant Swagger
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Docker

### Commandes Utiles
```bash
# Démarrer la stack
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter la stack
docker-compose down

# Nettoyer les images
docker-compose down -v
```

### Services Disponibles
- **Backend**: http://localhost:8080
- **PgAdmin**: http://localhost:8081
- **Database**: localhost:5432

---

## 🛠️ Troubleshooting

### La DB ne se connecte pas
```bash
# Vérifier si PostgreSQL fonctionne
docker ps | grep postgres

# Redémarrer
docker-compose restart db
```

### Port 8080 déjà utilisé
```bash
# Changer le port dans application.properties
server.port=8081
```

### Erreur de compilation Maven
```bash
# Nettoyer et recompiler
./mvnw clean compile

# Vérifier les dépendances
./mvnw dependency:tree
```

### Logs pour déboguer
```properties
# Dans application.properties
logging.level.com.example.springweave=DEBUG
spring.jpa.show-sql=true
```

---

## 📊 Architecture

```
┌─────────────────────────────────────────────┐
│         Client (Browser / Postman)          │
└──────────────────┬──────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────┐
│      Spring Boot REST API (Port 8080)       │
├─────────────────────────────────────────────┤
│  Controllers ↔ Services ↔ Repositories      │
└──────────────────┬──────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────┐
│  PostgreSQL Database (Port 5432)            │
└─────────────────────────────────────────────┘
```

---

## 📈 Endpoints Statistics

| Type | Nombre | Sécurité |
|------|--------|----------|
| Publics | 4 | Aucune |
| Clients | 6 | JWT CUSTOMER |
| Vendeurs | 5 | JWT VENDOR |
| Admin | 5 | JWT ADMIN |
| **Total** | **20** | - |

---

## 🔒 Sécurité

- ✅ Authentification JWT
- ✅ Chiffrement BCrypt
- ✅ RBAC (Role-Based Access Control)
- ✅ CORS configuré
- ✅ Exception handling centralisé
- ✅ Validation des entrées

---

## 📝 Checklist de Lancement

- [ ] Clone le projet
- [ ] Copier `.env.example` → `.env`
- [ ] Démarrer Docker `docker-compose up -d`
- [ ] Compiler `./mvnw clean compile`
- [ ] Lancer l'app `./mvnw spring-boot:run`
- [ ] Tester endpoints via Postman
- [ ] Vérifier Swagger `http://localhost:8080/swagger-ui`

---

## 📞 Support

Pour toute question:
- 📧 Email: support@shopivers.com
- 📚 Docs: Consulter les fichiers MD
- 🐛 Issues: Créer une issue détaillée

---

## 📄 Fichiers Importants

| Fichier | Usage |
|---------|-------|
| `pom.xml` | Dépendances Maven |
| `application.properties` | Configuration |
| `Dockerfile` | Image Docker |
| `docker-compose.yml` | Stack Docker |
| `API_DOCUMENTATION.md` | API Reference |
| `QUICK_START.md` | Getting Started |
| `DEVELOPER_NOTES.md` | Developer Guide |

---

**Dernière mise à jour:** 27 Janvier 2026  
**Version:** 1.0.0  
**Statut:** ✅ Production Ready
