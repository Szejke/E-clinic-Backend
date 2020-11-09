package pl.medicalcenter.infrastructure.users.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.medicalcenter.domain.Doctor;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.infrastructure.users.doctor.DoctorJpaRepository;
import pl.medicalcenter.infrastructure.users.patient.PatientJpaRepository;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private PatientJpaRepository patientJpaRepository;

    private DoctorJpaRepository doctorJpaRepository;

    public AppUserDetailsService(PatientJpaRepository patientJpaRepository, DoctorJpaRepository doctorJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Patient> patientByEmail = patientJpaRepository.findByEmail(email);
        Optional<Doctor> doctorByEmail = doctorJpaRepository.findOneByEmail(email);
        if (patientByEmail.isPresent()) {
            Patient patient = patientByEmail.get();
            return new org.springframework.security.core.userdetails.
                    User(patient.getEmail(), patient.getPassword(), Arrays.asList(new SimpleGrantedAuthority("patient"), new SimpleGrantedAuthority(String.valueOf(patient.getId()))));
        } else if (doctorByEmail.isPresent()) {
            Doctor doctor = doctorByEmail.get();
            return new org.springframework.security.core.userdetails.
                    User(doctor.getEmail(), ofNullable(doctor.getPassword()).orElse(""), Arrays.asList(new SimpleGrantedAuthority("doctor"), new SimpleGrantedAuthority(String.valueOf(doctor.getId()))));
        } else {
            throw new UsernameNotFoundException("User with email " + email +" doesn't exist");
        }
    }
}
