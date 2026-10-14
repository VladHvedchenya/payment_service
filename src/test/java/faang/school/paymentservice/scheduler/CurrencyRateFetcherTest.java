package faang.school.paymentservice.scheduler;

import faang.school.paymentservice.service.currency.CurrencyService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CurrencyRateFetcherTest {

    private final CurrencyService currencyService = mock(CurrencyService.class);
    private final CurrencyRateFetcher currencyRateFetcher = new CurrencyRateFetcher(currencyService);

    @Test
    void fetchRatesShouldUpdateCurrencyRates() {
        currencyRateFetcher.fetchRates();

        verify(currencyService).updateRates();
    }
}
