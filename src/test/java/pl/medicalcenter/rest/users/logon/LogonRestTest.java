package pl.medicalcenter.rest.users.logon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.medicalcenter.MedicalCenterApplication;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedicalCenterApplication.class)
@AutoConfigureMockMvc(secure = false)
public class LogonRestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAuthorizeUser() throws Exception {
//        User user = User.builder().email("email@email.com").password("12345").build();
//
//        mockMvc.perform(
//                post("/users/logon")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .content(objectMapper.writeValueAsString(user))
//        ).andExpect(status().isOk());
    }
}