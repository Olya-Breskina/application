package ru.podgoretskaya.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.podgoretskaya.application.service.OffersService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(APIController.class)
class APIControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OffersService offersService;
    @Test
    void getOffersPages() throws Exception {
        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                            {
                                                "firstName":"qwe",
                                                "middleName":"qwe",
                                                "lastName":"qwe",
                                                "birthdate":"1994-12-13",
                                                "passportSeries":"1234",
                                                "passportNumber":"123456",
                                                "email":"a@mail.ru",
                                                "amount":100000,
                                                "term":12
                                            }
                                """))
                .andExpect(status().isOk());
        when(offersService.loanOptions(any())).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateStatusHistory() throws Exception{
        mockMvc.perform(put("/application/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                      {
                                            "applicationId": 1,
                                            "requestedAmount": 100000,
                                            "totalAmount": 107115.48,
                                            "term": 6,
                                            "monthlyPayment": 17852.58,
                                            "rate": 24,
                                            "isInsuranceEnabled": false,
                                            "isSalaryClient": false
                                        }
                                """))
                .andExpect(status().isOk());
    }
}