package faang.school.paymentservice.service.currency;

import faang.school.paymentservice.client.ExchangeRatesClient;
import faang.school.paymentservice.client.dto.ExchangeRatesResponse;
import faang.school.paymentservice.dto.Currency;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    private final ExchangeRatesClient exchangeRatesClient = mock(ExchangeRatesClient.class);
    private final CurrencyService currencyService = new CurrencyService(exchangeRatesClient);

    @Test
    void updateRatesShouldSaveFetchedRates() {
        ExchangeRatesResponse response = new ExchangeRatesResponse(
                true,
                "USD",
                Map.of("EUR", BigDecimal.valueOf(0.92))
        );

        when(exchangeRatesClient.fetchRates()).thenReturn(response);

        currencyService.updateRates();

        assertThat(currencyService.getRate(Currency.EUR)).isEqualByComparingTo(BigDecimal.valueOf(0.92));
    }
}
