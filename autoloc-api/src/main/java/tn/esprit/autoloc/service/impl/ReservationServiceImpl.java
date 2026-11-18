package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", dto.getVehiculeId()));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);

        ReservationResponseDTO response = reservationMapper.toDto(saved);
        response.setTarifEstime(calculerTarif(vehicule, saved));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
        ReservationResponseDTO response = reservationMapper.toDto(reservation);
        response.setTarifEstime(calculerTarif(reservation.getVehicule(), reservation));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(r -> {
                    ReservationResponseDTO dto = reservationMapper.toDto(r);
                    dto.setTarifEstime(calculerTarif(r.getVehicule(), r));
                    return dto;
                })
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", dto.getVehiculeId()));

        existant.setClient(client);
        existant.setVehicule(vehicule);
        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());

        Reservation saved = reservationRepository.save(existant);
        ReservationResponseDTO response = reservationMapper.toDto(saved);
        response.setTarifEstime(calculerTarif(vehicule, saved));
        return response;
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Réservation", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public void annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
        reservation.setStatut(StatutReservation.ANNULEE);
        reservationRepository.save(reservation);
    }

    @Override
    public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", idReservation));

        Client client = clientRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client introuvable avec le nom '" + nom + "' et prénom '" + prenom + "'"));

        reservation.setClient(client);
        Reservation saved = reservationRepository.save(reservation);

        ReservationResponseDTO response = reservationMapper.toDto(saved);
        response.setTarifEstime(calculerTarif(saved.getVehicule(), saved));
        return response;
    }

    @Override
    public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
        // Valider les dates
        if (requestDTO.getDateDebutReservation().isAfter(requestDTO.getDateFinReservation())) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }

        // Récupérer le client et le véhicule
        Client client = clientRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", requestDTO.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(requestDTO.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", requestDTO.getVehiculeId()));

        // Créer le contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(requestDTO.getDateSignatureContrat());
        contrat.setMontantTotal(requestDTO.getMontantTotalContrat());
        contrat.setValide(requestDTO.getValide() != null ? requestDTO.getValide() : true);

        // Créer la réservation et associer le contrat
        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.getDateDebutReservation());
        reservation.setDateFin(requestDTO.getDateFinReservation());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setContrat(contrat);

        // Associer la réservation au contrat (relation bidirectionnelle)
        contrat.setReservation(reservation);

        // Sauvegarder (cascade sauvegarde le contrat)
        Reservation saved = reservationRepository.save(reservation);

        ReservationResponseDTO response = reservationMapper.toDto(saved);
        response.setTarifEstime(calculerTarif(vehicule, saved));
        return response;
    }

    private BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        if (vehicule == null || vehicule.getTarifJournalier() == null
                || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return BigDecimal.ZERO;
        }
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        return vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(Math.max(jours, 0)));
    }
}