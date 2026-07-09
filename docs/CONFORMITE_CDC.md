# Matrice de conformité au cahier des charges — SHOPIVERS / Weave

> Établie le 9 juillet 2026 à partir d'une revue du code (`src/main/java/...`) et du cahier des charges consolidé (v2.0).
> Légende statut : ✅ Fait · 🟡 Partiel · ⛔ Manquant · ➖ Hors code (organisationnel/marketing).

## 1. Fonctionnalités (CDC §4)

| Exigence CDC | Où (code) | Statut |
|---|---|---|
| §4.1 Catalogue unifié + filtres (origine, prix, dispo) | `ProductController`, `ProductService` | 🟡 catalogue OK, filtres à compléter |
| §4.1 Simulateur de crédit temps réel | `POST /api/credit/simulate`, `CreditService.simulate` | ✅ |
| §4.1 Parcours crédit (Premium + KYC + scoring) | `CreditService.applyForCredit`, `KycService` | ✅ |
| §4.1 Multi-paiement (Mobile Money, cartes) | `PaymentService`, `MockPaymentProvider`, Stripe | 🟡 cartes/mock OK, Mobile Money réel à brancher |
| §4.1 Tableau de bord client | Front React + `/api/credit/customer/{id}`, notifications | ✅ |
| §4.2 Dashboard risque & scoring (admin) | `AdminController`, `/api/credit` | 🟡 API OK, tableau de bord à consolider |
| §4.2 Onboarding fournisseurs + Score Vendeur | `Vendor`, `VendorController` | 🟡 modèle OK, workflow/scoring auto à finir |
| §4.2 Module logistique (douanes/transit) | — | ⛔ voir §8 |
| §4.3 Portail vendeur + stock via API | `VendorProductController`, `stock_status` | 🟡 CRUD OK, synchro API externe à faire |
| §4.4 Éligibilité dynamique (règles + ML) | `CreditScoringService` | 🟡 règles V1 OK, ML à venir |
| §4.4 Plafonds dynamiques | `CreditScoringService.recommendedLimit` | 🟡 base OK, relèvement auto après remboursements à expliciter |
| §4.4 Recouvrement automatisé gradué | `CreditService.markOverdueInstallments`, `/notifications/remind-overdue` | 🟡 base OK, phases J-3/J/J+30 → assurance à structurer |

## 2. Architecture technique (CDC §5)

| Exigence CDC | Où (code) | Statut |
|---|---|---|
| §5.1 Stack (Backend, PostgreSQL, cloud) | Spring Boot 3.4 / Java 17, PostgreSQL, Docker | ✅ (Node.js était une alternative ; Spring retenu) |
| §5.2 Découpage microservices | Monolithe modulaire | 🟡 découpage logique en place, extraction à planifier |
| §5.3 Modèle de données | 12 entités JPA (voir ERD `docs/diagrams/02`) | ✅ |
| §5.4 API REST + OAuth2/JWT | `SecurityConfig`, `JwtService`, `JwtAuthenticationFilter` | ✅ (JWT bearer) |
| §5.5 CI/CD, scalabilité, monitoring | GitHub Actions, Actuator (health/metrics), Docker | 🟡 Prometheus/Grafana + K8s manquants |

## 3. Sécurité & conformité (CDC §6)

| Exigence CDC | Où (code) | Statut |
|---|---|---|
| §6.1 PCI-DSS / tokenisation (pas de PAN stocké) | Passerelle (Stripe) | 🟡 délégué à la passerelle, à documenter |
| §6.2 Authentification forte (2FA/MFA) | — | ⛔ |
| §6.2 Moindre privilège (RBAC) | `@PreAuthorize`, rôles | ✅ |
| §6.3 Anti-fraude (Fraud Score) | — | ⛔ |
| §6.4 Chiffrement au repos (AES-256) / TLS | infra | 🟡 TLS au déploiement, at-rest à mettre en place |
| §6.5 Journal d'audit (Audit Trail) | entité `AuditLog` | 🟡 entité présente, à câbler systématiquement |
| §6.5 DRP / sauvegardes (RTO/RPO) | infra | ⛔ |

## 4. Gestion financière du crédit (CDC §7)

| Exigence CDC | Où (code) | Statut |
|---|---|---|
| §7.1 Modèle de revenus (intérêts, frais, Premium, commissions) | `CreditService`, `Customer.isPremium` | 🟡 intérêts OK, frais de dossier + gestion Premium (MRR) à faire |
| §7.2 Scoring pondéré 40/30/20/10 sur 1000 | `CreditScoringService` | ✅ conforme (V1 règles) |
| §7.3 Catégories de risque, plafonds, règles d'octroi (<400 / 400–550 / >700) | `CreditScoringService.evaluate` | ✅ |
| §7.4 Contrôle des impayés (phases graduées) | `markOverdueInstallments` | 🟡 base OK, phases + bascule assurance à structurer |
| §7.5 Partenariats bancaires / assurance-crédit | modélisé (apporteur d'affaires) | 🟡 conceptuel OK, intégration API partenaire manquante |

## 5. Logistique & UX (CDC §8–9)

| Exigence CDC | Où (code) | Statut |
|---|---|---|
| §8 Logistique cross-border (suivi, douanes, KPI) | — | ⛔ module à créer |
| §9 UX/UI mobile-first, simulateur, dashboard gamifié | Front React 19 + Vite | ✅ |
| §15.2 Schémas d'architecture (annexe) | `docs/diagrams/*.svg` | ✅ ajoutés le 9/07/2026 |

## 6. Dette technique repérée

- **Bug de concurrence** : `CreditService.lastScoring` est un champ mutable partagé sur un bean singleton → risque de résultat de scoring croisé entre requêtes simultanées. À remplacer par un retour explicite (record `App + ScoringResult`).
- **Exceptions génériques** : usage de `RuntimeException` un peu partout → introduire des exceptions métier dédiées mappées à des codes HTTP précis dans `GlobalExceptionHandler`.
- **Fichiers-notes parasites** dans les packages Java (`services/La logique métier`, `repositories/Les accès DB`, `Config/post.json`, etc.) : à déplacer vers `docs/` ou supprimer (sans extension `.java`, ils ne cassent pas la compilation mais polluent l'arbre).
- **Dev H2 vs `jsonb`** : `Transaction`/`KycDocument` utilisent `jsonb` → démarrer en Docker/PostgreSQL en dev pour éviter les échecs H2.
- **Traçabilité scoring** : le score et la décision ne sont pas persistés sur le `CreditApplication` (seulement recalculés) → à persister pour l'audit (§6.5) et l'explicabilité.

## 7. Backlog priorisé (proposition)

**P1 — cœur crédit (court terme)**
1. Corriger le partage d'état `lastScoring` (concurrence).
2. Structurer le recouvrement gradué (J-3 / J / J+1→30 / J+30 → assurance) + tâche planifiée `@Scheduled` déclenchant `markOverdueInstallments` et les rappels.
3. Expliciter le plafond dynamique : relèvement automatique après N mensualités ponctuelles (§7.3).
4. Frais de dossier + cycle de vie de l'abonnement Premium (souscription, renouvellement, MRR).
5. Persister score + décision + motif sur le dossier de crédit.

**P2 — parcours & sourcing**
6. Intégration Mobile Money réelle (TMoney/Flooz) derrière l'interface `PaymentProvider`.
7. Sourcing « Zéro Stock » : synchronisation stock/prix via API fournisseur.
8. Module Logistique (§8) : entité `Shipment`, statuts cross-border, KPI (DLM, taux de livraison à temps).

**P3 — sécurité & industrialisation (transverse)**
9. Anti-fraude : `FraudScoringService` (§6.3).
10. 2FA/MFA (§6.2), chiffrement au repos, câblage systématique de l'`AuditLog` (§6.5).
11. Observabilité (Prometheus/Grafana), CI/CD complet, déploiement Kubernetes (§5.5).
