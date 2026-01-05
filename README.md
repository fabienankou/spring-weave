# WEAVE  Plateforme E-Commerce

**SHOPIVERS** est une solution complète de vente en ligne. Le projet s'appuie sur un backend **Spring Boot**, un frontend moderne et une infrastructure robuste pilotée par **Docker** et **Kubernetes**.

---

## 📂 Structure du Backend (Spring Boot)

Le code source se trouve dans `src/main/java/com/example/springweave/`. Nous avons adopté une architecture en couches (Layered Architecture) pour isoler les responsabilités.

### Organisation des Packages et Fichiers

* **`config/`** : Configuration technique.
* `SecurityConfig.java`, `CorsConfig.java`, `SwaggerConfig.java`.


* **`controllers/`** : Points d'entrée API REST.
* `AuthController.java`, `ProductController.java`, `OrderController.java`.


* **`models/`** : Entités JPA (Miroir de la base de données).
* `User.java`, `Product.java`, `Order.java`, `Role.java`.


* **`repositories/`** : Accès aux données via Spring Data JPA.
* `UserRepository.java`, `ProductRepository.java`.


* **`services/`** : Logique métier et traitements.
* `AuthService.java`, `ProductService.java`.


* **`dtos/`** : Objets d'échange (Data Transfer Objects).
* `LoginRequest.java`, `ProductResponse.java`.


* **`security/`** : Logique liée à la sécurité.
* `JwtService.java`, `JwtAuthenticationFilter.java`.


* **`exceptions/`** : Traitement des erreurs.
* `GlobalExceptionHandler.java`, `ResourceNotFoundException.java`.



---

## 👥 Répartition des Rôles (ISR)

Chaque membre de l'équipe ISR porte une responsabilité spécifique pour garantir la stabilité du système.

| Rôle | Responsable | Missions principales |
| --- | --- | --- |
| **ISR 1 — Docker** | Fabien | Création des Dockerfiles (Back/Front/DB), rédaction du `docker-compose.yml`, gestion des volumes et réseaux. |
| **ISR 2 — CI/CD** |Fabien | Mise en place du GitFlow, création du pipeline GitHub Actions (Build, Test, Push Docker). |
| **ISR 3 — K8s** |Arsene | Création des manifests (Deployments, Services, Ingress), gestion de la scalabilité et haute disponibilité. |
| **ISR 4 — Sécurité** | enock | Monitoring (Prometheus/Grafana), scan des images Docker, gestion des certificats SSL/HTTPS. |

---

## 🛠 Workflow de Développement

### Stratégie Git (GitFlow)

1. **`main`** : Code stable et déployé en production.
2. **`develop`** : Branche principale de travail pour l'intégration.
3. **`feature/`** : Branches éphémères pour chaque nouvelle fonctionnalité (ex: `feature/backend/jwt`).

### Pipeline CI/CD

À chaque "Push" sur la branche `develop` :

1. **Build** : Compilation du code (Maven pour le back, NPM pour le front).
2. **Tests** : Lancement des tests unitaires automatisés.
3. **Images** : Construction et publication des images Docker.
4. **Déploiement** : Mise à jour automatique de l'environnement de test.

---

## 🚀 Guide de démarrage

### 1. Prérequis

* Java 17 installé.
* Docker & Docker Compose opérationnels.
* Postman pour les tests API.

### 2. Lancement Local

```bash
# Lancer la base de données PostgreSQL
docker-compose up -d db

# Lancer l'application Spring Boot (via IntelliJ ou ligne de commande)
mvn spring-boot:run

```

### 3. Documentation API

Une fois le serveur lancé, la documentation interactive Swagger est accessible ici :
`http://localhost:8080/swagger-ui.html`

---

## 🔒 Sécurité

* **Mots de passe** : Hachés via **BCrypt**.
* **Session** : Sans état (Stateless) utilisant des tokens **JWT** (JSON Web Tokens).
* **IDs** : Protection contre l'énumération par des formats sécurisés.

---
