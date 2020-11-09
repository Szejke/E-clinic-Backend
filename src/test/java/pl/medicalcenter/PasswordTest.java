package pl.medicalcenter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.annotations.Test;

public class PasswordTest {

    @Test
    public void passwordTest() {
        System.out.println(new BCryptPasswordEncoder().encode("medicalcenter123!@#"));
    }
}
