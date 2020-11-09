package pl.medicalcenter.rest.visits.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.medicalcenter.MedicalCenterApplication;
import pl.medicalcenter.domain.Doctor;
import pl.medicalcenter.domain.Visit;
import pl.medicalcenter.domain.VisitStatuses;
import pl.medicalcenter.infrastructure.users.doctor.DoctorJpaRepository;
import pl.medicalcenter.infrastructure.users.visits.doctor.DoctorVisitsJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedicalCenterApplication.class)
@AutoConfigureMockMvc(secure = false)
public class DoctorVisitsRestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DoctorVisitsJpaRepository repository;

    @Autowired
    private DoctorJpaRepository doctorJpaRepository;

    private MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldFindDoctorVisits() throws Exception {
        Doctor doctor = doctorJpaRepository.save(Doctor.builder().build());
        Visit visit = repository.save(Visit.builder().doctor(doctor).build());
        repository.save(visit);

        MvcResult mvcResult = mockMvc.perform(
                get("/visits/doctor/" + doctor.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Visit[] visits = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Visit[].class);
        assertThat(visits).isNotEmpty();
    }

    @Test
    public void shouldDeleteVisit() throws Exception {
        Doctor doctor = doctorJpaRepository.save(Doctor.builder().build());
        Visit visit = repository.save(Visit.builder().doctor(doctor).build());
        repository.save(visit);

        mockMvc.perform(
                delete("/visits/" + visit.getId() + "/doctor/" + doctor.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Optional<Visit> visitByIdAndDoctorId = repository.findVisitByIdAndDoctorId(visit.getId(), doctor.getId());
        assertThat(visitByIdAndDoctorId).isEmpty();
    }

    @Test
    public void shouldNotDeleteVisit() throws Exception {
        Doctor doctor = doctorJpaRepository.save(Doctor.builder().build());
        Visit visit = repository.save(Visit.builder().status(VisitStatuses.DONE).doctor(doctor).build());
        repository.save(visit);

        mockMvc.perform(
                delete("/visits/" + visit.getId() + "/doctor/" + doctor.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Optional<Visit> visitByIdAndDoctorId = repository.findVisitByIdAndDoctorId(visit.getId(), doctor.getId());
        assertThat(visitByIdAndDoctorId).isNotEmpty();
    }

}