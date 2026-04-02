//package com.example.springweave.Config;
//
//import com.example.springweave.models.Customer;
//import com.example.springweave.repositories.*;
//import net.datafaker.Faker;
//import org.apache.catalina.User;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//
//@Component
//public class DatabaseSeeder implements CommandLineRunner {
//
//    // Injection de tes Repositories (à adapter selon tes noms de classes)
//    private final CustomerRepository userRepository;
//    private final VendorRepository vendorRepository;
//    //private final DriverRepository driverRepository;
//    private final CategoryRepository categoryRepository;
//    private final ProductRepository productRepository;
//    private final OrderRepository orderRepository;
//    private final OrderItemRepository orderItemRepository;
//    private final AddressRepository addressRepository;
//    // ... injecte les autres repositories nécessaires
//
//    public DatabaseSeeder(UserRepository userRepository, VendorRepository vendorRepository, CategoryRepository categoryRepository, ProductRepository productRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository, /* autres repos */) {
//        this.userRepository = userRepository;
//        this.vendorRepository = vendorRepository;
//        // ... initialisation
//        this.categoryRepository = categoryRepository;
//        this.productRepository = productRepository;
//        this.orderRepository = orderRepository;
//        this.orderItemRepository = orderItemRepository;
//        this.addressRepository = addressRepository;
//    }
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        if (userRepository.count() >  0) {
//            System.out.println("La base de données contient déjà des données. Seeding ignoré.");
//            return;
//        }
//
//        System.out.println("Démarrage de la génération des fausses données...");
//        Faker faker = new Faker(new Locale("fr"));
//        Random random = new Random();
//
//        // 1. USERS (Min 500)
//        List<User> clients = new ArrayList<>();
//        List<User> vendeurs = new ArrayList<>();
//        List<User> livreurs = new ArrayList<>();
//
//        String[] roles = {"client", "vendeur", "livreur", "admin"};
//        String[] status = {"actif", "bloque", "en_attente"};
//
//        for (int i = 0; i < 600; i++) {
//            User user = new User();
//            user.setEmail(faker.internet().unique().emailAddress());
//            user.setPasswordHash(faker.internet().password(8, 20, true, true, true));
//            user.setNomComplet(faker.name().fullName());
//            user.setTelephone(faker.phoneNumber().cellPhone());
//            user.setAdresse(faker.address().fullAddress());
//
//            // Répartition logique : beaucoup de clients, quelques vendeurs/livreurs
//            String role = (i < 400) ? "client" : (i < 500) ? "vendeur" : "livreur";
//            user.setRole(role);
//            user.setStatut(status[random.nextInt(status.length)]);
//            user.setCreatedAt(Timestamp.from(Instant.now().minus(random.nextInt(365), ChronoUnit.DAYS)));
//
//            user = userRepository.save(user); // On sauvegarde pour avoir l'ID généré
//
//            if (role.equals("client")) clients.add(user);
//            else if (role.equals("vendeur")) vendeurs.add(user);
//            else if (role.equals("livreur")) livreurs.add(user);
//        }
//
//        // 2. VENDORS & DRIVERS
//        for (User v : vendeurs) {
//            Vendor vendor = new Vendor();
//            vendor.setUserId(v.getId());
//            vendor.setNomBoutique(faker.company().name());
//            vendor.setSiret(faker.numerify("##############"));
//            vendor.setDocumentsVerifies(faker.bool().bool());
//            vendor.setSoldePortefeuille(new BigDecimal(faker.commerce().price(0, 5000)));
//            vendorRepository.save(vendor);
//        }
//
//        for (User l : livreurs) {
//            Driver driver = new Driver();
//            driver.setUserId(l.getId());
//            driver.setTypeVehicule(faker.vehicle().driveType()); // ex: "Scooter", "Camionnette"
//            driver.setZoneGeographique(faker.address().city());
//            driver.setEstDisponible(faker.bool().bool());
//            driverRepository.save(driver);
//        }
//
//        // 3. CATEGORIES (50 lignes)
//        List<Category> categories = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            Category cat = new Category();
//            cat.setNom(faker.commerce().department());
//            categories.add(categoryRepository.save(cat));
//        }
//
//        // 4. PRODUCTS (Min 500 lignes)
//        List<Product> products = new ArrayList<>();
//        for (int i = 0; i < 800; i++) {
//            Product p = new Product();
//            p.setVendorId(vendeurs.get(random.nextInt(vendeurs.size())).getId());
//            p.setCategoryId(categories.get(random.nextInt(categories.size())).getId());
//            p.setTitre(faker.commerce().productName());
//            p.setDescription(faker.lorem().paragraph());
//            p.setPrix(new BigDecimal(faker.commerce().price(5, 500)));
//            p.setStock(random.nextInt(200));
//            p.setImageUrl("https://picsum.photos/seed/" + i + "/400/400");
//            products.add(productRepository.save(p));
//        }
//
//        // 5. ADDRESSES (Min 500 lignes)
//        for (User client : clients) {
//            Address address = new Address();
//            address.setUserId(client.getId());
//            address.setTitre(random.nextBoolean() ? "Domicile" : "Travail");
//            address.setAdresseComplete(faker.address().streetAddress());
//            address.setVille(faker.address().city());
//            address.setCodePostal(faker.address().zipCode());
//            address.setPays("France");
//            addressRepository.save(address);
//        }
//
//        // 6. ORDERS & ORDER_ITEMS (Min 500 commandes)
//        String[] orderStatuses = {"panier", "payee", "en_preparation", "expediee", "livree", "annulee", "litige"};
//
//        for (int i = 0; i < 500; i++) {
//            Order order = new Order();
//            order.setClientId(clients.get(random.nextInt(clients.size())).getId());
//            order.setDateCommande(Timestamp.from(Instant.now().minus(random.nextInt(30), ChronoUnit.DAYS)));
//            order.setStatut(orderStatuses[random.nextInt(orderStatuses.length)]);
//            order.setTotalTtc(BigDecimal.ZERO); // On le calculera après
//
//            order = orderRepository.save(order);
//
//            // Générer 1 à 4 articles par commande
//            int nbItems = random.nextInt(4) + 1;
//            BigDecimal totalCommande = BigDecimal.ZERO;
//
//            for (int j = 0; j < nbItems; j++) {
//                Product randomProduct = products.get(random.nextInt(products.size()));
//                int quantite = random.nextInt(3) + 1;
//
//                OrderItem item = new OrderItem();
//                item.setOrderId(order.getId());
//                item.setProductId(randomProduct.getId());
//                item.setQuantite(quantite);
//                item.setPrixUnitaire(randomProduct.getPrix());
//                item.setNoteClient(order.getStatut().equals("livree") ? random.nextInt(5) + 1 : null);
//
//                orderItemRepository.save(item);
//
//                totalCommande = totalCommande.add(randomProduct.getPrix().multiply(new BigDecimal(quantite)));
//            }
//
//            // Mise à jour du total de la commande
//            order.setTotalTtc(totalCommande);
//            orderRepository.save(order);
//        }
//
//        // À partir d'ici, tu peux dupliquer la même logique pour Payments, Deliveries, Reviews, etc.
//        // Exemple pour Deliveries : on boucle sur les commandes "expediee" ou "livree"
//        // et on leur assigne un chauffeur au hasard dans la liste `livreurs`.
//
//        System.out.println("Génération terminée avec succès ! Toutes les tables sont remplies.");
//    }
//}