# Liste complète des endpoints REST - AutoLoc API

**Base URL:** `http://localhost:8081/api`

---

## 📋 Réservations (`/reservations`)

| Méthode | Endpoint | Description | Nouveau |
|---------|----------|-------------|---------|
| POST | `/reservations` | Créer une réservation | ❌ |
| GET | `/reservations` | Lister toutes les réservations | ❌ |
| GET | `/reservations/{id}` | Consulter une réservation | ❌ |
| PUT | `/reservations/{id}` | Modifier une réservation | ❌ |
| DELETE | `/reservations/{id}` | Supprimer une réservation | ❌ |
| PUT | `/reservations/{id}/annuler` | Annuler une réservation | ❌ |
| **PUT** | **`/reservations/{id}/affecter-client`** | **Affecter un client (nom/prénom)** | ✅ **Nouveau** |
| **POST** | **`/reservations/avec-contrat`** | **Créer réservation + contrat** | ✅ **Nouveau** |

---

## 🚗 Véhicules (`/vehicules`)

| Méthode | Endpoint | Description | Nouveau |
|---------|----------|-------------|---------|
| POST | `/vehicules` | Créer un véhicule | ❌ |
| GET | `/vehicules` | Lister tous les véhicules | ❌ |
| GET | `/vehicules/{id}` | Consulter un véhicule | ❌ |
| PUT | `/vehicules/{id}` | Modifier un véhicule | ❌ |
| DELETE | `/vehicules/{id}` | Supprimer un véhicule | ❌ |
| PUT | `/vehicules/{id}/agence/{agenceId}` | Affecter à une agence | ❌ |
| PUT | `/vehicules/{id}/desaffecter` | Désaffecter d'une agence | ❌ |
| **PUT** | **`/vehicules/{idVehicule}/equipement/{idEquipement}`** | **Affecter un équipement** | ✅ **Nouveau** |
| **POST** | **`/vehicules/maintenance`** | **Créer une maintenance** | ✅ **Nouveau** |

---

## 📄 Contrats (`/contrats`)

| Méthode | Endpoint | Description | Nouveau |
|---------|----------|-------------|---------|
| POST | `/contrats` | Créer un contrat | ❌ |
| GET | `/contrats` | Lister tous les contrats | ❌ |
| GET | `/contrats/{id}` | Consulter un contrat | ❌ |
| PUT | `/contrats/{id}` | Modifier un contrat | ❌ |
| DELETE | `/contrats/{id}` | Supprimer un contrat | ❌ |
| **POST** | **`/contrats/affecter-reservation`** | **Affecter à une réservation (dates)** | ✅ **Nouveau** |

---

## 👥 Clients (`/clients`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/clients` | Créer un client |
| GET | `/clients` | Lister tous les clients |
| GET | `/clients/{id}` | Consulter un client |
| PUT | `/clients/{id}` | Modifier un client |
| DELETE | `/clients/{id}` | Supprimer un client |

---

## 🏢 Agences (`/agences`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/agences` | Créer une agence |
| GET | `/agences` | Lister toutes les agences |
| GET | `/agences/{id}` | Consulter une agence |
| PUT | `/agences/{id}` | Modifier une agence |
| DELETE | `/agences/{id}` | Supprimer une agence |

---

## 👷 Employés (`/employees`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/employees` | Créer un employé |
| GET | `/employees` | Lister tous les employés |
| GET | `/employees/{id}` | Consulter un employé |
| PUT | `/employees/{id}` | Modifier un employé |
| DELETE | `/employees/{id}` | Supprimer un employé |

---

## 🛠️ Équipements (`/equipements`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/equipements` | Créer un équipement |
| GET | `/equipements` | Lister tous les équipements |
| GET | `/equipements/{id}` | Consulter un équipement |
| PUT | `/equipements/{id}` | Modifier un équipement |
| DELETE | `/equipements/{id}` | Supprimer un équipement |

---

## 🔧 Maintenances (`/maintenances`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/maintenances` | Créer une maintenance |
| GET | `/maintenances` | Lister toutes les maintenances |
| GET | `/maintenances/{id}` | Consulter une maintenance |
| PUT | `/maintenances/{id}` | Modifier une maintenance |
| DELETE | `/maintenances/{id}` | Supprimer une maintenance |

---

## 💳 Paiements (`/paiements`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/paiements` | Créer un paiement |
| GET | `/paiements` | Lister tous les paiements |
| GET | `/paiements/{id}` | Consulter un paiement |
| PUT | `/paiements/{id}` | Modifier un paiement |
| DELETE | `/paiements/{id}` | Supprimer un paiement |

---

## 📊 Récapitulatif

**Total endpoints:** 64
- **Endpoints existants:** 59
- **Nouveaux endpoints d'affectation:** 5 ✅

---

## 🔗 Swagger UI

Accédez à la documentation interactive complète :

**URL:** http://localhost:8081/api/swagger-ui.html

Tous les endpoints sont documentés avec :
- Descriptions détaillées
- Paramètres requis/optionnels
- Schémas de requête/réponse
- Codes de réponse HTTP
- Possibilité de tester directement depuis le navigateur

---

## 🎯 Nouveaux endpoints en détail

### 1. PUT `/reservations/{id}/affecter-client`
**Params:** `nom`, `prenom`  
**Exemple:** `/reservations/1/affecter-client?nom=Dupont&prenom=Jean`

### 2. POST `/reservations/avec-contrat`
**Body:** `ReservationWithContratRequestDTO` (JSON)  
**Exemple:** Voir EXEMPLES_REQUETES_AFFECTATION.md

### 3. PUT `/vehicules/{idVehicule}/equipement/{idEquipement}`
**Path:** `idVehicule`, `idEquipement`  
**Exemple:** `/vehicules/1/equipement/2`

### 4. POST `/vehicules/maintenance`
**Params:** `description`, `immatriculation`  
**Exemple:** `/vehicules/maintenance?description=Vidange&immatriculation=ABC123`

### 5. POST `/contrats/affecter-reservation`
**Params:** `dateSignature`, `dateDebut`, `dateFin`  
**Exemple:** `/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-01-20&dateFin=2025-01-25`

---

✅ **Tous les endpoints sont opérationnels et documentés dans Swagger !**
