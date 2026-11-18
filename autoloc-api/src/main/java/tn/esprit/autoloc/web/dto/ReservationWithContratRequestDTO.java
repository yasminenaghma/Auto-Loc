package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithContratRequestDTO {
    
    // Données de la réservation
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebutReservation;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFinReservation;
    
    @NotNull(message = "Le client est obligatoire")
    private Long clientId;
    
    @NotNull(message = "Le véhicule est obligatoire")
    private Long vehiculeId;
    
    // Données du contrat
    @NotNull(message = "La date de signature du contrat est obligatoire")
    private LocalDate dateSignatureContrat;
    
    @NotNull(message = "Le montant total du contrat est obligatoire")
    private BigDecimal montantTotalContrat;
    
    private Boolean valide = true;
}
