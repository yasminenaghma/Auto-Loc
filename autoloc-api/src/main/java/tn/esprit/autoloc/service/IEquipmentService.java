package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.EquipmentDTO;

import java.util.List;

public interface IEquipmentService {
    EquipmentDTO create(EquipmentDTO dto);
    EquipmentDTO findById(Long id);
    List<EquipmentDTO> findAll();
    EquipmentDTO update(Long id, EquipmentDTO dto);
    void deleteById(Long id);
}