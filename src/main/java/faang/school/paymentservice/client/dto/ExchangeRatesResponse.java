package faang.school.paymentservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRatesResponse(
        boolean success,
        @JsonProperty("base")
        String baseCurrency,
        Map<String, BigDecimal> rates
) {
}
