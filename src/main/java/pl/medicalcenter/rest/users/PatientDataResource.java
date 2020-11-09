package pl.medicalcenter.rest.users;

import lombok.Data;

@Data
public class PatientDataResource {
    private String name;
    private String surname;
    private String pesel;
    private String address;
    private String postalCode;
}
