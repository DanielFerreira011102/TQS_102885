/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geocoding;

import connection.ISimpleHttpClient;
import exceptions.InvalidCoordinatesException;
import org.apache.http.ParseException;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * calls external api to perform the reverse geocode
 *
 * @author ico
 */
public class AddressResolver {

    private final ISimpleHttpClient httpClient;

    public AddressResolver(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public Optional<Address> findAddressForLocation(double latitude, double longitude) throws URISyntaxException, IOException, ParseException, org.json.simple.parser.ParseException, InvalidCoordinatesException {

        if (latitude > 180 || latitude < -180 || longitude > 180 || longitude < -180) {
            throw new InvalidCoordinatesException("Invalid coordinates!");
        }

        String apiKey = ConfigUtils.getPropertyFromConfig("mapquest_key");

        URIBuilder uriBuilder = new URIBuilder("https://www.mapquestapi.com/geocoding/v1/reverse");
        uriBuilder.addParameter("key", apiKey);
        uriBuilder.addParameter("location", (new Formatter()).format(Locale.US, "%.6f,%.6f", latitude, longitude).toString());



        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, apiResponse);

        // get parts from response till reaching the address
        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        // get the first element of the results array
        obj = (JSONObject) ((JSONArray) obj.get("results")).get(0);

        if (((JSONArray) obj.get("locations")).isEmpty()) {
            return Optional.empty();
        } else {
            JSONObject address = (JSONObject) ((JSONArray) obj.get("locations")).get(0);

            String road = (String) address.get("street");
            String city = (String) address.get("adminArea5");
            String state = (String) address.get("adminArea3");
            String zip = (String) address.get("postalCode");
            return Optional.of(new Address(road, city, state, zip, null));


        }
    }
}
