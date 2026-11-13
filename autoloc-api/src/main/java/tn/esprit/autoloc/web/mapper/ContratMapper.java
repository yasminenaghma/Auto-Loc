package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratDTO;

@Component
public class ContratMapper {

    public ContratDTO toDto(Contrat contrat) {
        if (contrat == null) {
            return null;
        }
        ContratDTO dto = new ContratDTO();
        dto.setId(contrat.getIdContrat());
        dto.setDateSignature(contrat.getDateSignature());
        dto.setMontantTotal(contrat.getMontantTotal());
        dto.setValide(contrat.getValide());
        dto.setReservationId(contrat.getReservation() != null ? contrat.getReservation().getIdReservation() : null);
        return dto;
    }

    public Contrat toEntity(ContratDTO dto) {
        if (dto == null) {
            return null;
        }
        Contrat contrat = new Contrat();
        contrat.setIdContrat(dto.getId());
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.getValide());
        return contrat;
    }

    public void updateEntity(Contrat contrat, ContratDTO dto) {
        if (contrat == null || dto == null) {
            return;
        }
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.getValide());
    }
}
