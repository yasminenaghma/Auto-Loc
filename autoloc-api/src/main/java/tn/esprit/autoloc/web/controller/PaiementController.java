package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule)")
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un paiement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<PaiementDTO> findById(@Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les paiements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée")
    })
    public ResponseEntity<List<PaiementDTO>> findAll() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    @PostMapping
    @Operation(summary = "Créer un paiement (refusé - méthode non autorisée)")
    @ApiResponses({
            @ApiResponse(responseCode = "405", description = "Méthode non autorisée - les paiements sont créés via les contrats")
    })
    public ResponseEntity<Void> create() {
        throw new UnsupportedOperationException("La création de paiements se fait via les contrats");
    }
}