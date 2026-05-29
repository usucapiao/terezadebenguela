package br.com.instituto.teresa.controller;

import br.com.instituto.teresa.dto.DiscographyTrackResponseDTO;
import br.com.instituto.teresa.service.DiscographyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiscographyController.class)
public class DiscographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiscographyService discographyService;

    @Test
    public void testGetAllTracks() throws Exception {
        DiscographyTrackResponseDTO track = new DiscographyTrackResponseDTO(
            1L, "code", "Test Track", "Artist", "file.mp3"
        );

        Mockito.when(discographyService.getAllTracks()).thenReturn(Arrays.asList(track));

        mockMvc.perform(get("/api/discography"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Test Track"));
    }
}
