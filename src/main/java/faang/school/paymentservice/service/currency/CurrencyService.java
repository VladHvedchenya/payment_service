package faang.school.paymentservice.service.currency;

import faang.school.paymentservice.client.ExchangeRatesClient;
import faang.school.paymentservice.client.dto.ExchangeRatesResponse;
import faang.school.paymentservice.dto.Currency;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final ExchangeRatesClient exchangeRatesClient;
    private final Map<Currency, BigDecimal> rates = new ConcurrentHashMap<>();

    public void updateRates() {
        ExchangeRatesResponse response = exchangeRatesClient.fetchRates();

        response.rates().forEach((currencyCode, rate) ->
                rates.put(Currency.valueOf(currencyCode), rate)
        );
    }

    public BigDecimal getRate(Currency currency) {
        return rates.get(currency);
    }
}
