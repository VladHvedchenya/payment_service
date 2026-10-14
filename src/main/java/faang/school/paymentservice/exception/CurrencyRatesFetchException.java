package faang.school.paymentservice.exception;

public class CurrencyRatesFetchException extends RuntimeException {
    public CurrencyRatesFetchException(String message) {
        super(message);
    }
}