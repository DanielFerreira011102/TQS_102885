package pt.ua.tqsenv.exceptions;

public class AirQualityServiceException extends Exception {
    public AirQualityServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public AirQualityServiceException(String message) {
        super(message);
    }
}
