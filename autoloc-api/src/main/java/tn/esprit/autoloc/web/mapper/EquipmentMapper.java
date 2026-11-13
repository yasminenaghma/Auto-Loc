package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipment;
import tn.esprit.autoloc.web.dto.EquipmentDTO;

@Component
public class EquipmentMapper {

    public EquipmentDTO toDto(Equipment equipment) {
        if (equipment == null) {
            return null;
        }
        return new EquipmentDTO(
                equipment.getIdEquipment(),
                equipment.getLibelle()
        );
    }

    public Equipment toEntity(EquipmentDTO dto) {
        if (dto == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setIdEquipment(dto.getId());
        equipment.setLibelle(dto.getLibelle());
        return equipment;
    }

    public void updateEntity(Equipment equipment, EquipmentDTO dto) {
        if (equipment == null || dto == null) {
            return;
        }
        equipment.setLibelle(dto.getLibelle());
    }
}
