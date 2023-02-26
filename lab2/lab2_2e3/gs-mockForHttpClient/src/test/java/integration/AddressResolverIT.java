package integration;

import connection.ISimpleHttpClient;
import connection.TqsBasicHttpClient;
import exceptions.InvalidCoordinatesException;
import geocoding.Address;
import geocoding.AddressResolver;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AddressResolverIT {

    TqsBasicHttpClient httpClient = new TqsBasicHttpClient();


    AddressResolver resolver = new AddressResolver(httpClient);

    @Test
    public void whenGoodCoordidates_returnAddress() throws IOException, URISyntaxException, ParseException, InvalidCoordinatesException {
        Optional<Address> result = resolver.findAddressForLocation(40.633116,-8.658784);
        assertEquals(result, Optional.of(new Address( "Avenida João Jacinto de Magalhães", "Aveiro", "", "3810-149", null)));
    }

    @Test
    public void whenBadCoordidates_thenReturnNoValidAddrress() throws IOException, URISyntaxException, ParseException {
        assertThrows(InvalidCoordinatesException.class, () -> resolver.findAddressForLocation(-300, -810));
    }

}
