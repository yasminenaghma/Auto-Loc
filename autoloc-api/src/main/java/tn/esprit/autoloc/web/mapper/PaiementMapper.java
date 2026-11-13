package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.web.dto.PaiementDTO;

@Component
@RequiredArgsConstructor
public class PaiementMapper {

    private final IContratRepository contratRepository;

    public PaiementDTO toDto(Paiement paiement) {
        if (paiement == null) {
            return null;
        }
        return new PaiementDTO(
                paiement.getIdPaiement(),
                paiement.getMontant(),
                paiement.getDatePaiement(),
                paiement.getModePaiement(),
                paiement.getContrat() != null ? paiement.getContrat().getIdContrat() : null
        );
    }

    public Paiement toEntity(PaiementDTO dto) {
        if (dto == null) {
            return null;
        }
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(dto.getId());
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setModePaiement(dto.getModePaiement());
        
        if (dto.getContratId() != null) {
            Contrat contrat = contratRepository.findById(dto.getContratId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contrat", dto.getContratId()));
            paiement.setContrat(contrat);
        }
        
        return paiement;
    }

    public void updateEntity(Paiement paiement, PaiementDTO dto) {
        if (paiement == null || dto == null) {
            return;
        }
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setModePaiement(dto.getModePaiement());
        
        if (dto.getContratId() != null) {
            Contrat contrat = contratRepository.findById(dto.getContratId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contrat", dto.getContratId()));
            paiement.setContrat(contrat);
        } else {
            paiement.setContrat(null);
        }
    }
}
