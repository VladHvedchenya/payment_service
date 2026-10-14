package faang.school.paymentservice.client;

import faang.school.paymentservice.client.dto.ExchangeRatesResponse;
import faang.school.paymentservice.config.CurrencyRatesProperties;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.exception.CurrencyRatesFetchException;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExchangeRatesClientTest {

    private static final CurrencyRatesProperties PROPERTIES = new CurrencyRatesProperties(
            "0 0 0 * * *",
            "https://api.exchangerate.host",
            "test-key",
            Currency.USD,
            List.of(Currency.EUR),
            Duration.ofSeconds(5)
    );

    @Test
    void fetchRatesShouldRequestExternalApiAndReturnParsedResponse() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(request -> {
                    assertThat(request.url().getPath()).isEqualTo("/live");
                    assertThat(request.url().getQuery()).contains(
                            "access_key=test-key",
                            "base=USD",
                            "symbols=EUR"
                    );

                    return Mono.just(jsonResponse("""
                            {
                              "success": true,
                              "base": "USD",
                              "rates": {
                                "EUR": 0.92
                              }
                            }
                            """));
                });
        ExchangeRatesClient client = new ExchangeRatesClient(webClientBuilder, PROPERTIES);

        ExchangeRatesResponse response = client.fetchRates();

        assertThat(response.success()).isTrue();
        assertThat(response.baseCurrency()).isEqualTo("USD");
        assertThat(response.rates()).containsKey("EUR");
        assertThat(response.rates().get("EUR")).isEqualByComparingTo("0.92");
    }

    @Test
    void fetchRatesShouldThrowExceptionWhenResponseIsNotSuccessful() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(request -> Mono.just(jsonResponse("""
                        {
                          "success": false,
                          "base": "USD",
                          "rates": {}
                        }
                        """)));
        ExchangeRatesClient client = new ExchangeRatesClient(webClientBuilder, PROPERTIES);

        assertThatThrownBy(client::fetchRates)
                .isInstanceOf(CurrencyRatesFetchException.class)
                .hasMessage("Failed to fetch currency rates");
    }

    private static ClientResponse jsonResponse(String body) {
        return ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .build();
    }
}
