package pl.medicalcenter.rest.users.register;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.medicalcenter.domain.Patient;

class RegisterResourceConverter {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    static Patient of(RegisterResource resource) {
        return Patient.builder()
                .name(resource.getName())
                .surname(resource.getSurname())
                .email(resource.getEmail())
                .password(bCryptPasswordEncoder.encode(resource.getPassword()))
                .pesel(resource.getPesel())
                .address(resource.getAddress())
                .postalCode(resource.getPostalCode())
                .build();
    }
}
