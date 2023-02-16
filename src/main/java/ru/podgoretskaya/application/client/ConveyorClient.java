package ru.podgoretskaya.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.podgoretskaya.application.dto.LoanApplicationRequestDTO;
import ru.podgoretskaya.application.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "deal",url ="http://localhost:8081")
public interface ConveyorClient {
    @PostMapping(value = "/deal/application")
    List<LoanOfferDTO> getOffersPages(@RequestBody LoanApplicationRequestDTO model);

    @PutMapping(value = "/deal/offer")
    void updateStatusHistory(@RequestBody LoanOfferDTO model);
}
