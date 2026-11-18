# Exemples de requêtes - Services d'affectation

## 🔧 Configuration de base

**Base URL:** `http://localhost:8081/api`

**Headers requis pour les POST/PUT avec body JSON:**
```
Content-Type: application/json
```

---

## 📝 Service 1: Affecter une réservation à un client

### Endpoint
```
PUT /reservations/{idReservation}/affecter-client
```

### Paramètres
- **Path:** `idReservation` (Long)
- **Query:** `nom` (String), `prenom` (String)

### Exemple cURL
```bash
curl -X PUT "http://localhost:8081/api/reservations/1/affecter-client?nom=Dupont&prenom=Jean"
```

### Exemple Postman
```
Method: PUT
URL: http://localhost:8081/api/reservations/1/affecter-client
Params:
  nom = Dupont
  prenom = Jean
```

### Réponse attendue (200 OK)
```json
{
  "id": 1,
  "dateDebut": "2025-02-01",
  "dateFin": "2025-02-05",
  "statut": "CONFIRMEE",
  "clientId": 3,
  "clientNomComplet": "Dupont Jean",
  "vehiculeId": 2,
  "vehiculeImmatriculation": "ABC123",
  "tarifEstime": 180.00
}
```

### Erreurs possibles
- **404:** Réservation introuvable
- **404:** Client introuvable avec le nom 'Dupont' et prénom 'Jean'

---

## 🚗 Service 2: Affecter un équipement à un véhicule

### Endpoint
```
PUT /vehicules/{idVehicule}/equipement/{idEquipement}
```

### Paramètres
- **Path:** `idVehicule` (Long), `idEquipement` (Long)

### Exemple cURL
```bash
curl -X PUT "http://localhost:8081/api/vehicules/1/equipement/2"
```

### Exemple Postman
```
Method: PUT
URL: http://localhost:8081/api/vehicules/1/equipement/2
```

### Réponse attendue (200 OK)
```json
{
  "id": 1,
  "immatriculation": "ABC123",
  "marque": "Renault",
  "modele": "Clio",
  "categorie": "ECONOMIQUE",
  "tarifJournalier": 45.00,
  "statut": "DISPONIBLE",
  "agenceId": 1
}
```

### Erreurs possibles
- **404:** Véhicule introuvable avec l'identifiant 1
- **404:** Équipement introuvable avec l'identifiant 2

---

## 🔧 Service 3: Créer et affecter une maintenance à un véhicule

### Endpoint
```
POST /vehicules/maintenance
```

### Paramètres
- **Query:** `description` (String), `immatriculation` (String)

### Exemple cURL
```bash
curl -X POST "http://localhost:8081/api/vehicules/maintenance?description=Vidange+moteur&immatriculation=ABC123"
```

### Exemple Postman
```
Method: POST
URL: http://localhost:8081/api/vehicules/maintenance
Params:
  description = Vidange moteur
  immatriculation = ABC123
```

### Réponse attendue (200 OK)
```json
{
  "id": 1,
  "immatriculation": "ABC123",
  "marque": "Renault",
  "modele": "Clio",
  "categorie": "ECONOMIQUE",
  "tarifJournalier": 45.00,
  "statut": "DISPONIBLE",
  "agenceId": 1
}
```

**Note:** La maintenance est créée avec `dateDebut = LocalDate.now()` automatiquement.

### Erreurs possibles
- **404:** Véhicule introuvable avec l'immatriculation 'ABC123'

---

## 📄 Service 4: Créer un contrat et l'affecter à une réservation

### Endpoint
```
POST /contrats/affecter-reservation
```

### Paramètres
- **Query:** `dateSignature` (LocalDate), `dateDebut` (LocalDate), `dateFin` (LocalDate)

### Exemple cURL
```bash
curl -X POST "http://localhost:8081/api/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-01-20&dateFin=2025-01-25"
```

### Exemple Postman
```
Method: POST
URL: http://localhost:8081/api/contrats/affecter-reservation
Params:
  dateSignature = 2025-01-15
  dateDebut = 2025-01-20
  dateFin = 2025-01-25
```

### Réponse attendue (201 CREATED)
```json
{
  "id": 5,
  "dateSignature": "2025-01-15",
  "montantTotal": 225.00,
  "valide": true,
  "reservationId": 3
}
```

**Note:** Le montant est calculé automatiquement basé sur le tarif du véhicule de la réservation.

### Erreurs possibles
- **400:** La date de début doit être antérieure à la date de fin
- **404:** Aucune réservation trouvée pour les dates : début=2025-01-20, fin=2025-01-25
- **400:** Toutes les réservations trouvées ont déjà un contrat associé

---

## 📋 Service 5: Créer une réservation avec son contrat

### Endpoint
```
POST /reservations/avec-contrat
```

### Body JSON
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

### Exemple cURL
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

### Exemple Postman
```
Method: POST
URL: http://localhost:8081/api/reservations/avec-contrat
Headers:
  Content-Type: application/json
Body (raw JSON):
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

### Réponse attendue (201 CREATED)
```json
{
  "id": 8,
  "dateDebut": "2025-02-01",
  "dateFin": "2025-02-10",
  "statut": "CONFIRMEE",
  "clientId": 1,
  "clientNomComplet": "Martin Sophie",
  "vehiculeId": 2,
  "vehiculeImmatriculation": "XYZ789",
  "tarifEstime": 405.00
}
```

**Note:** La réservation et le contrat sont créés dans une seule transaction @Transactional.

### Erreurs possibles
- **400:** La date de début doit être antérieure à la date de fin
- **400:** Validation errors (champs obligatoires manquants)
- **404:** Client introuvable avec l'identifiant 1
- **404:** Véhicule introuvable avec l'identifiant 2

---

## 🧪 Scénario de test complet

### Prérequis: Créer les données de base

#### 1. Créer une agence
```bash
curl -X POST "http://localhost:8081/api/agences" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "AutoLoc Paris",
    "adresse": "15 Rue de la République",
    "telephone": "0145678901"
  }'
```

#### 2. Créer un client
```bash
curl -X POST "http://localhost:8081/api/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Martin",
    "prenom": "Sophie",
    "email": "sophie.martin@example.com",
    "telephone": "0612345678",
    "numPermis": "123456789",
    "dateInscription": "2025-01-15"
  }'
```

#### 3. Créer un véhicule
```bash
curl -X POST "http://localhost:8081/api/vehicules" \
  -H "Content-Type: application/json" \
  -d '{
    "immatriculation": "XYZ789",
    "marque": "Renault",
    "modele": "Clio",
    "categorie": "ECONOMIQUE",
    "tarifJournalier": 45.00,
    "statut": "DISPONIBLE",
    "agenceId": 1
  }'
```

#### 4. Créer un équipement
```bash
curl -X POST "http://localhost:8081/api/equipements" \
  -H "Content-Type: application/json" \
  -d '{
    "libelle": "GPS"
  }'
```

### Tests des services d'affectation

#### Test 1: Affecter l'équipement au véhicule
```bash
curl -X PUT "http://localhost:8081/api/vehicules/1/equipement/1"
```

#### Test 2: Créer une réservation avec contrat
```bash
curl -X POST "http://localhost:8081/api/reservations/avec-contrat" \
  -H "Content-Type: application/json" \
  -d '{
    "dateDebutReservation": "2025-02-01",
    "dateFinReservation": "2025-02-05",
    "clientId": 1,
    "vehiculeId": 1,
    "dateSignatureContrat": "2025-01-28",
    "montantTotalContrat": 180.00,
    "valide": true
  }'
```

#### Test 3: Créer une autre réservation (sans contrat)
```bash
curl -X POST "http://localhost:8081/api/reservations" \
  -H "Content-Type: application/json" \
  -d '{
    "dateDebut": "2025-03-01",
    "dateFin": "2025-03-07",
    "clientId": 1,
    "vehiculeId": 1
  }'
```

#### Test 4: Affecter un contrat à la réservation précédente
```bash
curl -X POST "http://localhost:8081/api/contrats/affecter-reservation?dateSignature=2025-02-25&dateDebut=2025-03-01&dateFin=2025-03-07"
```

#### Test 5: Créer un deuxième client
```bash
curl -X POST "http://localhost:8081/api/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com",
    "telephone": "0698765432",
    "numPermis": "987654321",
    "dateInscription": "2025-01-16"
  }'
```

#### Test 6: Créer une réservation sans client puis l'affecter
```bash
# Note: Pour créer sans client, il faut modifier temporairement le service
# ou créer avec un client puis changer avec l'affectation
curl -X PUT "http://localhost:8081/api/reservations/2/affecter-client?nom=Dupont&prenom=Jean"
```

#### Test 7: Affecter une maintenance au véhicule
```bash
curl -X POST "http://localhost:8081/api/vehicules/maintenance?description=Contrôle+technique&immatriculation=XYZ789"
```

---

## 📊 Vérification dans Swagger UI

Accédez à: **http://localhost:8081/api/swagger-ui.html**

Vous trouverez les nouveaux endpoints dans les sections:
- **Réservations:** `PUT /reservations/{id}/affecter-client`, `POST /reservations/avec-contrat`
- **Véhicules:** `PUT /vehicules/{id}/equipement/{idEquipement}`, `POST /vehicules/maintenance`
- **Contrats:** `POST /contrats/affecter-reservation`

---

## ⚠️ Erreurs communes et solutions

### Erreur 404: Client introuvable
**Problème:** Le nom/prénom ne correspond à aucun client en base.

**Solution:** Vérifier l'orthographe exacte (sensible à la casse) :
```bash
curl -X GET "http://localhost:8081/api/clients"
```

### Erreur 404: Véhicule introuvable avec immatriculation
**Problème:** L'immatriculation n'existe pas en base.

**Solution:** Vérifier les immatriculations disponibles :
```bash
curl -X GET "http://localhost:8081/api/vehicules"
```

### Erreur 400: Dates invalides
**Problème:** `dateDebut > dateFin`

**Solution:** S'assurer que `dateDebut` est toujours antérieure à `dateFin`.

### Erreur 400: Toutes les réservations ont déjà un contrat
**Problème:** Le service 4 ne trouve que des réservations déjà liées à un contrat.

**Solution:** Créer une nouvelle réservation sans contrat pour les dates spécifiées.

---

## 🎯 Collection Postman

Pour importer dans Postman, créez une nouvelle collection et ajoutez ces requêtes :

1. **Affecter Client à Réservation**
   - PUT `{{baseUrl}}/reservations/1/affecter-client?nom=Dupont&prenom=Jean`

2. **Affecter Équipement à Véhicule**
   - PUT `{{baseUrl}}/vehicules/1/equipement/2`

3. **Affecter Maintenance à Véhicule**
   - POST `{{baseUrl}}/vehicules/maintenance?description=Vidange&immatriculation=ABC123`

4. **Affecter Contrat à Réservation**
   - POST `{{baseUrl}}/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-01-20&dateFin=2025-01-25`

5. **Créer Réservation avec Contrat**
   - POST `{{baseUrl}}/reservations/avec-contrat`
   - Body: voir exemple JSON ci-dessus

**Variable d'environnement:**
```
baseUrl = http://localhost:8081/api
```

---

✅ **Tous les endpoints sont fonctionnels et testables !**
