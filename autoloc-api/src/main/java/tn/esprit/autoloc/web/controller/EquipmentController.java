package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IEquipmentService;
import tn.esprit.autoloc.web.dto.EquipmentDTO;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des équipements pour véhicules")
public class EquipmentController {

    private final IEquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentDTO> create(@Valid @RequestBody EquipmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> findAll() {
        return ResponseEntity.ok(equipmentService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> update(@PathVariable Long id, @Valid @RequestBody EquipmentDTO dto) {
        return ResponseEntity.ok(equipmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}