# Guide PowerShell - Tester les services d'affectation

## ⚠️ Important pour Windows PowerShell

Dans PowerShell, `curl` est un **alias** pour `Invoke-WebRequest`, la syntaxe est différente de bash/Linux.

---

## 🚀 Démarrage rapide

### 1. Démarrer l'application

```powershell
# Dans le terminal PowerShell
cd C:\Users\yasmi\Auto-Loc\autoloc-api
.\mvnw spring-boot:run
```

Attendez le message : `Started AutolocApiApplication`

---

### 2. Tester automatiquement tous les services

```powershell
# Dans un NOUVEAU terminal PowerShell (laissez l'application tourner)
cd C:\Users\yasmi\Auto-Loc\autoloc-api
.\test-services.ps1
```

Ce script teste automatiquement les 5 services d'affectation.

---

## 📝 Commandes PowerShell manuelles

### Service 1: Affecter une réservation à un client

```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8081/api/reservations/1/affecter-client?nom=Dupont&prenom=Jean" -Method PUT
$response | ConvertTo-Json
```

---

### Service 2: Affecter un équipement à un véhicule

```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8081/api/vehicules/1/equipement/2" -Method PUT
$response | ConvertTo-Json
```

---

### Service 3: Créer une maintenance pour un véhicule

```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8081/api/vehicules/maintenance?description=Vidange&immatriculation=ABC123" -Method POST
$response | ConvertTo-Json
```

---

### Service 4: Affecter un contrat à une réservation

```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8081/api/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-02-01&dateFin=2025-02-05" -Method POST
$response | ConvertTo-Json
```

---

### Service 5: Créer une réservation avec contrat

```powershell
$body = @{
    dateDebutReservation = "2025-03-01"
    dateFinReservation = "2025-03-05"
    clientId = 1
    vehiculeId = 1
    dateSignatureContrat = "2025-02-25"
    montantTotalContrat = 200.00
    valide = $true
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8081/api/reservations/avec-contrat" -Method POST -Body $body -ContentType "application/json"
$response | ConvertTo-Json
```

---

## 🌐 Tester dans le navigateur

### Ouvrir Swagger UI
```
http://localhost:8081/api/swagger-ui.html
```

Vous pouvez tester tous les endpoints directement depuis l'interface web !

---

## 📋 Créer des données de test

### Créer une agence

```powershell
$agence = @{
    nom = "AutoLoc Paris"
    adresse = "15 Rue de la République"
    telephone = "0145678901"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/agences" -Method POST -Body $agence -ContentType "application/json"
```

---

### Créer un client

```powershell
$client = @{
    nom = "Dupont"
    prenom = "Jean"
    email = "jean.dupont@example.com"
    telephone = "0612345678"
    numPermis = "123456789"
    dateInscription = "2025-01-15"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/clients" -Method POST -Body $client -ContentType "application/json"
```

---

### Créer un véhicule

```powershell
$vehicule = @{
    immatriculation = "ABC123"
    marque = "Renault"
    modele = "Clio"
    categorie = "ECONOMIQUE"
    tarifJournalier = 45.00
    statut = "DISPONIBLE"
    agenceId = 1
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/vehicules" -Method POST -Body $vehicule -ContentType "application/json"
```

---

### Créer un équipement

```powershell
$equipement = @{
    libelle = "GPS"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/equipements" -Method POST -Body $equipement -ContentType "application/json"
```

---

### Créer une réservation

```powershell
$reservation = @{
    dateDebut = "2025-02-01"
    dateFin = "2025-02-05"
    clientId = 1
    vehiculeId = 1
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/reservations" -Method POST -Body $reservation -ContentType "application/json"
```

---

## 🔍 Vérifier les données existantes

```powershell
# Lister tous les clients
Invoke-RestMethod -Uri "http://localhost:8081/api/clients" -Method GET | ConvertTo-Json

# Lister tous les véhicules
Invoke-RestMethod -Uri "http://localhost:8081/api/vehicules" -Method GET | ConvertTo-Json

# Lister toutes les réservations
Invoke-RestMethod -Uri "http://localhost:8081/api/reservations" -Method GET | ConvertTo-Json

# Lister tous les équipements
Invoke-RestMethod -Uri "http://localhost:8081/api/equipements" -Method GET | ConvertTo-Json

# Lister tous les contrats
Invoke-RestMethod -Uri "http://localhost:8081/api/contrats" -Method GET | ConvertTo-Json
```

---

## ❌ Résolution des erreurs

### Erreur: "Impossible de trouver un paramètre correspondant au nom X"

**Problème:** Vous utilisez la syntaxe Linux `curl -X` dans PowerShell.

**Solution:** Utilisez `Invoke-RestMethod` au lieu de `curl`.

---

### Erreur: "localhost a refusé de se connecter"

**Problème:** L'application n'est pas démarrée.

**Solution:** Démarrez l'application avec `.\mvnw spring-boot:run`

---

### Erreur 404: "Client introuvable"

**Problème:** Le client n'existe pas en base de données.

**Solution:** Créez d'abord le client (voir section "Créer des données de test").

---

### Erreur 404: "Véhicule introuvable"

**Problème:** Le véhicule n'existe pas en base de données.

**Solution:** Créez d'abord le véhicule (voir section "Créer des données de test").

---

## 💡 Astuce : Utiliser Git Bash sous Windows

Si vous préférez la syntaxe Linux `curl`, installez **Git Bash** et utilisez-le au lieu de PowerShell :

```bash
# Dans Git Bash
curl -X PUT "http://localhost:8081/api/vehicules/1/equipement/2"
```

---

## 📚 Documentation complète

- **README_AFFECTATION.md** - Index de la documentation
- **SERVICES_AFFECTATION.md** - Documentation technique détaillée
- **EXEMPLES_REQUETES_AFFECTATION.md** - Exemples (syntaxe Linux/bash)
- **Ce fichier** - Guide PowerShell Windows

---

✅ **Maintenant vous pouvez tester les services depuis PowerShell !**
