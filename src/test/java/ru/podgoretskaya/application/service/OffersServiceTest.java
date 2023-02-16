package ru.podgoretskaya.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import ru.podgoretskaya.application.client.ConveyorClient;
import ru.podgoretskaya.application.dto.LoanApplicationRequestDTO;
import ru.podgoretskaya.application.dto.LoanOfferDTO;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class OffersServiceTest {
    ObjectMapper objectMapper = new ObjectMapper();
    ConveyorClient conveyorClient = Mockito.mock(ConveyorClient.class);

    @Spy
    OffersService offersService = new OffersService(conveyorClient);

    @BeforeEach
    void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
        offersService.setAgeMin(18);
        offersService.setAmountMin(BigDecimal.valueOf(100000));
        offersService.setTermMin(6);
    }

    @Test
    void loanOptions() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = objectMapper.readValue(new File("src/test/resources/LoanApplicationRequestDTO.json"), LoanApplicationRequestDTO.class);
        LoanOfferDTO loanOfferDTO = objectMapper.readValue(new File("src/test/resources/LoanOfferDTO.json"), LoanOfferDTO.class);
        Mockito.when(conveyorClient.getOffersPages(Mockito.any())).thenReturn(Collections.singletonList(loanOfferDTO));
offersService.loanOptions(loanApplicationRequestDTO);
        Mockito.verify(conveyorClient).getOffersPages(loanApplicationRequestDTO);
        //---
        LoanApplicationRequestDTO loanApplicationRequestDTO1 = objectMapper.readValue(new File("src/test/resources/LoanApplicationRequestDTOWithMistakes.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO1));
        assertEquals("проверьте ФИО", illegalArgumentException.getMessage());
        //---
        LoanApplicationRequestDTO loanApplicationRequestDTO2 = objectMapper.readValue(new File("src/test/resources/LoanApplicationRequestDTOWithMistakes2.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException2 = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO2));
        assertEquals("проверьте ФИО", illegalArgumentException2.getMessage());
        //--
        LoanApplicationRequestDTO loanApplicationRequestDTO3 = objectMapper.readValue(new File("src/test/resources/LoanApplicationRequestDTOWithMistakes3.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException3 = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO3));
        assertEquals("проверьте дату рождения", illegalArgumentException3.getMessage());
        //--
        LoanApplicationRequestDTO loanApplicationRequestDTO4 = objectMapper.readValue(new File
                        ("src/test/resources/LoanApplicationRequestDTOWithMistakes4.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException4 = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO4));
        assertEquals("неверный email", illegalArgumentException4.getMessage());
        //--
        LoanApplicationRequestDTO loanApplicationRequestDTO5 = objectMapper.readValue(new File
                        ("src/test/resources/LoanApplicationRequestDTOWithMistakes5.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException5 = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO5));
        assertEquals("увеличите срок кредита", illegalArgumentException5.getMessage());
        //--
        LoanApplicationRequestDTO loanApplicationRequestDTO6 = objectMapper.readValue(new File
                        ("src/test/resources/LoanApplicationRequestDTOWithMistakes6.json"),
                LoanApplicationRequestDTO.class);
        IllegalArgumentException illegalArgumentException6 = assertThrows(IllegalArgumentException.class,
                () -> offersService.loanOptions(loanApplicationRequestDTO6));
        assertEquals("увеличите сумму кредита", illegalArgumentException6.getMessage());

    }

    @Test
    void getLoanOfferDTO() throws Exception {
        LoanOfferDTO loanOfferDTO = objectMapper.readValue(new File("src/test/resources/LoanOfferDTO.json"), LoanOfferDTO.class);
        //Mockito.when(conveyorClient.updateStatusHistory(Mockito.any())).thenReturn(Collections.singletonList(loanOfferDTO));
        offersService.getLoanOfferDTO(loanOfferDTO);
        Mockito.verify(conveyorClient).updateStatusHistory(loanOfferDTO);
    }
}