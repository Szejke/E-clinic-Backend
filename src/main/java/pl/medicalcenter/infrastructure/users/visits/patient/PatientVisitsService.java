package pl.medicalcenter.infrastructure.users.visits.patient;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.domain.Payment;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.domain.VisitStatuses;
import pl.medicalcenter.infrastructure.users.patient.PatientJpaRepository;
import pl.medicalcenter.infrastructure.users.patient.PatientNotFoundException;
import pl.medicalcenter.infrastructure.users.visits.doctor.VisitNotFoundException;
import pl.medicalcenter.infrastructure.users.visits.event.*;
import pl.medicalcenter.rest.BusinessLogicRestException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Optional.ofNullable;
import static pl.medicalcenter.domain.VisitStatuses.*;
import static pl.medicalcenter.rest.ErrorDescriptions.*;

@Service
public class PatientVisitsService {

    private ApplicationEventPublisher applicationEventPublisher;

    private PatientVisitsJpaRepository patientVisitsJpaRepository;

    private PatientJpaRepository patientJpaRepository;

    public PatientVisitsService(ApplicationEventPublisher applicationEventPublisher, PatientVisitsJpaRepository patientVisitsJpaRepository, PatientJpaRepository patientJpaRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.patientVisitsJpaRepository = patientVisitsJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
    }

    public ResponseEntity reserveVisit(Long visitId, Long patientId) {
        try {
            Visit reservatedVisit = patientVisitsJpaRepository.findVisitByIdAndPatientIsNull(visitId)
                    .filter(visit -> VisitStatuses.NEW.equals(visit.getStatus()) ||
                            VisitStatuses.POSTPONED.equals(visit.getStatus()) ||
                            VisitStatuses.ABORTED_BY_PATIENT.equals(visit.getStatus()))
                    .map(visit -> {
                        visit.setStatus(RESERVED);
                        visit.setPatient(patientJpaRepository.findById(patientId).orElseThrow(PatientNotFoundException::new));
                        visit.setPayment(Payment.builder().paid(BigDecimal.ZERO).build());
                        return patientVisitsJpaRepository.save(visit);
                    })
                    .orElseThrow(ReservationException::new);
            applicationEventPublisher.publishEvent(new VisitReservationEvent(reservatedVisit));
            return ResponseEntity.accepted().build();
        } catch (ReservationException e) {
            throw new BusinessLogicRestException(RESERVATION.getValue());
        } catch (PatientNotFoundException e) {
            throw new BusinessLogicRestException(PATIENT_NOT_FOUND.getValue());
        }
    }

    public ResponseEntity abortVisit(Long visitId, Long patientId) {
        try {
            patientVisitsJpaRepository.findVisitByIdAndPatientId(visitId, patientId)
                    .filter(vis -> vis.getStatus().equals(VisitStatuses.NEW) || vis.getStatus().equals(VisitStatuses.ABORTED_BY_PATIENT) || vis.getStatus().equals(VisitStatuses.POSTPONED) || vis.getStatus().equals(VisitStatuses.RESERVED) || vis.getStatus().equals(VisitStatuses.PAID))
                    .map(vis -> {
                        vis.setStatus(ABORTED_BY_PATIENT);
                        vis.getPayment().setPaid(BigDecimal.ZERO);
                        vis.setPatient(null);
                        return patientVisitsJpaRepository.save(vis);
                    })
                    .orElseThrow(VisitNotFoundException::new);
            applicationEventPublisher.publishEvent(new VisitAbortEvent(patientJpaRepository.findById(patientId).orElseThrow(PatientNotFoundException::new)));
            return ResponseEntity.accepted().build();
        } catch (VisitNotFoundException e) {
            throw new BusinessLogicRestException(VISIT_NOT_FOUND.getValue());
        }
    }

    public ResponseEntity postponeVisit(Long visitId, Long patientId, Long otherVisitId) {
        try {
            Visit visitBeforePostpone = patientVisitsJpaRepository.findVisitByIdAndPatientId(visitId, patientId)
                    .filter(vis -> vis.getStatus().equals(PAID) || vis.getStatus().equals(RESERVED) || vis.getStatus().equals(POSTPONED))
                    .orElseThrow(VisitNotFoundException::new);
            Visit visitAfterPostpone = patientVisitsJpaRepository.findVisitByIdAndPatientIsNull(otherVisitId)
                    .orElseThrow(VisitNotFoundException::new);
            Patient patient = patientJpaRepository.findById(patientId)
                    .orElseThrow(PatientNotFoundException::new);
            ApplicationEvent event = null;

            BigDecimal moneyToGiveBack = moneyToGiveBack(visitBeforePostpone);

            if (giveMoneyBack(moneyToGiveBack)) {
                event = new GiveBackMoneyAfterPostponeVisitEvent(patient, moneyToGiveBack, visitBeforePostpone);
            } else if (isSurcharge(moneyToGiveBack)) {
                event = new ToPaidAfterPostponeVisitEvent(patient, visitAfterPostpone.getPrice());
            }
            visitBeforePostpone.getPayment().setPaid(BigDecimal.ZERO);
            visitBeforePostpone.setStatus(POSTPONED);
            visitBeforePostpone.setPatient(null);
            visitAfterPostpone.setStatus(RESERVED);
            visitAfterPostpone.setPatient(patient);

            patientVisitsJpaRepository.save(visitBeforePostpone);
            patientVisitsJpaRepository.save(visitAfterPostpone);

            ofNullable(event)
                    .ifPresent(e -> applicationEventPublisher.publishEvent(e));
            return ResponseEntity.accepted().build();
        } catch (VisitNotFoundException e) {
            throw new BusinessLogicRestException(VISIT_NOT_FOUND.getValue());
        } catch (PatientNotFoundException e) {
            throw new BusinessLogicRestException(PATIENT_NOT_FOUND.getValue());
        }
    }

    private BigDecimal moneyToGiveBack(Visit reservedVisit) {
        return reservedVisit.getPayment().getPaid();
    }

    private boolean giveMoneyBack(BigDecimal moneyDifference) {
        return moneyDifference.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isSurcharge(BigDecimal moneyDifference) {
        return moneyDifference.compareTo(BigDecimal.ZERO) < 0;
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity findVisitsForPostponeByDate(Long patientId, Long visitId, Integer year, Integer month, Integer day) {
        try {
            Visit visit = patientVisitsJpaRepository.findVisitByIdAndPatientId(visitId, patientId)
                    .orElseThrow(VisitNotFoundException::new);
            return new ResponseEntity(patientVisitsJpaRepository.findVisitsToPostponeByDay(year, month, day, visit.getDoctor().getId(), visitId), HttpStatus.OK);
        } catch (VisitNotFoundException e) {
            throw new BusinessLogicRestException(VISIT_NOT_FOUND.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity findVisitsForPostpone(Long patientId, Long visitId) {
        LocalDate today = LocalDate.now();
        try {
            Visit visit = patientVisitsJpaRepository.findVisitByIdAndPatientId(visitId, patientId)
                    .orElseThrow(VisitNotFoundException::new);
            return new ResponseEntity(patientVisitsJpaRepository.findVisitsToPostpone(today.getYear(), today.getMonth().getValue() - 1, today.getDayOfMonth(), visit.getDoctor().getId(), visitId), HttpStatus.OK);
        } catch (VisitNotFoundException e) {
            throw new BusinessLogicRestException(VISIT_NOT_FOUND.getValue());
        }
    }

    public ResponseEntity pay(Long visitId, Long patientId) {
        try {
            Visit visit = patientVisitsJpaRepository.findVisitByIdAndPatientId(visitId, patientId)
                    .filter(vis -> vis.getStatus().equals(RESERVED) || vis.getStatus().equals(POSTPONED))
                    .orElseThrow(VisitNotFoundException::new);

            visit.getPayment().setPaid(visit.getPrice());
            visit.setStatus(PAID);

            applicationEventPublisher.publishEvent(new VisitPaidEvent(patientVisitsJpaRepository.save(visit)));
            return ResponseEntity.accepted().build();
        } catch (VisitNotFoundException e) {
            throw new BusinessLogicRestException(VISIT_NOT_FOUND.getValue());
        } catch (PatientNotFoundException e) {
            throw new BusinessLogicRestException(PATIENT_NOT_FOUND.getValue());
        }
    }
}
