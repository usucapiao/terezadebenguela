package br.com.instituto.teresa.controller;

import br.com.instituto.teresa.dto.VolunteerRequestDTO;
import br.com.instituto.teresa.service.VolunteerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VolunteerController.class)
public class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VolunteerService volunteerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SuppressWarnings("null")
    public void testCreateVolunteer_Success() throws Exception {
        VolunteerRequestDTO volunteerDTO = new VolunteerRequestDTO(
            "John Doe", "john@example.com", null, "25", "To help out."
        );

        Mockito.doNothing().when(volunteerService).processVolunteer(ArgumentMatchers.any(VolunteerRequestDTO.class));

        mockMvc.perform(post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Candidatura enviada com sucesso!"));
    }

    @Test
    @SuppressWarnings("null")
    public void testCreateVolunteer_ValidationError() throws Exception {
        // Missing name and email properties triggering validation error
        VolunteerRequestDTO volunteerDTO = new VolunteerRequestDTO(
            null, null, null, null, null
        );

        mockMvc.perform(post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteerDTO)))
                .andExpect(status().isBadRequest());
    }
}
