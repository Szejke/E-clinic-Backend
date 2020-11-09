package pl.medicalcenter.rest.users.register;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.medicalcenter.infrastructure.users.doctor.DoctorJpaRepository;
import pl.medicalcenter.infrastructure.users.register.repository.RegisterJpaRepository;

import java.util.Optional;

@CrossOrigin
@RestController
public class RegisterRest {

    private RegisterJpaRepository registerJpaRepository;
    private DoctorJpaRepository doctorJpaRepository;

    public RegisterRest(RegisterJpaRepository registerJpaRepository, DoctorJpaRepository doctorJpaRepository) {
        this.registerJpaRepository = registerJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    @PostMapping("/users/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody final RegisterResource registerResource) {
        return new ResponseEntity<>(
                Optional.ofNullable(registerResource)
                        .map(RegisterResourceConverter::of)
                        .filter(value -> !registerJpaRepository.findByEmail(value.getEmail()).isPresent())
                        .filter(value -> !doctorJpaRepository.findOneByEmail(value.getEmail()).isPresent())
                        .flatMap(value -> Optional.ofNullable(registerJpaRepository.save(value)))
                        .isPresent() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST
        );
    }
}
