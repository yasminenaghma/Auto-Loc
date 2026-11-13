package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    public ReservationResponseDTO toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        
        String clientNomComplet = null;
        Long clientId = null;
        if (reservation.getClient() != null) {
            clientId = reservation.getClient().getIdClient();
            clientNomComplet = reservation.getClient().getNom() + " " + reservation.getClient().getPrenom();
        }
        
        String vehiculeImmatriculation = null;
        Long vehiculeId = null;
        if (reservation.getVehicule() != null) {
            vehiculeId = reservation.getVehicule().getIdVehicule();
            vehiculeImmatriculation = reservation.getVehicule().getImmatriculation();
        }
        
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getIdReservation());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setStatut(reservation.getStatut());
        dto.setClientId(clientId);
        dto.setClientNomComplet(clientNomComplet);
        dto.setVehiculeId(vehiculeId);
        dto.setVehiculeImmatriculation(vehiculeImmatriculation);
        
        return dto;
    }

    public Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        
        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
            reservation.setClient(client);
        }
        
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            reservation.setVehicule(vehicule);
        }
        
        return reservation;
    }

    public void updateEntity(Reservation reservation, ReservationRequestDTO dto) {
        if (reservation == null || dto == null) {
            return;
        }
        
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        
        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
            reservation.setClient(client);
        } else {
            reservation.setClient(null);
        }
        
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            reservation.setVehicule(vehicule);
        } else {
            reservation.setVehicule(null);
        }
    }
}
