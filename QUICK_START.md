# SHOPIVERS - Guide de Démarrage Rapide

## Prérequis

- Java 17+
- Maven 3.9+
- PostgreSQL 15+
- Docker & Docker Compose (optionnel)
- Git

## Démarrage Local

### 1. Configuration Base de Données

#### Option A: PostgreSQL Local

```bash
# Linux/Mac
brew install postgresql
brew services start postgresql

# Windows
# Installer PostgreSQL depuis: https://www.postgresql.org/download/windows/
```

#### Option B: Docker

```bash
docker-compose up -d db pgadmin
```

Accédez à PgAdmin: `http://localhost:8081`

### 2. Configuration de l'Application

```bash
# Cloner le projet
git clone https://github.com/fabienankou/spring-weave.git
cd spring-weave

# Créer un fichier .env (optionnel)
cp .env.example .env
```

### 3. Démarrer l'Application

```bash
# Avec Maven
mvn spring-boot:run

# Ou avec Maven Wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Ou avec Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run
```

L'application sera disponible sur: `http://localhost:8080`

### 4. Vérifier le Status

```bash
curl http://localhost:8080/actuator/health
```

## Documentation API

### Swagger/OpenAPI

Accédez à: `http://localhost:8080/swagger-ui/index.html`

### Collection Postman

Importez le fichier `SHOPIVERS_API.postman_collection.json` dans Postman

## Tests Rapides

### 1. Créer un Compte

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "phone": "+22890123456",
    "country": "TG",
    "city": "Lomé",
    "address": "123 Rue Test"
  }'
```

### 2. Se Connecter

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Récupérer un Produit

```bash
curl http://localhost:8080/api/products
```

## Déploiement Docker

```bash
# Build l'image
docker build -t spring-weave:latest .

# Démarrer l'application complète
docker-compose up -d

# Voir les logs
docker-compose logs -f backend

# Arrêter tout
docker-compose down
```

## Dépannage

### Erreur: "Database connection refused"

Vérifiez que PostgreSQL fonctionne:

```bash
# Linux/Mac
brew services list

# Windows (via Services)
# Cherchez PostgreSQL dans Services

# Docker
docker ps | grep postgres
```

### Erreur: "Port 8080 already in use"

Changez le port dans `application.properties`:

```properties
server.port=8081
```

### Erreur de Compilation

```bash
# Nettoyer et recompiler
mvn clean compile

# Vérifier les dépendances
mvn dependency:tree
```

## Structure du Projet

```
spring-weave/
├── src/main/java/com/example/springweave/
│   ├── controllers/      # Endpoints API
│   ├── services/         # Logique métier
│   ├── models/           # Entités JPA
│   ├── repositories/     # Accès BD
│   ├── dtos/             # Objets d'échange
│   ├── security/         # JWT, API Keys
│   ├── exceptions/       # Gestion erreurs
│   └── Config/           # Configuration
├── src/main/resources/
│   └── application.properties
├── pom.xml              # Dépendances Maven
└── Dockerfile
```

## Documentation Complète

Voir: `README.md` et `API_DOCUMENTATION.md`

## Support

Pour toute question, contactez: `support@shopivers.com`
