# API Documentation - SHOPIVERS E-Commerce

## Endpoints Publics

### Authentification

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Register
```
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+22890123456",
  "country": "TG",
  "city": "Lomé",
  "address": "123 Rue Test"
}
```

### Produits

#### Récupérer tous les produits
```
GET /api/products
```

#### Récupérer un produit
```
GET /api/products/{id}
```

#### Créer un produit (Vendeur)
```
POST /api/vendor/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Produit Test",
  "description": "Description",
  "category": "Électronique",
  "price": 50000,
  "currency": "XOF",
  "isAvailable": true,
  "vendor_id": "{vendor_uuid}"
}
```

#### Mettre à jour un produit
```
PUT /api/vendor/products/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

#### Supprimer un produit
```
DELETE /api/vendor/products/{id}
Authorization: Bearer {token}
```

### Commandes

#### Créer une commande
```
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": "{customer_uuid}",
  "items": [
    {
      "productId": "{product_uuid}",
      "quantity": 2
    }
  ],
  "shippingAddress": "123 Rue Test",
  "paymentMethod": "card"
}
```

#### Récupérer toutes les commandes
```
GET /api/orders
Authorization: Bearer {token}
```

#### Récupérer une commande
```
GET /api/orders/{id}
Authorization: Bearer {token}
```

#### Récupérer les commandes du client
```
GET /api/orders/customer/{customerId}
Authorization: Bearer {token}
```

## Statuts

### Order Status
- PENDING
- CONFIRMED
- PROCESSING
- SHIPPED
- IN_TRANSIT
- CUSTOMS
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED
- REFUNDED

### Payment Status
- PENDING
- PARTIAL
- PAID
- FAILED
- REFUNDED

## Rôles et Permissions

- **CUSTOMER**: Accès à /api/orders, /api/products
- **VENDOR**: Accès à /api/vendor/products
- **ADMIN**: Accès à tous les endpoints

## Documentation Swagger

Accédez à: `http://localhost:8080/swagger-ui/index.html`

## Erreurs Courantes

### 401 Unauthorized
Le token JWT est manquant ou invalide

### 403 Forbidden
L'utilisateur n'a pas les permissions nécessaires

### 404 Not Found
La ressource demandée n'existe pas

### 500 Internal Server Error
Erreur serveur interne
