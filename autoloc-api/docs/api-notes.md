# Notes API autoloc-api

## A. Endpoints

| Contrôleur | Méthode | Route | Service | Succès | Erreurs |
|------------|---------|-------|---------|--------|---------|
| AgenceController | POST | /api/agences | IAgenceService.create | 201 | 400 |
| AgenceController | GET | /api/agences/{id} | IAgenceService.findById | 200 | 404 |
| AgenceController | GET | /api/agences | IAgenceService.findAll | 200 | - |
| AgenceController | PUT | /api/agences/{id} | IAgenceService.update | 200 | 400, 404 |
| AgenceController | DELETE | /api/agences/{id} | IAgenceService.deleteById | 204 | 404 |
| EmployeeController | POST | /api/employees | IEmployeeService.create | 201 | 400 |
| EmployeeController | GET | /api/employees/{id} | IEmployeeService.findById | 200 | 404 |
| EmployeeController | GET | /api/employees | IEmployeeService.findAll | 200 | - |
| EmployeeController | PUT | /api/employees/{id} | IEmployeeService.update | 200 | 400, 404 |
| EmployeeController | DELETE | /api/employees/{id} | IEmployeeService.deleteById | 204 | 404 |
| EmployeeController | PUT | /api/employees/{id}/agence/{agenceId} | IEmployeeService.affecterEmployeeAAgence | 200 | 404 |
| EmployeeController | PUT | /api/employees/{id}/desaffecter | IEmployeeService.desaffecterEmployeeDeAgence | 200 | 404 |
| VehiculeController | POST | /api/vehicules | IVehiculeService.create | 201 | 400 |
| VehiculeController | GET | /api/vehicules/{id} | IVehiculeService.findById | 200 | 404 |
| VehiculeController | GET | /api/vehicules | IVehiculeService.findAll | 200 | - |
| VehiculeController | PUT | /api/vehicules/{id} | IVehiculeService.update | 200 | 400, 404 |
| VehiculeController | DELETE | /api/vehicules/{id} | IVehiculeService.deleteById | 204 | 404 |
| VehiculeController | PUT | /api/vehicules/{id}/agence/{agenceId} | IVehiculeService.affecterVehiculeAAgence | 200 | 404 |
| VehiculeController | PUT | /api/vehicules/{id}/desaffecter | IVehiculeService.desaffecterVehiculeDeAgence | 200 | 404 |
| EquipmentController | POST | /api/equipements | IEquipmentService.create | 201 | 400 |
| EquipmentController | GET | /api/equipements/{id} | IEquipmentService.findById | 200 | 404 |
| EquipmentController | GET | /api/equipements | IEquipmentService.findAll | 200 | - |
| EquipmentController | PUT | /api/equipements/{id} | IEquipmentService.update | 200 | 400, 404 |
| EquipmentController | DELETE | /api/equipements/{id} | IEquipmentService.deleteById | 204 | 404 |
| ClientController | POST | /api/clients | IClientService.create | 201 | 400 |
| ClientController | GET | /api/clients/{id} | IClientService.findById | 200 | 404 |
| ClientController | GET | /api/clients | IClientService.findAll | 200 | - |
| ClientController | PUT | /api/clients/{id} | IClientService.update | 200 | 400, 404 |
| ClientController | DELETE | /api/clients/{id} | IClientService.deleteById | 204 | 404 |
| ReservationController | POST | /api/reservations | IReservationService.create | 201 | 400 |
| ReservationController | GET | /api/reservations/{id} | IReservationService.findById | 200 | 404 |
| ReservationController | GET | /api/reservations | IReservationService.findAll | 200 | - |
| ReservationController | PUT | /api/reservations/{id} | IReservationService.update | 200 | 400, 404 |
| ReservationController | DELETE | /api/reservations/{id} | IReservationService.deleteById | 204 | 404 |
| ReservationController | PUT | /api/reservations/{id}/annuler | IReservationService.annulerReservation | 200 | 404 |
| ContratController | POST | /api/contrats | IContratService.create | 201 | 400 |
| ContratController | GET | /api/contrats/{id} | IContratService.findById | 200 | 404 |
| ContratController | GET | /api/contrats | IContratService.findAll | 200 | - |
| ContratController | PUT | /api/contrats/{id} | IContratService.update | 200 | 400, 404 |
| ContratController | DELETE | /api/contrats/{id} | IContratService.deleteById | 204 | 404 |
| PaiementController | GET | /api/paiements/{id} | IPaiementService.findById | 200 | 404 |
| PaiementController | GET | /api/paiements | IPaiementService.findAll | 200 | - |
| PaiementController | POST | /api/paiements | Aucun (UnsupportedOperation) | - | 405 |
| MaintenanceController | POST | /api/maintenances | IMaintenanceService.create | 201 | 400 |
| MaintenanceController | GET | /api/maintenances/{id} | IMaintenanceService.findById | 200 | 404 |
| MaintenanceController | GET | /api/maintenances | IMaintenanceService.findAll | 200 | - |
| MaintenanceController | PUT | /api/maintenances/{id} | IMaintenanceService.update | 200 | 400, 404 |
| MaintenanceController | DELETE | /api/maintenances/{id} | IMaintenanceService.deleteById | 204 | 404 |

## B. DTO vs annotations Jackson

### Avant (utilisation d'annotations Jackson)

Les annotations `@JsonManagedReference` et `@JsonBackReference` étaient utilisées directement sur les entités JPA pour éviter les boucles infinies lors de la sérialisation JSON des associations bidirectionnelles.

**Problèmes :**
- Les entités JPA étaient exposées directement aux clients de l'API
- Risque de lazy loading exceptions (proxy Hibernate)
- Couplage fort entre la couche de persistance et la couche web
- Impossible de personnaliser la représentation JSON sans modifier les entités
- Les annotations Jackson polluaient le code métier

### Après (utilisation de DTO)

Les DTO (Data Transfer Objects) sont des classes/records dédiés à la représentation des données échangées avec l'API. Ils remplacent complètement les entités JPA dans les contrôleurs.

**Avantages :**
- Séparation claire entre la couche de persistance (entités) et la couche présentation (DTO)
- Contrôle total sur les données exposées (pas de fuite d'informations sensibles)
- Pas de proxy Hibernate dans les réponses JSON
- Les annotations Jackson deviennent inutiles sur les entités
- Facilite l'évolution indépendante du modèle de données et de l'API
- Meilleure performance (pas de chargement lazy non maîtrisé)

## C. Postman et Swagger : complémentarité

### Swagger UI (SpringDoc OpenAPI)

**URL d'accès** : `http://localhost:8080/api/swagger-ui.html`

**Ce qu'il apporte :**
- Documentation interactive et auto-générée à partir du code
- Possibilité de tester les endpoints directement depuis le navigateur
- Description des schémas JSON (modèles de requête et réponse)
- Visualisation claire de toutes les routes disponibles, regroupées par tag
- Accessible à tous (développeurs, testeurs, clients) sans installation

**Exemple concret :**
Quand je clique sur "Agences" dans Swagger UI, je vois tous les endpoints du contrôleur AgenceController avec leurs descriptions, paramètres attendus, et codes de réponse. Je peux cliquer sur "POST /api/agences", remplir un JSON exemple, cliquer "Execute" et voir la réponse.

### Postman

**Ce qu'il apporte :**
- Tests automatisés avec scripts JavaScript (assertions, extraction de variables)
- Collections partagées entre équipes avec gestion de versions
- Environnements multiples (local, dev, staging, prod)
- Tests de régression systématiques
- Scénarios complexes (création → consultation → modification → suppression)
- Chaînage de requêtes (ex : créer un contrat, extraire son ID, l'utiliser dans une autre requête)

**Exemple concret :**
Dans la collection Postman, le test "Créer un contrat" contient un script qui extrait l'ID du contrat créé (`pm.environment.set("contratId", ...)`) et le réutilise automatiquement dans les requêtes "Modifier le contrat" et "Supprimer le contrat". Cela permet de rejouer toute la suite de tests sans intervention manuelle.

**Complémentarité :**
- **Swagger** = documentation et exploration manuelle rapide
- **Postman** = tests automatisés, intégration continue, validation de bout en bout

## D. Anomalies SonarQube

### 1. Dépendance circulaire dans pom.xml (Critique)
- **Règle** : Circular dependency
- **Explication** : Le projet référençait lui-même comme dépendance Maven, créant une boucle infinie
- **Correction** : Suppression de la dépendance `<artifactId>autoloc-api</artifactId>` du bloc dependencies

### 2. Encodage de fichier incorrect (Bloquant)
- **Règle** : File encoding should be UTF-8
- **Explication** : Le fichier `application.properties` contenait des caractères accentués mal encodés (� au lieu de à/é)
- **Correction** : Remplacement des caractères accentués par leur équivalent ASCII

### 3. Package manquant référencé (Critique)
- **Règle** : Unresolved imports
- **Explication** : Les classes `NotificationFactory` et `ReservationServiceImpl` importaient le package `tn.esprit.autoloc.observer` qui n'existait pas
- **Correction** : Suppression de `NotificationFactory` et des références à `ReservationSubject` dans `ReservationServiceImpl`

### 4. Utilisation de MapStruct non configuré (Majeur)
- **Règle** : Dependencies should be correctly configured
- **Explication** : Les mappers utilisaient l'annotation `@Mapper` de MapStruct, mais la dépendance était incorrecte (`mapstruct-parent` au lieu de `mapstruct`) et l'annotation processor manquait
- **Correction** : Suppression de MapStruct et réécriture de tous les mappers en classes `@Component` manuelles

### 5. Annotation personnalisée inutile (Mineur)
- **Règle** : Useless code should be removed
- **Explication** : Un fichier `Mapper.java` définissait une annotation vide `@interface Mapper` qui entrait en conflit avec MapStruct
- **Correction** : Suppression du fichier

### 6. Imports inutilisés (Mineur)
- **Règle** : Unused imports should be removed
- **Explication** : Plusieurs contrôleurs avaient des imports inutilisés après refactoring
- **Correction** : Nettoyage automatique des imports lors de la réécriture des contrôleurs

## E. Questions techniques

### 1. Quel composant reçoit les requêtes et comment trouve-t-il la méthode ?

**DispatcherServlet** est le point d'entrée de toutes les requêtes HTTP dans une application Spring MVC. C'est un servlet frontal (Front Controller) qui :

1. Reçoit la requête HTTP entrante
2. Consulte le **HandlerMapping** pour trouver le contrôleur et la méthode correspondants
3. Le HandlerMapping examine les annotations `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc. pour faire le mapping URL → méthode
4. Invoque la méthode du contrôleur via un **HandlerAdapter**
5. Reçoit le résultat (objet Java) et le convertit en JSON via les **HttpMessageConverters** (Jackson)
6. Renvoie la réponse HTTP au client

**Exemple** : Quand une requête `GET /api/agences/1` arrive, le DispatcherServlet interroge le HandlerMapping qui trouve `AgenceController.findById(@PathVariable Long id)` grâce au mapping `/agences/{id}`.

### 2. Pourquoi dépendre de l'interface du service et pas du repository ni de l'implémentation ?

**Principe d'inversion de dépendance (SOLID)** : les modules de haut niveau (contrôleurs) ne doivent pas dépendre des modules de bas niveau (implémentations), mais des abstractions (interfaces).

**Avantages :**
- **Testabilité** : on peut facilement créer un mock de IAgenceService pour tester le contrôleur sans base de données
- **Flexibilité** : on peut changer l'implémentation (ex : passer de JPA à MongoDB) sans toucher au contrôleur
- **Cohérence architecturale** : le contrôleur ne fait pas de logique métier, il délègue tout au service

**Pourquoi pas le repository ?**
- Le contrôleur deviendrait un "gros service" avec de la logique métier → violation du Single Responsibility Principle
- Impossible de réutiliser la logique métier dans d'autres contextes (batch, MQ, etc.)
- Couplage fort avec la couche de persistance

### 3. Pourquoi 201, 204, 404 et pas 500 ?

**Principe** : les codes HTTP doivent refléter précisément ce qui s'est passé, pas simplement "ça a marché" ou "ça a échoué".

- **201 (Created)** : Indique qu'une nouvelle ressource a été créée avec succès (POST). Le client sait qu'il peut maintenant utiliser l'ID retourné pour consulter ou modifier la ressource.
- **204 (No Content)** : Indique que l'opération a réussi mais qu'il n'y a pas de corps de réponse (DELETE). Économise de la bande passante et évite de renvoyer null.
- **404 (Not Found)** : Indique que la ressource demandée n'existe pas. C'est une erreur côté client (mauvais ID), pas une erreur serveur.
- **500 (Internal Server Error)** : Signifie qu'une exception inattendue s'est produite côté serveur (bug, perte de connexion DB). Ce n'est PAS approprié quand une ressource n'existe simplement pas.

**Pourquoi c'est important :**
- Les clients API (front-end, mobile) peuvent réagir différemment selon le code
- 404 → afficher "Ressource introuvable", proposer de rafraîchir
- 500 → afficher "Erreur serveur", contacter le support
- Les proxies et caches HTTP traitent différemment 2xx, 4xx et 5xx

### 4. Pourquoi Paiement est en lecture seule et que renvoie POST /paiements ?

**Règle métier** : Les paiements sont créés automatiquement lors de la création d'un contrat (composition forte). Un paiement n'existe pas sans contrat.

**Conséquences** :
- Le contrôleur `PaiementController` n'expose que GET (consultation)
- Si on tente POST /api/paiements → **405 Method Not Allowed**
- La cascade JPA `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)` garantit que les paiements sont créés/supprimés avec le contrat

**Avantages** :
- Cohérence des données (pas de paiement orphelin)
- Logique métier centralisée dans ContratService
- API plus claire pour les clients (ils savent qu'ils doivent passer par /contrats)

### 5. Risques de l'exposition directe des entités JPA et justification des DTO

**Risques :**

1. **Lazy Loading Exceptions** : Si une collection est LAZY et qu'on sérialise l'entité hors transaction, on obtient une `LazyInitializationException` ou un proxy Hibernate dans le JSON.

2. **Boucles infinies** : Les associations bidirectionnelles (Agence ↔ Vehicule) créent des boucles lors de la sérialisation JSON, provoquant un `StackOverflowError`.

3. **Fuite d'informations** : Les entités peuvent contenir des champs sensibles (mot de passe, données internes) qu'on ne veut pas exposer.

4. **Couplage fort** : Toute modification du schéma de base de données impacte directement l'API → breaking change pour les clients.

5. **Performance** : Impossible de contrôler finement les données chargées → surcharge réseau et mémoire.

**Justification des DTO :**

- **Contrat d'API stable** : Les DTO définissent un contrat clair et versionnisable, indépendant du modèle de données.
- **Sécurité** : Contrôle total sur ce qui est exposé (principe du moindre privilège).
- **Performance** : Chargement optimisé des données (pas de N+1, pas de proxy).
- **Testabilité** : Plus facile de mocker et tester des objets simples que des entités avec leurs dépendances JPA.
- **Évolutivité** : Le modèle de données peut évoluer sans casser l'API (ajout de tables, refactoring).

**Conclusion** : Les DTO sont un investissement initial qui paie rapidement en maintenabilité, sécurité et performance.
