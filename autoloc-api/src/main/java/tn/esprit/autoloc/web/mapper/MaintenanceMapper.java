package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;

@Component
@RequiredArgsConstructor
public class MaintenanceMapper {

    private final IVehiculeRepository vehiculeRepository;

    public MaintenanceDTO toDto(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }
        return new MaintenanceDTO(
                maintenance.getIdMaintenance(),
                maintenance.getDateDebut(),
                maintenance.getDateFin(),
                maintenance.getDescription(),
                maintenance.getVehicule() != null ? maintenance.getVehicule().getIdVehicule() : null
        );
    }

    public Maintenance toEntity(MaintenanceDTO dto) {
        if (dto == null) {
            return null;
        }
        Maintenance maintenance = new Maintenance();
        maintenance.setIdMaintenance(dto.getId());
        maintenance.setDateDebut(dto.getDateDebut());
        maintenance.setDateFin(dto.getDateFin());
        maintenance.setDescription(dto.getDescription());
        
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            maintenance.setVehicule(vehicule);
        }
        
        return maintenance;
    }

    public void updateEntity(Maintenance maintenance, MaintenanceDTO dto) {
        if (maintenance == null || dto == null) {
            return;
        }
        maintenance.setDateDebut(dto.getDateDebut());
        maintenance.setDateFin(dto.getDateFin());
        maintenance.setDescription(dto.getDescription());
        
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            maintenance.setVehicule(vehicule);
        } else {
            maintenance.setVehicule(null);
        }
    }
}
