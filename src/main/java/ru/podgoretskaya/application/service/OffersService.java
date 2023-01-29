package ru.podgoretskaya.application.service;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.podgoretskaya.application.client.ConveyorClient;
import ru.podgoretskaya.application.dto.LoanApplicationRequestDTO;
import ru.podgoretskaya.application.dto.LoanOfferDTO;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class OffersService {

    @Value("${amountMin}")
    private  BigDecimal amountMin;
    @Value("${termMin}")
    private int termMin;
    @Value("${ageMin}")
    private  int ageMin;
private final ConveyorClient conveyorClient;

    private void firstLastMiddleNameOffers(LoanApplicationRequestDTO model) {
        Pattern patlatletter = Pattern.compile("^[a-zA-Z]{2,30}$");
        log.debug("имя " + model.getFirstName());
        Matcher firstNameLatLetter = patlatletter.matcher(model.getFirstName());
        if (!firstNameLatLetter.matches()) {
            log.info("проверьте имя" + model.getFirstName());
            throw new IllegalArgumentException("проверьте ФИО");
        }
        log.debug("фамилия " + model.getLastName());
        Matcher lastNameLatLetter = patlatletter.matcher(model.getLastName());
        if (!lastNameLatLetter.matches()) {
            log.info("проверьте фамилия" + model.getFirstName());
            throw new IllegalArgumentException("проверьте ФИО");
        }
        if (model.getMiddleName() != null) {
            log.debug("отчество " + model.getMiddleName());
            Matcher middleNameLatLetter = patlatletter.matcher(model.getMiddleName());
            if (!middleNameLatLetter.matches()) {
                log.info("проверьте отчество" + model.getMiddleName());
                throw new IllegalArgumentException("проверьте ФИО");
            }
        }
    }

    private void amountOffers(LoanApplicationRequestDTO model) {
        log.debug("запрошенная сумма " + model.getAmount());
        int compare = model.getAmount().compareTo(amountMin);
        if (compare < 0) {
            log.info("увеличите сумму кредита" + model.getAmount());
            throw new IllegalArgumentException("увеличите сумму кредита");
        }
    }

    private void termOffers(LoanApplicationRequestDTO model) {
        log.debug("срок кредита " + model.getTerm());
        int compare = model.getTerm().compareTo(termMin);
        if (compare < 0) {
            log.info("увеличите срок кредита" + model.getTerm());
            throw new IllegalArgumentException("увеличите срок кредита");
        }
    }

    private void birthdateOffers(LoanApplicationRequestDTO model) {
        LocalDate date = LocalDate.now();
        log.debug("дата рождения " + model.getBirthdate());
        int age = date.compareTo(model.getBirthdate());
        if (age < ageMin) {
            log.info("проверьте дату рождения" + model.getBirthdate());
            throw new IllegalArgumentException("проверьте дату рождения");
        }
    }

    private void passportOffers(LoanApplicationRequestDTO model) {
        log.debug("серия номер " + model.getPassportSeries() + ", " + model.getPassportNumber());
        int lengthPassportSeries = model.getPassportSeries().length();
        int lengthPassportNumber = model.getPassportNumber().length();
        if (!((lengthPassportSeries == 4) && (lengthPassportNumber == 6))) {
            log.info("проверьте данные паспорта" + model.getPassportSeries() + ", " + model.getPassportNumber());
            throw new IllegalArgumentException("проверьте данные паспорта");
        }
    }

    private void emailOffers(LoanApplicationRequestDTO model) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern patEmail = Pattern.compile(regex);
        log.debug("email " + model.getEmail());
        Matcher emailOffers = patEmail.matcher(model.getEmail());
        if (!emailOffers.matches()) {
            log.info("неверный email" + model.getEmail());
            throw new IllegalArgumentException("неверный email");
        }
    }

    public List<LoanOfferDTO> loanOptions(LoanApplicationRequestDTO model) {
        firstLastMiddleNameOffers(model);
        amountOffers(model);
        termOffers(model);
        birthdateOffers(model);
        passportOffers(model);
        emailOffers(model);
        log.info("отправка запроса в deal, с параметрами: " + model);
        return conveyorClient.getOffersPages(model);
    }
    public void getLoanOfferDTO(LoanOfferDTO model){
        log.info("выбранный вариан LoanOfferDTO: " + model);
        conveyorClient.updateStatusHistory(model);
    }
}
