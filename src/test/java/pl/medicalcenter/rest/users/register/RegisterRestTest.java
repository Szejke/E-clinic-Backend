package pl.medicalcenter.rest.users.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.medicalcenter.MedicalCenterApplication;
import pl.medicalcenter.domain.Patient;
import pl.medicalcenter.infrastructure.users.register.repository.RegisterJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedicalCenterApplication.class)
@AutoConfigureMockMvc
public class RegisterRestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterJpaRepository registerJpaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRegisterUser() throws Exception {
        RegisterResource registerResource = RegisterResource.builder()
                .email("test@test.com")
                .password("12345")
                .name("name")
                .surname("surname")
                .pesel("6666666666")
                .address("ulica 1")
                .postalCode("33-333")
                .build();

        mockMvc.perform(
                post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerResource)))
                .andExpect(status().isCreated());

        Optional<Patient> userByEmail = registerJpaRepository.findByEmail("test@test.com");
        assertThat(userByEmail.isPresent()).isTrue();
    }
}