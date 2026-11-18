package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO create(ContratDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        Contrat saved = contratRepository.save(contratMapper.toEntity(dto));
        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        return contratMapper.toDto(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream().map(contratMapper::toDto).toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));

        existant.setDateSignature(dto.getDateSignature());
        existant.setMontantTotal(dto.getMontantTotal());
        existant.setValide(dto.getValide());

        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    public ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
        // Valider les dates
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }

        // Rechercher les réservations correspondant aux dates fournies
        List<Reservation> reservations = reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin);

        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Aucune réservation trouvée pour les dates : début=" + dateDebut + ", fin=" + dateFin);
        }

        // Si plusieurs réservations correspondent, prendre la première sans contrat
        Reservation reservation = reservations.stream()
                .filter(r -> r.getContrat() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Toutes les réservations trouvées ont déjà un contrat associé"));

        // Calculer le montant total basé sur le tarif du véhicule
        BigDecimal montantTotal = calculerMontantTotal(reservation);

        // Créer le contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(true);
        contrat.setReservation(reservation);

        // Associer le contrat à la réservation
        reservation.setContrat(contrat);

        // Sauvegarder le contrat
        Contrat saved = contratRepository.save(contrat);

        return contratMapper.toDto(saved);
    }

    private BigDecimal calculerMontantTotal(Reservation reservation) {
        if (reservation.getVehicule() == null || reservation.getVehicule().getTarifJournalier() == null) {
            return BigDecimal.ZERO;
        }
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        return reservation.getVehicule().getTarifJournalier()
                .multiply(BigDecimal.valueOf(Math.max(jours, 1)));
    }
}