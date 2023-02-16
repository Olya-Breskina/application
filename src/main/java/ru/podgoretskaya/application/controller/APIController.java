package ru.podgoretskaya.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.podgoretskaya.application.dto.LoanApplicationRequestDTO;
import ru.podgoretskaya.application.dto.LoanOfferDTO;
import ru.podgoretskaya.application.service.OffersService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j//логгер
@Tag(name = "Перенос прескоринга в МС-application", description = "Методы для реализации прескоринга")
public class APIController {
    private final OffersService offersService;

    @PostMapping(value = "/application")
    @Operation(summary = "прескоринг")
    public ResponseEntity<List<LoanOfferDTO>> getOffersPages(@Parameter @RequestBody LoanApplicationRequestDTO model) {
        log.info("вызов /conveyor/offers. Параметры: \"" + model.toString());
        try {
            return new ResponseEntity<>(offersService.loanOptions(model), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.debug("ошибка заполнения формы");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/application/offer")
    @Operation(summary = "сохранение выбранного варианта прескоринга")
    public void updateStatusHistory(@RequestBody LoanOfferDTO model) {
        log.debug("вызов /offer. Параметры: \"" + model.toString());
        offersService.getLoanOfferDTO(model);
    }
}
