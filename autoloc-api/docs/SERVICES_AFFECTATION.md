# Services d'Affectation - Documentation

## 📋 Vue d'ensemble

Ce document présente les 5 services d'affectation implémentés pour le projet AutoLoc API.

## ✅ Services implémentés

### 1. Affecter une réservation à un client (par nom/prénom)

**Signature:** `ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom)`

**Description:** Associe une réservation existante à un client en recherchant ce dernier par son nom et prénom.

**Endpoint REST:**
- **Méthode:** PUT
- **URL:** `/api/reservations/{idReservation}/affecter-client`
- **Paramètres:**
  - Path: `idReservation` (Long)
  - Query: `nom` (String), `prenom` (String)

**Exemple de requête:**
```bash
curl -X PUT "http://localhost:8081/api/reservations/1/affecter-client?nom=Dupont&prenom=Jean"
```

**Gestion des erreurs:**
- 404 si la réservation n'existe pas
- 404 si aucun client ne correspond au nom/prénom

---

### 2. Affecter un équipement à un véhicule

**Signature:** `VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement)`

**Description:** Ajoute un équipement à la liste des équipements d'un véhicule (relation ManyToMany).

**Endpoint REST:**
- **Méthode:** PUT
- **URL:** `/api/vehicules/{idVehicule}/equipement/{idEquipement}`
- **Paramètres:**
  - Path: `idVehicule` (Long), `idEquipement` (Long)

**Exemple de requête:**
```bash
curl -X PUT "http://localhost:8081/api/vehicules/1/equipement/2"
```

**Gestion des erreurs:**
- 404 si le véhicule n'existe pas
- 404 si l'équipement n'existe pas

**Note:** Si l'équipement est déjà affecté au véhicule, aucune duplication n'est créée.

---

### 3. Créer et affecter une maintenance à un véhicule (par immatriculation)

**Signature:** `VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation)`

**Description:** Crée une nouvelle maintenance et l'associe à un véhicule identifié par son immatriculation. La date de début est fixée automatiquement à aujourd'hui.

**Endpoint REST:**
- **Méthode:** POST
- **URL:** `/api/vehicules/maintenance`
- **Paramètres:**
  - Query: `description` (String), `immatriculation` (String)

**Exemple de requête:**
```bash
curl -X POST "http://localhost:8081/api/vehicules/maintenance?description=Vidange+moteur&immatriculation=ABC123"
```

**Gestion des erreurs:**
- 404 si aucun véhicule ne correspond à l'immatriculation

---

### 4. Créer un contrat et l'affecter à une réservation (recherche par dates)

**Signature:** `ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin)`

**Description:** Recherche une réservation correspondant aux dates fournies (dateDebut + dateFin), crée un contrat avec calcul automatique du montant basé sur le tarif du véhicule, et l'associe à la réservation.

**Endpoint REST:**
- **Méthode:** POST
- **URL:** `/api/contrats/affecter-reservation`
- **Paramètres:**
  - Query: `dateSignature` (LocalDate format ISO), `dateDebut` (LocalDate), `dateFin` (LocalDate)

**Exemple de requête:**
```bash
curl -X POST "http://localhost:8081/api/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-01-20&dateFin=2025-01-25"
```

**Gestion des erreurs:**
- 400 si dateDebut > dateFin
- 404 si aucune réservation ne correspond aux dates
- 400 si toutes les réservations trouvées ont déjà un contrat

**Logique de recherche:** 
- Si plusieurs réservations correspondent aux dates, le service sélectionne la première sans contrat associé.
- Le montant total est calculé automatiquement : `(dateFin - dateDebut) × tarifJournalier`

---

### 5. Créer une réservation avec son contrat associé en une seule transaction

**Signature:** `ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO)`

**Description:** Crée simultanément une réservation et son contrat dans une seule transaction @Transactional. Garantit la cohérence des données (si l'une échoue, l'autre est annulée).

**Endpoint REST:**
- **Méthode:** POST
- **URL:** `/api/reservations/avec-contrat`
- **Body JSON:**

```json
{
  "dateDebutReservation": "2025-02-01",
  "dateFinReservation": "2025-02-10",
  "clientId": 1,
  "vehiculeId": 2,
  "dateSignatureContrat": "2025-01-28",
  "montantTotalContrat": 450.00,
  "valide": true
}
```

**Exemple de requête:**
```bash
curl -X POST "http://localhost:8081/api/reservations/avec-contrat" \
  -H "Content-Type: application/json" \
  -d '{
    "dateDebutReservation": "2025-02-01",
    "dateFinReservation": "2025-02-10",
    "clientId": 1,
    "vehiculeId": 2,
    "dateSignatureContrat": "2025-01-28",
    "montantTotalContrat": 450.00,
    "valide": true
  }'
```

**Gestion des erreurs:**
- 400 si dateDebutReservation > dateFinReservation
- 404 si le client n'existe pas
- 404 si le véhicule n'existe pas
- 400 si les validations JSR-303 échouent (@NotNull, etc.)

---

## 📦 Fichiers créés/modifiés

### Nouveaux fichiers créés

1. **`src/main/java/tn/esprit/autoloc/web/dto/ReservationWithContratRequestDTO.java`**
   - DTO pour le service 5
   - Contient les données de réservation + contrat
   - Validation JSR-303 avec @NotNull

### Fichiers modifiés

#### Repositories
2. **`IClientRepository.java`**
   - Ajout: `Optional<Client> findByNomAndPrenom(String nom, String prenom)`

3. **`IVehiculeRepository.java`**
   - Ajout: `Optional<Vehicule> findByImmatriculation(String immatriculation)`

4. **`IReservationRepository.java`**
   - Ajout: `List<Reservation> findByDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin)`

#### Services (interfaces)
5. **`IReservationService.java`**
   - Ajout des méthodes: `affecterReservationAClient()`, `creerReservationEtContratAssocie()`

6. **`IVehiculeService.java`**
   - Ajout des méthodes: `affecterVehiculeAEquipement()`, `affecterMaintenanceAvehicule()`

7. **`IContratService.java`**
   - Ajout de la méthode: `affecterContratAReservation()`

#### Services (implémentations)
8. **`ReservationServiceImpl.java`**
   - Implémentation des 2 nouveaux services
   - Import ajouté: `Contrat`, `ReservationWithContratRequestDTO`

9. **`VehiculeServiceImpl.java`**
   - Implémentation des 2 nouveaux services
   - Imports ajoutés: `Equipment`, `Maintenance`, `LocalDate`, `IEquipmentRepository`

10. **`ContratServiceImpl.java`**
    - Implémentation du service d'affectation avec calcul automatique du montant
    - Imports ajoutés: `Reservation`, `IReservationRepository`, `BigDecimal`, `LocalDate`, `ChronoUnit`

#### Controllers
11. **`ReservationController.java`**
    - Endpoint PUT `/reservations/{id}/affecter-client`
    - Endpoint POST `/reservations/avec-contrat`

12. **`VehiculeController.java`**
    - Endpoint PUT `/vehicules/{idVehicule}/equipement/{idEquipement}`
    - Endpoint POST `/vehicules/maintenance`

13. **`ContratController.java`**
    - Endpoint POST `/contrats/affecter-reservation`
    - Ajout import `LocalDate`

#### Exceptions
14. **`ResourceNotFoundException.java`**
    - Ajout d'un constructeur `ResourceNotFoundException(String message)` pour les messages personnalisés

---

## 🔧 Points techniques

### Annotations utilisées
- `@Transactional` sur toutes les méthodes modifiant des données
- `@Valid` sur les DTO pour activer la validation JSR-303
- `@RequestParam` pour les paramètres query string
- `@PathVariable` pour les paramètres d'URL
- `@RequestBody` pour les payloads JSON

### Gestion des relations JPA
- **OneToOne** (Reservation ↔ Contrat) : cascade ALL, orphanRemoval
- **ManyToOne** (Reservation → Client, Vehicule) : association simple
- **ManyToMany** (Vehicule ↔ Equipment) : gestion bidirectionnelle
- **OneToMany** (Vehicule → Maintenance) : cascade ALL

### Validation des dates
- Vérification systématique: `dateDebut.isAfter(dateFin)` → IllegalArgumentException
- Dates au format ISO-8601 (yyyy-MM-dd)

### Calcul des montants
- Formule: `(dateFin - dateDebut) × tarifJournalier`
- Minimum 1 jour si dateFin = dateDebut

---

## ✅ Résultat de compilation

```
[INFO] Compiling 80 source files with javac [debug release 17] to target\classes
[INFO] BUILD SUCCESS
```

Le projet compile sans erreur. Tous les services respectent les signatures demandées et suivent les conventions du projet existant.

---

## 🧪 Tests recommandés

### Scénario de test complet

1. **Créer un client:**
```bash
POST /api/clients
{
  "nom": "Martin",
  "prenom": "Sophie",
  "email": "sophie.martin@example.com",
  "telephone": "0612345678",
  "numPermis": "123456789",
  "dateInscription": "2025-01-15"
}
```

2. **Créer un véhicule:**
```bash
POST /api/vehicules
{
  "immatriculation": "XYZ789",
  "marque": "Renault",
  "modele": "Clio",
  "categorie": "ECONOMIQUE",
  "tarifJournalier": 45.00,
  "statut": "DISPONIBLE",
  "agenceId": 1
}
```

3. **Créer un équipement:**
```bash
POST /api/equipements
{
  "libelle": "GPS"
}
```

4. **Affecter l'équipement au véhicule:**
```bash
PUT /api/vehicules/1/equipement/1
```

5. **Créer une réservation avec contrat:**
```bash
POST /api/reservations/avec-contrat
{
  "dateDebutReservation": "2025-02-01",
  "dateFinReservation": "2025-02-05",
  "clientId": 1,
  "vehiculeId": 1,
  "dateSignatureContrat": "2025-01-28",
  "montantTotalContrat": 180.00,
  "valide": true
}
```

6. **Affecter une maintenance au véhicule:**
```bash
POST /api/vehicules/maintenance?description=Contrôle technique&immatriculation=XYZ789
```

---

## 📊 Architecture respectée

- **Couche Controller:** Exposition REST uniquement, aucune logique métier
- **Couche Service:** Toute la logique métier + transactions
- **Couche Repository:** Requêtes Spring Data JPA
- **DTO/Mapper:** Conversion entité ↔ DTO systématique
- **Exceptions:** Gestion centralisée avec GlobalExceptionHandler

✅ **Toutes les règles du projet sont respectées.**
