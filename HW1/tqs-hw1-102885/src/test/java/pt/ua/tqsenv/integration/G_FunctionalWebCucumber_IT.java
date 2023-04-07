package pt.ua.tqsenv.integration;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pt.ua.tqsenv.AirQualityApplication;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("pt/ua/tqsenv")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "pt.ua.tqsenv")
public class G_FunctionalWebCucumber_IT {

    private static ConfigurableApplicationContext appContext;

    @BeforeAll
    public static void startSpringBootApplication() {
        appContext = SpringApplication.run(AirQualityApplication.class);
    }

    @AfterAll
    public static void stopSpringBootApplication() {
        appContext.stop();
    }

}
