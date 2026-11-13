package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employee;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.web.dto.EmployeeDTO;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final IAgenceRepository agenceRepository;

    public EmployeeDTO toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeDTO(
                employee.getIdEmployee(),
                employee.getNom(),
                employee.getPrenom(),
                employee.getRole(),
                employee.getAgence() != null ? employee.getAgence().getIdAgence() : null
        );
    }

    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setIdEmployee(dto.getId());
        employee.setNom(dto.getNom());
        employee.setPrenom(dto.getPrenom());
        employee.setRole(dto.getRole());
        
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", dto.getAgenceId()));
            employee.setAgence(agence);
        }
        
        return employee;
    }

    public void updateEntity(Employee employee, EmployeeDTO dto) {
        if (employee == null || dto == null) {
            return;
        }
        employee.setNom(dto.getNom());
        employee.setPrenom(dto.getPrenom());
        employee.setRole(dto.getRole());
        
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", dto.getAgenceId()));
            employee.setAgence(agence);
        } else {
            employee.setAgence(null);
        }
    }
}
