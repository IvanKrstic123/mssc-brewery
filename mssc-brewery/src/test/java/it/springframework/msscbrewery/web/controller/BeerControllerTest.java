package it.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.springframework.msscbrewery.web.model.BeerDto;
import it.springframework.msscbrewery.web.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validDto;

    @BeforeEach
    void setUp() {
        validDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Beer1")
                .beerStyle("PALE_ALE")
                .upc(13213213213L)
                .build();
    }

    @Test
    void getBeer() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(validDto);

        mockMvc.perform(get("/api/v1/beer/" + validDto.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validDto.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(validDto.getBeerName().toString())));
    }

    @Test
    void handlePost() throws Exception {
        //given
        BeerDto beerDto = validDto;
        beerDto.setId(null);
        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).beerName("New Beer").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any())).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    void handleUpdate() throws Exception {
        //given
        BeerDto beerDto = validDto;
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc.perform(put("/api/v1/beer/" + validDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());

        then(beerService).should().updateBeer(any(), any());
    }
}