package com.example.springweave;

import com.example.springweave.models.AdminUser;
import com.example.springweave.models.Customer;
import com.example.springweave.models.Vendor;
import com.example.springweave.models.enums.AdminRole; // Import de l'Enum
import com.example.springweave.models.enums.KycStatus;
import com.example.springweave.models.enums.VendorType;
import com.example.springweave.repositories.AdminUserRepository;
import com.example.springweave.repositories.CustomerRepository; // Import du Repository
import com.example.springweave.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class SpringWeaveApplication { // Supprime le <AdminUserRepository> ici

    public static void main(String[] args) {
        SpringApplication.run(SpringWeaveApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // On vérifie si un client existe déjà par son email (ou username selon ton AbstractUserAccount)
            if (customerRepository.count() == 0) {
                Customer testCustomer = new Customer();

                // Champs hérités de AbstractUserAccount
                testCustomer.setName("client_test");
                testCustomer.setEmail("client@springweave.com");
                String encodedPassword = passwordEncoder.encode("Client123!");
                testCustomer.setPasswordHash(encodedPassword);


                // Champs spécifiques à ta classe Customer
                testCustomer.setName("Jean Dupont");
                testCustomer.setPhone("+221770000000"); // Obligatoire selon ton code
                testCustomer.setKycStatus(KycStatus.VERIFIED); // On l'approuve direct pour le test
                testCustomer.setCreditLimit(new BigDecimal("500000"));
                testCustomer.setAvailableCredit(new BigDecimal("500000"));
                testCustomer.setCountry("Sénégal");
                testCustomer.setCity("Dakar");

                customerRepository.save(testCustomer);
                System.out.println("✅ Customer de test créé : client_test / Client123!");
            }
        };
    }
   @Component
    public class UserManagementService {

        private final VendorRepository vendorRepository;
        private final AdminUserRepository adminUserRepository;

        // Injection par constructeur (Recommandé par Spring)
        public UserManagementService(VendorRepository vendorRepository,
                                     AdminUserRepository adminUserRepository) {
            this.vendorRepository = vendorRepository;
            this.adminUserRepository = adminUserRepository;
        }

        public void createEntities() {
            // --- 1. Création d'un Vendeur ---
            Vendor newVendor = new Vendor();
            newVendor.setName("Samsung Store Abidjan");
            newVendor.setEmail("pro@samsung.ci");
            newVendor.setApiKey("sk_live_123456789");
            newVendor.setType(VendorType.LOCAL); // Assure-toi d'avoir cet enum
            newVendor.setCertified(true);
            newVendor.setCountry("CI");

            // C'est ici que la magie opère : SAVE en base de données
            vendorRepository.save(newVendor);
            System.out.println("Vendeur créé avec succès !");

            // --- 2. Création d'un Admin ---
            AdminUser newAdmin = new AdminUser();
            newAdmin.setUsername("superadmin");
            newAdmin.setFullName("Jean Responsable");
            newAdmin.setRole(AdminRole.SUPER_ADMIN); // Assure-toi d'avoir cet enum
            // Attention : n'oublie pas de hasher le mot de passe si AbstractUserAccount a ce champ !
            // newAdmin.setPasswordHash(passwordEncoder.encode("monSuperMotDePasse"));

            adminUserRepository.save(newAdmin);
            System.out.println("Admin créé avec succès !");
        }
    }
}