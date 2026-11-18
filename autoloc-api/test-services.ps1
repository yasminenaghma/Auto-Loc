# Script PowerShell pour tester les services d'affectation
# Usage: .\test-services.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests des Services d'Affectation" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8081/api"

# Test 1: Vérifier que l'application répond
Write-Host "Test 0: Vérification de l'application..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/clients" -Method GET -ErrorAction Stop
    Write-Host "✓ Application accessible (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "✗ Application non accessible" -ForegroundColor Red
    Write-Host "  Assurez-vous que l'application est démarrée avec: ./mvnw spring-boot:run" -ForegroundColor Red
    exit
}
Write-Host ""

# Test 2: Service 2 - Affecter équipement à véhicule (le plus simple)
Write-Host "Test 1: Affecter un équipement à un véhicule" -ForegroundColor Yellow
Write-Host "  PUT /vehicules/1/equipement/1" -ForegroundColor Gray
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/vehicules/1/equipement/1" -Method PUT -ContentType "application/json"
    Write-Host "✓ Succès!" -ForegroundColor Green
    Write-Host "  Véhicule: $($response.marque) $($response.modele) ($($response.immatriculation))" -ForegroundColor Gray
} catch {
    Write-Host "✗ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "  → Créez d'abord un véhicule et un équipement" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 3: Service 1 - Affecter réservation à client
Write-Host "Test 2: Affecter une réservation à un client" -ForegroundColor Yellow
Write-Host "  PUT /reservations/1/affecter-client?nom=Dupont&prenom=Jean" -ForegroundColor Gray
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/reservations/1/affecter-client?nom=Dupont&prenom=Jean" -Method PUT -ContentType "application/json"
    Write-Host "✓ Succès!" -ForegroundColor Green
    Write-Host "  Client: $($response.clientNomComplet)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "  → Vérifiez que la réservation 1 et le client 'Dupont Jean' existent" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 4: Service 3 - Créer maintenance pour véhicule
Write-Host "Test 3: Créer une maintenance pour un véhicule" -ForegroundColor Yellow
Write-Host "  POST /vehicules/maintenance?description=Vidange&immatriculation=ABC123" -ForegroundColor Gray
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/vehicules/maintenance?description=Vidange&immatriculation=ABC123" -Method POST -ContentType "application/json"
    Write-Host "✓ Succès!" -ForegroundColor Green
    Write-Host "  Véhicule: $($response.immatriculation)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "  → Vérifiez qu'un véhicule avec l'immatriculation 'ABC123' existe" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 5: Service 4 - Affecter contrat à réservation
Write-Host "Test 4: Affecter un contrat à une réservation" -ForegroundColor Yellow
Write-Host "  POST /contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-02-01&dateFin=2025-02-05" -ForegroundColor Gray
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/contrats/affecter-reservation?dateSignature=2025-01-15&dateDebut=2025-02-01&dateFin=2025-02-05" -Method POST -ContentType "application/json"
    Write-Host "✓ Succès!" -ForegroundColor Green
    Write-Host "  Contrat ID: $($response.id), Montant: $($response.montantTotal)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "  → Créez d'abord une réservation avec ces dates (2025-02-01 à 2025-02-05)" -ForegroundColor Yellow
    }
}
Write-Host ""

# Test 6: Service 5 - Créer réservation avec contrat
Write-Host "Test 5: Créer une réservation avec son contrat" -ForegroundColor Yellow
Write-Host "  POST /reservations/avec-contrat" -ForegroundColor Gray

$body = @{
    dateDebutReservation = "2025-03-01"
    dateFinReservation = "2025-03-05"
    clientId = 1
    vehiculeId = 1
    dateSignatureContrat = "2025-02-25"
    montantTotalContrat = 200.00
    valide = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/reservations/avec-contrat" -Method POST -Body $body -ContentType "application/json"
    Write-Host "✓ Succès!" -ForegroundColor Green
    Write-Host "  Réservation ID: $($response.id)" -ForegroundColor Gray
    Write-Host "  Client: $($response.clientNomComplet)" -ForegroundColor Gray
    Write-Host "  Véhicule: $($response.vehiculeImmatriculation)" -ForegroundColor Gray
    Write-Host "  Tarif estimé: $($response.tarifEstime) €" -ForegroundColor Gray
} catch {
    Write-Host "✗ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "  → Vérifiez que le client ID=1 et le véhicule ID=1 existent" -ForegroundColor Yellow
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests terminés" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Pour plus d'informations:" -ForegroundColor Yellow
Write-Host "  - Swagger UI: http://localhost:8081/api/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Documentation: docs/README_AFFECTATION.md" -ForegroundColor Gray
