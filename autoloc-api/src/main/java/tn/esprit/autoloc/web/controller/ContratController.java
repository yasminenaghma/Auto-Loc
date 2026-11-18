package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    @Operation(summary = "Créer un nouveau contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<ContratDTO> create(@Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un contrat par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<ContratDTO> findById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les contrats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée")
    })
    public ResponseEntity<List<ContratDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<ContratDTO> update(@Parameter(description = "Identifiant du contrat") @PathVariable Long id,
                                            @Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.ok(contratService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat (cascade vers les paiements)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Nouvel endpoint d'affectation
    @PostMapping("/affecter-reservation")
    @Operation(summary = "Créer un contrat et l'affecter à une réservation existante basée sur les dates")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé et affecté avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucune réservation trouvée pour ces dates"),
            @ApiResponse(responseCode = "400", description = "Dates invalides ou réservation déjà associée à un contrat")
    })
    public ResponseEntity<ContratDTO> affecterContratAReservation(
            @RequestParam LocalDate dateSignature,
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.affecterContratAReservation(dateSignature, dateDebut, dateFin));
    }
}