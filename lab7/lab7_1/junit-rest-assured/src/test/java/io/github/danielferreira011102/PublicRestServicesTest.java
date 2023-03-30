package io.github.danielferreira011102;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PublicRestServicesTest {

    private String API_URL = "https://jsonplaceholder.typicode.com/todos";

    @Test
    void testEndpointIsAvailable() {
        given().when().get(API_URL).then().assertThat().statusCode(200);
    }

    @Test
    void testQueryObject() {
        given().when().get(API_URL).then().assertThat().statusCode(200)
                .body("[3].title", equalTo("et porro tempora"));
    }

    @Test
    void testContains() {
        given().when().get(API_URL).then().assertThat().statusCode(200)
                .body("", hasSize(200))
                .body("id", hasItems(198, 199));
    }

    @Test
    void testTimeTaken() {
        given().when().get(API_URL).then().assertThat().statusCode(200)
                .body("", hasSize(200))
                .time(lessThan(2L*1000));
    }
}
