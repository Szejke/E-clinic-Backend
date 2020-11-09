package pl.medicalcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.medicalcenter.infrastructure.users.doctor.DoctorJpaRepository;
import pl.medicalcenter.infrastructure.users.patient.PatientJpaRepository;
import pl.medicalcenter.infrastructure.users.register.repository.RegisterJpaRepository;
import pl.medicalcenter.infrastructure.users.visits.doctor.DoctorVisitsJpaRepository;
import pl.medicalcenter.infrastructure.users.visits.patient.PatientVisitsJpaRepository;

@EntityScan(basePackages = "pl.medicalcenter.domain")
@EnableJpaRepositories(
        basePackageClasses = {
                RegisterJpaRepository.class,
                DoctorVisitsJpaRepository.class,
                PatientVisitsJpaRepository.class,
                PatientJpaRepository.class,
                DoctorJpaRepository.class
        }
)
@SpringBootApplication(scanBasePackages = {"pl.medicalcenter"}, exclude = {SecurityAutoConfiguration.class})
public class MedicalCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalCenterApplication.class, args);
    }
}
