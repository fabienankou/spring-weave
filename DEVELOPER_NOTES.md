# 📝 NOTES DÉVELOPPEURS - SHOPIVERS

## Convention de Code

### Nommage

- **Classes**: PascalCase (AuthService.java, ProductController.java)
- **Méthodes**: camelCase (getProductById, createOrder)
- **Constantes**: UPPER_SNAKE_CASE (JWT_EXPIRATION, API_KEY)
- **Variables**: camelCase (customer, orderItems)

### Packages

```
com.example.springweave.
├── controllers     # Endpoints API
├── services        # Logique métier
├── models          # Entités JPA
├── repositories    # Accès données
├── dtos            # Data Transfer Objects
├── security        # JWT, Authentification
├── Config          # Configuration Spring
├── exceptions      # Gestion erreurs
└── models.enums    # Énumérations
```

### Imports

Garder les imports organisés:
1. Java imports
2. javax imports
3. org.springframework imports
4. Project imports

---

## Annotations Communes

### Spring

```java
@RestController      // Classe contrôleur REST
@Service             // Service métier
@Repository          // Repository d'accès données
@Configuration       // Configuration Spring
@Bean                // Définition bean Spring

@RequestMapping      // Mapping URL
@GetMapping          // GET HTTP
@PostMapping         // POST HTTP
@PutMapping          // PUT HTTP
@DeleteMapping       // DELETE HTTP
@PatchMapping        // PATCH HTTP

@Autowired           // Injection dépendance
@RequiredArgsConstructor // Lombok - injecter dépendances finales
```

### Lombok

```java
@Getter              // Générer getters
@Setter              // Générer setters
@NoArgsConstructor   // Constructeur sans args
@AllArgsConstructor  // Constructeur tous args
@Data                // Getter + Setter + equals + hashCode + toString
@RequiredArgsConstructor // Constructeur args finales
@Slf4j               // Logger
```

### JPA

```java
@Entity              // Classe entité
@Table               // Mapping table BD
@Column              // Mapping colonne
@Id                  // Clé primaire
@GeneratedValue      // Génération auto ID
@ManyToOne           // Relation plusieurs à un
@OneToMany           // Relation un à plusieurs
@JoinColumn          // Colonne jointure
```

### Validation

```java
@NotNull             // Ne pas être null
@NotBlank            // Ne pas être vide
@Email               // Valide email
@Size(min=, max=)    // Taille
@Min/@Max            // Valeur min/max
```

---

## Patterns Utilisés

### Service Pattern

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    
    public Product createProduct(Product product) {
        // Validation
        validate(product);
        // Logique métier
        return repository.save(product);
    }
}
```

### Controller Pattern

```java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok(convertToResponse(product));
    }
}
```

### DTO Pattern

```java
public record ProductResponse(
    UUID id,
    String name,
    BigDecimal price
) {}
```

---

## Gestion des Erreurs

### Exception Personnalisée

```java
try {
    Product product = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
} catch (RuntimeException e) {
    logger.error("Erreur: {}", e.getMessage());
    throw e;
}
```

### Global Exception Handler

Utiliser `GlobalExceptionHandler` pour les réponses standardisées:

```java
{
    "timestamp": "2026-01-27T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Produit non trouvé"
}
```

---

## Transactions

### Utiliser @Transactional

```java
@Transactional
public Order createOrder(CreateOrderRequest request) {
    // Si une erreur survient, tout est annulé
    Order order = orderRepository.save(...);
    items.forEach(item -> orderItemRepository.save(item));
    return order;
}
```

### Ne pas oublier les flush

```java
@Transactional(flush = FlushModeType.COMMIT)
public void complexOperation() {
    // Opérations complexes
}
```

---

## Tests API avec Curl

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"pass123"}'
```

### Créer Produit (avec token)
```bash
TOKEN="votre_token_jwt"
curl -X POST http://localhost:8080/api/vendor/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Test Product",
    "price":5000,
    "category":"Electronics"
  }'
```

### Créer Commande
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId":"uuid",
    "items":[{"productId":"uuid","quantity":1}],
    "shippingAddress":"Test",
    "paymentMethod":"card"
  }'
```

---

## Bonnes Pratiques

### 1. Toujours valider les entrées

```java
if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
    throw new IllegalArgumentException("Prix invalide");
}
```

### 2. Utiliser les Optionals

```java
Customer customer = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Non trouvé"));
```

### 3. Loguer appropriément

```java
@Slf4j
public class ProductService {
    public void createProduct(Product product) {
        log.info("Création produit: {}", product.getName());
        log.debug("Détails: {}", product);
    }
}
```

### 4. Respecter SOLID

- Single Responsibility
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

### 5. Pagination pour les listes

```java
Page<Product> products = repository.findAll(PageRequest.of(0, 10));
```

### 6. Utiliser les enums

```java
public enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED
}
```

---

## Debugging

### En mode DEV

Dans `application.properties`:
```properties
logging.level.com.example.springweave=DEBUG
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Utiliser les Breakpoints

```bash
# Démarrer en mode debug
./mvnw spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### Inspecter la BD

```bash
# Via PgAdmin
http://localhost:8081

# Via psql
psql -U postgres -d spring_weave
```

---

## Performance

### Index sur les colonnes recherchées

```java
@Entity
public class Product {
    @Column(unique = true)
    private String sku;  // Index unique
}
```

### Lazy Loading pour les relations

```java
@ManyToOne(fetch = FetchType.LAZY)
private Vendor vendor;
```

### N+1 Query Problem

```java
// ❌ Mauvais - crée N+1 queries
List<Order> orders = orderRepository.findAll();
orders.forEach(o -> System.out.println(o.getCustomer().getName()));

// ✅ Bon - utiliser JOIN FETCH
Page<Order> orders = orderRepository.findAllWithCustomer(pageable);
```

---

## Sécurité

### Valider les entrées

```java
if (email == null || !email.contains("@")) {
    throw new IllegalArgumentException("Email invalide");
}
```

### Ne jamais exposer les erreurs DB

```java
// ❌ Mauvais
catch (Exception e) {
    return ResponseEntity.badRequest().body(e.getMessage());
}

// ✅ Bon
catch (Exception e) {
    logger.error("Erreur DB", e);
    return ResponseEntity.badRequest().body("Erreur serveur");
}
```

### Hacher les mots de passe

```java
String hashedPassword = passwordEncoder.encode(rawPassword);
```

### Valider les permissions

```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteOrder(UUID id) { }
```

---

## Déploiement

### Avant le déploiement

- ✅ Tests unitaires passants
- ✅ Code review effectuée
- ✅ Documentation à jour
- ✅ Pas de secrets en dur
- ✅ Logs appropriés

### Variables d'environnement

```bash
# Production
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/spring_weave
export APPLICATION_SECURITY_JWT_SECRET_KEY=prod_secret_key
```

---

## Ressources Utiles

- Spring Boot Docs: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- JWT: https://jwt.io
- PostgreSQL: https://www.postgresql.org/docs/
- Docker: https://docs.docker.com/

---

## Questions Fréquentes

**Q: Comment ajouter une nouvelle API?**
A: 1) Créer Controller, 2) Créer Service, 3) Ajouter Repository si besoin, 4) Ajouter DTO

**Q: Comment modifier une entité?**
A: 1) Modifier la classe, 2) Spring va générer migration via Hibernate, 3) Tester

**Q: Comment sécuriser un endpoint?**
A: Utiliser `@PreAuthorize("hasRole('ROLE_NAME')")` sur la méthode

**Q: Comment debugger?**
A: Utiliser les logs (DEBUG level) ou les breakpoints IDE

---

**Dernière mise à jour:** 27 Janvier 2026
**Version:** 1.0.0
