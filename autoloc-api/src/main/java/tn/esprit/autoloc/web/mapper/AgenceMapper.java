package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceDTO;

@Component
public class AgenceMapper {

    public AgenceDTO toDto(Agence agence) {
        if (agence == null) {
            return null;
        }
        return new AgenceDTO(
                agence.getIdAgence(),
                agence.getNom(),
                agence.getVille(),
                agence.getAdresse(),
                agence.getTelephone()
        );
    }

    public Agence toEntity(AgenceDTO dto) {
        if (dto == null) {
            return null;
        }
        Agence agence = new Agence();
        agence.setIdAgence(dto.getId());
        agence.setNom(dto.getNom());
        agence.setVille(dto.getVille());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
        return agence;
    }

    public void updateEntity(Agence agence, AgenceDTO dto) {
        if (agence == null || dto == null) {
            return;
        }
        agence.setNom(dto.getNom());
        agence.setVille(dto.getVille());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
    }
}
