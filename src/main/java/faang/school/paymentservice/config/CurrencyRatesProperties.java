package faang.school.paymentservice.config;

import faang.school.paymentservice.dto.Currency;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "currency.rates")
public record CurrencyRatesProperties(
        String fetchCron,
        String baseUrl,
        String apiKey,
        Currency baseCurrency,
        List<Currency> symbols,
        Duration requestTimeout
) {
}
