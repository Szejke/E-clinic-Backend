package pl.medicalcenter.rest.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.medicalcenter.MedicalCenterApplication;
import pl.medicalcenter.infrastructure.users.patient.PatientJpaRepository;
import pl.medicalcenter.infrastructure.users.register.repository.RegisterJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedicalCenterApplication.class)
@AutoConfigureMockMvc(secure = false)
public class PatientRestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PatientJpaRepository patientJpaRepository;

    @Autowired
    private RegisterJpaRepository registerJpaRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

//    @Test
//    public void shouldFindDoctorVisits() throws Exception {
//        User user = registerJpaRepository.save(User.builder().build());
//        PatientDataResource resource = PatientDataResource.builder()
//                .name("newname")
//                .surname("newsurname")
//                .pesel("12345678909")
//                .address("newaddress")
//                .postalCode("99-000")
//                .build();
//
//        mockMvc.perform(
//                patch("/patients/" + user.getId())
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(resource)))
//                .andExpect(status().isOk());
//
//        User updatedUser = registerJpaRepository.findById(user.getId()).orElseThrow(PatientNotFoundException::new);
//        assertThat(updatedUser.getName()).isEqualTo(resource.getName());
//        assertThat(updatedUser.getSurname()).isEqualTo(resource.getSurname());
//        assertThat(updatedUser.getPesel()).isEqualTo(resource.getPesel());
//        assertThat(updatedUser.getAddress()).isEqualTo(resource.getAddress());
//        assertThat(updatedUser.getPostalCode()).isEqualTo(resource.getPostalCode());
//    }
}