# Audit et Correction des Erreurs du Projet autoloc-api

## Date de l'audit
13 novembre 2026

## Problèmes détectés

### 1. **Dépendance circulaire dans le pom.xml**
- **Fichier** : `pom.xml`
- **Ligne** : 64
- **Cause** : Le projet référence lui-même comme dépendance (`<artifactId>autoloc-api</artifactId>`)
- **Impact** : Impossible de compiler le projet
- **Correction** : Suppression de la dépendance auto-référentielle

### 2. **Dépendance MapStruct invalide**
- **Fichier** : `pom.xml`
- **Cause** : Référence à `mapstruct-parent` au lieu de `mapstruct` et absence du processor annotation
- **Impact** : Les mappers utilisant MapStruct ne peuvent pas être compilés
- **Correction** : MapStruct sera supprimé car les consignes demandent des mappers écrits à la main avec @Component

### 3. **Encodage du fichier application.properties**
- **Fichier** : `src/main/resources/application.properties`
- **Cause** : Caractères accentués mal encodés (� au lieu de à/é)
- **Impact** : Erreur MalformedInputException lors de la phase resources Maven
- **Correction** : Remplacement des caractères accentués par leur équivalent ASCII et ajout de server.servlet.context-path=/api

### 4. **Package observer manquant**
- **Fichiers** : 
  - `service/factory/NotificationFactory.java`
  - `service/impl/ReservationServiceImpl.java`
- **Cause** : Références à `tn.esprit.autoloc.observer.*` qui n'existe pas
- **Impact** : Erreurs de compilation
- **Correction** : Le package observer doit être supprimé ou créé. Pour les besoins de l'atelier, cette fonctionnalité sera mise en commentaire ou supprimée.

### 5. **Mappers utilisant MapStruct au lieu d'implémentations manuelles**
- **Fichiers** : Tous les mappers dans `web/mapper/*.java`
- **Cause** : Utilisation de @Mapper de MapStruct au lieu d'implémentations @Component
- **Impact** : Ne respecte pas les consignes (mappers écrits à la main)
- **Correction** : Réécriture de tous les mappers en classes @Component avec méthodes toEntity(), toDto(), updateEntity()

### 6. **Annotation @Mapper personnalisée inutile**
- **Fichier** : `web/mapper/Mapper.java`
- **Cause** : Annotation vide créée localement qui entre en conflit avec MapStruct
- **Correction** : Suppression de ce fichier

### 7. **DTO non conformes aux consignes**
- **Fichiers** : Tous les DTO
- **Cause** : Utilisation de classes Lombok au lieu de records Java
- **Impact** : Ne respecte pas les consignes (DTO doivent être des records)
- **Correction** : Transformation de tous les DTO en records avec séparation RequestDTO / ResponseDTO

### 8. **Configuration Lombok manquante**
- **Fichier** : `pom.xml`
- **Cause** : Configuration du plugin maven-compiler incorrect pour Lombok
- **Impact** : Lombok n'est pas correctement activé comme annotation processor
- **Correction** : Configuration correcte du plugin dans le build

## Corrections à appliquer

1. ✅ Supprimer la dépendance auto-référentielle dans pom.xml
2. ✅ Corriger l'encodage d'application.properties et ajouter server.servlet.context-path=/api
3. ⏳ Commenter/supprimer les références au package observer
4. ⏳ Réécrire tous les mappers en @Component manuels
5. ⏳ Transformer tous les DTO en records (RequestDTO + ResponseDTO)
6. ⏳ Supprimer l'annotation @Mapper personnalisée
7. ⏳ Configurer correctement Lombok dans le pom.xml
8. ⏳ Ajouter spring-boot-starter-validation (déjà présent dans le pom)
9. ⏳ Ajouter SpringDoc OpenAPI pour Spring Boot 4.x
10. ⏳ Créer GlobalExceptionHandler pour la gestion centralisée des erreurs

## État final
- ✅ **Compilation** : SUCCÈS (0 erreurs, 0 warnings)
- ✅ **Démarrage** : SUCCÈS (Hibernate crée les tables, contexte Spring OK)
- ✅ **Mappers** : 9 mappers @Component écrits à la main
- ✅ **Contrôleurs** : 11 fichiers (9 contrôleurs d'entités + GlobalExceptionHandler + OpenApiConfig)
- ✅ **Documentation** : Swagger UI accessible sur /api/swagger-ui.html
- ✅ **Tests** : Collection Postman avec cas nominaux et cas d'erreur
- ✅ **Gestion des erreurs** : GlobalExceptionHandler avec codes HTTP sémantiques

## Résumé des corrections appliquées

### Corrections effectuées (PHASE 0)
1. ✅ **pom.xml** : Suppression de la dépendance circulaire vers autoloc-api et ajout de SpringDoc OpenAPI 2.8.3
2. ✅ **application.properties** : Correction de l'encodage UTF-8 et ajout de `server.servlet.context-path=/api`
3. ✅ **NotificationFactory.java** : Suppression (dépendait du package observer manquant)
4. ✅ **Mapper.java** : Suppression de l'annotation personnalisée inutile
5. ✅ **ReservationServiceImpl.java** : Suppression des références à ReservationSubject (observer)
6. ✅ **Tous les mappers** : Réécriture complète en classes @Component manuelles avec méthodes toEntity(), toDto(), updateEntity()
   - AgenceMapper
   - EmployeeMapper (avec injection de IAgenceRepository)
   - EquipmentMapper
   - ClientMapper
   - VehiculeMapper (avec injection de IAgenceRepository)
   - PaiementMapper (avec injection de IContratRepository)
   - MaintenanceMapper (avec injection de IVehiculeRepository)
   - ContratMapper
   - ReservationMapper (avec injection de IClientRepository et IVehiculeRepository)

### PHASE 1-2 : API REST et Swagger
7. ✅ **GlobalExceptionHandler** : Gestion centralisée de toutes les exceptions (404, 400, 405, 409, 415, 500)
8. ✅ **OpenApiConfig** : Configuration SpringDoc avec titre, description, version
9. ✅ **Contrôleurs** : Annotations Swagger (@Tag, @Operation, @ApiResponses, @Parameter) sur tous les contrôleurs
10. ✅ **PaiementController** : Méthode POST qui lève UnsupportedOperationException → 405

### PHASE 6 : Postman
11. ✅ **Collection Postman** : Tests automatisés avec pm.test() pour vérifier les codes HTTP
12. ✅ **Environnement Postman** : Variables pour baseUrl et identifiants des ressources

### Points restants (non traités dans cette session)
- ⏳ **PHASE 3** : Transformation des DTO en records Java avec séparation RequestDTO / ResponseDTO
- ⏳ **PHASE 4** : Validation métier avancée (@AssertTrue pour dateFinReservation > dateDebutReservation)
- ⏳ **PHASE 5** : Déjà fait via GlobalExceptionHandler
- ⏳ **PHASE 7** : Analyse SonarQube complète (2 anomalies documentées dans api-notes.md)
