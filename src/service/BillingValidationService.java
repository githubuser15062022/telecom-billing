package service;

import model.Subscriber;
import repository.SubscriberRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BillingValidationService {

    private final SubscriberRepository subscriberRepository;

    public BillingValidationService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void registerSubscriberFromDatabase(long msisdn) {
        System.out.println("🔎 JPA СЛОЙ: Поиск абонента в Oracle СУБД...");
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(msisdn);

        if (subscriberOptional.isPresent()) {
            Subscriber s = subscriberOptional.get();
            System.out.println("ИНФО: Абонент поднят! Тариф: " + s.getPlanId() + ", Баланс: " + s.getBalance());
        } else {
            throw new BillingValidationException("Код 404: Абонент " + msisdn + " отсутствует в СУБД Oracle!");
        }
    }

    public static void validateBalanceAndCountry(long msisdn) {
        long countryCode = msisdn / 10000000000L;
        if (countryCode != 7) {
            throw new BillingValidationException("Код 406: Допускаются только номера РФ (начинающиеся на 7)");
        }
        System.out.println("Валидация региона пройдена.");
    }
}