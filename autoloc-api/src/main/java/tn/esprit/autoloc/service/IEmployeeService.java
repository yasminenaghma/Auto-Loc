package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {
    EmployeeDTO create(EmployeeDTO dto);
    EmployeeDTO findById(Long id);
    List<EmployeeDTO> findAll();
    EmployeeDTO update(Long id, EmployeeDTO dto);
    void deleteById(Long id);

    void affecterEmployeeAAgence(Long employeeId, Long agenceId);
    void desaffecterEmployeeDeAgence(Long employeeId);
}