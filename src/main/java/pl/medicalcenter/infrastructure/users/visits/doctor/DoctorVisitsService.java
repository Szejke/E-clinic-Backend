package pl.medicalcenter.infrastructure.users.visits.doctor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.medicalcenter.domain.Doctor;
import pl.medicalcenter.domain.Payment;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.infrastructure.users.doctor.DoctorJpaRepository;
import pl.medicalcenter.infrastructure.users.doctor.DoctorNotFoundException;
import pl.medicalcenter.rest.BusinessLogicRestException;
import pl.medicalcenter.rest.visits.doctor.AddVisitsResource;
import pl.medicalcenter.rest.visits.doctor.UpdateVisitResource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.medicalcenter.domain.VisitStatuses.DONE;
import static pl.medicalcenter.domain.VisitStatuses.NEW;
import static pl.medicalcenter.rest.ErrorDescriptions.DOCTOR_NOT_FOUND;
import static pl.medicalcenter.rest.ErrorDescriptions.VISIT_ALREADY_EXIST_IN_TIME;

@Service
public class DoctorVisitsService {

    private DoctorVisitsJpaRepository doctorVisitsJpaRepository;

    private DoctorJpaRepository doctorJpaRepository;

    public DoctorVisitsService(DoctorVisitsJpaRepository doctorVisitsJpaRepository, DoctorJpaRepository doctorJpaRepository) {
        this.doctorVisitsJpaRepository = doctorVisitsJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    public void tryDeleteVisit(Long visitId, Long doctorId) {
        doctorVisitsJpaRepository.findVisitByIdAndDoctorId(visitId, doctorId)
                .filter(visit -> !DONE.equals(visit.getStatus()))
                .ifPresent(visit -> {
                    // return money
                    doctorVisitsJpaRepository.delete(visit);
                });
    }

    public void tryUpdateVisit(Long visitId, Long doctorId, UpdateVisitResource resource) throws VisitNotFoundException {
        doctorVisitsJpaRepository.findVisitByIdAndDoctorId(visitId, doctorId)
                .map(visit -> {
                    visit.setReceipt(resource.getReceipt());
                    visit.setDiagnosis(resource.getDiagnosis());
                    visit.setStatus(DONE);
                    return doctorVisitsJpaRepository.save(visit);
                }).orElseThrow(VisitNotFoundException::new);
    }

    public ResponseEntity tryAddVisits(Long doctorId, AddVisitsResource resource) {
        try {
            Doctor doctor = doctorJpaRepository.findById(doctorId)
                    .orElseThrow(DoctorNotFoundException::new);
            List<Visit> visitsToAdd = new ArrayList<>();
            Set<Visit> visits = doctorVisitsJpaRepository.findAllByDoctorIdAndYearAndMonthAndDay(doctorId, resource.getYear(), resource.getMonth(), resource.getDay());

            Integer startHour = resource.getStartHour();
            Integer startMinutes = resource.getStartMinutes();
            Integer duration = resource.getDuration();

            for (int i = 0; i < resource.getNumberOfVisits(); i++) {
                Integer finalStartHour = startHour;
                Integer finalStartMinutes = startMinutes;
                Set<Visit> visitsInTime = visits.stream()
                        .filter(visit -> visit.getHour().equals(finalStartHour))
                        .filter(visit -> visit.getMinutes().equals(finalStartMinutes))
                        .collect(Collectors.toSet());

                if (visitsInTime.isEmpty()) {
                    visitsToAdd.add(Visit.builder()
                            .status(NEW)
                            .doctor(doctor)
                            .price(resource.getPrice())
                            .duration(duration)
                            .year(resource.getYear())
                            .month(resource.getMonth())
                            .day(resource.getDay())
                            .hour(finalStartHour)
                            .minutes(finalStartMinutes)
                            .build());
                    startMinutes += duration;
                    if (startMinutes > 59) {
                        startHour++;
                        startMinutes -= 60;
                        if (startHour > 23) {
                            break;
                        }
                    }
                } else {
                    throw new VisitAlreadyExistInTimeException();
                }
            }
            Set<Visit> visitWithPayments = ((List<Visit>) doctorVisitsJpaRepository.save(visitsToAdd))
                    .stream()
                    .peek(visit -> visit.setPayment(
                            Payment.builder()
                                    .paid(BigDecimal.ZERO)
                                    .visit(visit)
                                    .build()
                    )).collect(Collectors.toSet());
            doctorVisitsJpaRepository.save(visitWithPayments);
            return ResponseEntity.accepted().build();
        } catch (DoctorNotFoundException e) {
            throw new BusinessLogicRestException(DOCTOR_NOT_FOUND.getValue());
        } catch (VisitAlreadyExistInTimeException e) {
            throw new BusinessLogicRestException(VISIT_ALREADY_EXIST_IN_TIME.getValue());
        }
    }
}
