package faang.school.paymentservice.client;

import faang.school.paymentservice.client.dto.ExchangeRatesResponse;
import faang.school.paymentservice.config.CurrencyRatesProperties;
import faang.school.paymentservice.exception.CurrencyRatesFetchException;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ExchangeRatesClient {

    private final WebClient.Builder webClientBuilder;
    private final CurrencyRatesProperties properties;

    @Retryable(maxAttempts = 3)
    public ExchangeRatesResponse fetchRates() {
        String symbols = properties.symbols().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        Duration timeout = properties.requestTimeout();

        ExchangeRatesResponse response = webClientBuilder
                .baseUrl(properties.baseUrl())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/live")
                        .queryParam("access_key", properties.apiKey())
                        .queryParam("base", properties.baseCurrency())
                        .queryParam("symbols", symbols)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRatesResponse.class)
                .block(timeout);

        if (response == null || !response.success() || response.rates() == null || response.rates().isEmpty()) {
            throw new CurrencyRatesFetchException("Failed to fetch currency rates");
        }

        return response;
    }
}
