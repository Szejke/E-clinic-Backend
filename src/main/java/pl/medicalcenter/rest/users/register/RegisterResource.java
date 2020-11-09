package pl.medicalcenter.rest.users.register;

import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
class RegisterResource {

    @Email
    @Size(min = 3, max = 254, message = "Invalid email length. Required from 3 to 255")
    private String email;

    @Size(max = 35)
    private String password;

    @Size(min = 3, max = 35)
    private String name;

    @Size(min = 3, max = 35)
    private String surname;

    @Size(min = 11, max = 11, message = "Invalid pesel code length. Required 11")
    private String pesel;

    private String address;

    @Size(min = 6, max = 6, message = "Invalid postal code length. Required 6")
    private String postalCode;
}
